package uk.ac.tees.mad.gifttrack.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cloudinary.Cloudinary
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.gifttrack.data.local.dao.GiftDao
import uk.ac.tees.mad.gifttrack.data.local.entity.PendingGiftEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.io.File

@HiltWorker
class GiftSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: GiftDao,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val cloudinary: Cloudinary
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val uid = auth.currentUser?.uid ?: return Result.failure()
        val pendingGifts = dao.getAllPendingGifts()

        for (gift in pendingGifts) {
            try {
                // Upload image to Cloudinary if exists
                val imageUrl = gift.imageUri?.let { uri ->
                    val file = File(uri)
                    val uploadResult = cloudinary.uploader().upload(file, mapOf())
                    uploadResult["secure_url"] as? String
                }

                // Upload to Firestore
                val giftDoc = mapOf(
                    "title" to gift.title,
                    "recipientName" to gift.recipientName,
                    "occasion" to gift.occasion,
                    "price" to gift.price,
                    "notes" to gift.notes,
                    "imageUrl" to imageUrl,
                    "date" to gift.date,
                    "status" to gift.status
                )

                firestore.collection("users")
                    .document(uid)
                    .collection("gifts")
                    .add(giftDoc)
                    .await()

                // Remove from pending
                dao.deletePendingGift(gift.localId)
            } catch (e: Exception) {
                e.printStackTrace()
                return Result.retry()
            }
        }
        return Result.success()
    }
}