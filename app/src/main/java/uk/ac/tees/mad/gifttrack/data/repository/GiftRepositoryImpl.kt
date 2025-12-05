package uk.ac.tees.mad.gifttrack.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.local.entity.PendingGiftEntity
import uk.ac.tees.mad.gifttrack.data.local.mappers.toDomain
import uk.ac.tees.mad.gifttrack.data.local.mappers.toEntity
import uk.ac.tees.mad.gifttrack.domain.model.Gift
import uk.ac.tees.mad.gifttrack.domain.model.GiftStatus
import uk.ac.tees.mad.gifttrack.domain.repository.GiftRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class GiftRepositoryImpl @Inject constructor(
    private val giftDao: GiftDao,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : GiftRepository {

    private val cloudName = "dmgwqrc5b"
    private val uploadPreset = "unsigned_gift_upload"

    override fun getAllGifts(): Flow<List<Gift>> {
        return giftDao.getAllGifts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertGift(gift: Gift) {
        giftDao.insertGift(gift.toEntity())
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("gifts")
            .document(gift.id)
            .set(gift)
            .await()
    }

    override suspend fun deleteGift(id: String) {
        giftDao.deleteGiftById(id)
    }

    override suspend fun addGiftToFirestore(gift: Gift) {
        val uid = auth.currentUser?.uid ?: return
//        if (uid == null) {
//            giftDao.insertPendingGift(
//                PendingGiftEntity(
//                    title = gift.title,
//                    recipientName = gift.recipientName,
//                    occasion = gift.occasion,
//                    price = gift.price,
//                    notes = gift.notes,
//                    imageUri = gift.imageUrl,
//                    date = gift.date,
//                    status = when (gift.status) {
//                        GiftStatus.GIVEN -> 0
//                        GiftStatus.PLANNED -> 1
//                        GiftStatus.RECEIVED -> 2
//                    }
//                )
//            )
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//
//            val work = OneTimeWorkRequestBuilder<GiftSyncWorker>()
//                .setConstraints(constraints)
//                .build()
//
//            WorkManager.getInstance(context).enqueue(work)
//            return
//        }

        firestore.collection("users")
            .document(uid)
            .collection("gifts")
            .document(gift.id)
            .set(gift)
            .await()

        giftDao.insertGift(gift.toEntity())
    }

    override suspend fun updateGift(gift: Gift) {
        giftDao.updateGift(gift.toEntity())
    }

    override suspend fun updateGiftInFirestore(gift: Gift) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            // Offline fallback: mark for pending sync
            giftDao.insertPendingGift(
                PendingGiftEntity(
                    title = gift.title,
                    recipientName = gift.recipientName,
                    occasion = gift.occasion,
                    price = gift.price,
                    notes = gift.notes,
                    imageUri = gift.imageUrl,
                    date = gift.date,
                    status = when (gift.status) {
                        GiftStatus.GIVEN -> 0
                        GiftStatus.PLANNED -> 1
                        GiftStatus.RECEIVED -> 2
                    }
                )
            )
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("gifts")
            .document(gift.id)
            .set(gift)
            .await()

        giftDao.updateGift(gift.toEntity())
    }

    override suspend fun uploadGiftImage(localUri: Uri): String {
        return withContext(Dispatchers.IO) {
            try {
                val file = uriToTempFile(localUri)

                val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaType()))
                    .addFormDataPart("upload_preset", uploadPreset)
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()

                val request = Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val body =
                    response.body?.string() ?: throw Exception("Empty response from Cloudinary")
                val json = JSONObject(body)
                val url = json.optString("secure_url", "")
                if (url.isBlank()) throw Exception("Cloudinary upload failed (no secure_url). body=$body")
                url
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    private fun uriToTempFile(uri: Uri): File {
        val input = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI: $uri")
        val temp = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(temp).use { out ->
            input.copyTo(out)
        }
        input.close()
        return temp
    }

    override suspend fun syncFromFirestore() {
        val uid = auth.currentUser?.uid ?: return
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("gifts")
            .get()
            .await()
        val gifts = snapshot.toObjects(Gift::class.java)
        giftDao.insertAll(gifts.map { it.toEntity() })
    }
}