package uk.ac.tees.mad.gifttrack.ui.camera

import android.net.Uri
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor() : ViewModel() {

    val imageCapture = ImageCapture.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    fun savePhoto(file: File,  executor: Executor, onSaved: (Uri) -> Unit, onError: (Exception) -> Unit) {
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exeception: ImageCaptureException) = onError(exeception)
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onSaved(Uri.fromFile(file))
                }
            }
        )
    }
}