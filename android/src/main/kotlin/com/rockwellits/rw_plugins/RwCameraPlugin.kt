package com.rockwellits.rw_plugins.rw_camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.ByteArrayOutputStream


class CameraPluginActivity : Activity() {
    private val BASIC_CAMERA_REQUEST_CODE = 1889
    private val FILEPROVIDER_CAMERA_REQUEST_CODE = 1998
    private val EXTRA_RESULT = "data"
    private var basicPhoto = true
    private var video = false
    private var format = Bitmap.CompressFormat.PNG
    private var quality: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        video = intent.getBooleanExtra("video", false)
        basicPhoto = intent.getBooleanExtra("basicPhoto", true)

        if (intent.hasExtra("format")) {
            format = Bitmap.CompressFormat.valueOf(intent.getStringExtra("format"))
        }

        if (intent.hasExtra("quality")) {
            quality = intent.getIntExtra("quality", 100)
        }

        if (checkPermissions()) {
            launchApplet()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == RESULT_OK && intent != null) {
            if (video && intent.data is Uri) {
                RwCameraPlugin.onVideoResult(intent.data?.path)
            } else if (!basicPhoto && intent.data is Uri) {
                RwCameraPlugin.onPhotoFileResult(intent.data?.path)
            } else if (basicPhoto) {
                val bitmap = intent.extras!!.getParcelable<Bitmap>(EXTRA_RESULT)

                RwCameraPlugin.onPhotoBytesResult(bitmap, format, quality)
            }
        } else {
            RwCameraPlugin.onEmptyResult()
        }

        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            BASIC_CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchApplet()
                } else {
                    RwCameraPlugin.onEmptyResult()
                    finish()
                }
                return
            }

            else -> {
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    BASIC_CAMERA_REQUEST_CODE)

            return false
        }

        return true
    }

    private fun launchApplet() {
        val intent = Intent(if (video) MediaStore.ACTION_VIDEO_CAPTURE else
            MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(intent, if (!video && !basicPhoto) FILEPROVIDER_CAMERA_REQUEST_CODE else
            BASIC_CAMERA_REQUEST_CODE)
    }
}

class RwCameraPlugin(private val activity: Activity) : MethodCallHandler {
    companion object {
        private const val CHANNEL = "com.rockwellits.rw_plugins/rw_camera"
        private lateinit var channel: MethodChannel
        private lateinit var methodResult: Result

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            channel = MethodChannel(registrar.messenger(), CHANNEL)
            channel.setMethodCallHandler(RwCameraPlugin(registrar.activity()))
        }

        @JvmStatic
        fun onPhotoBytesResult(bitmap: Bitmap?, format: Bitmap.CompressFormat, quality: Int) {
            if (bitmap != null) {
                val stream = ByteArrayOutputStream()

                bitmap.compress(format, quality, stream)

                val bytes = stream.toByteArray()

                bitmap.recycle()
                methodResult.success(bytes)
            } else {
                methodResult.error(RwCameraPlugin::class.java.canonicalName, "Unable to take photo", null)
            }
        }

        @JvmStatic
        fun onPhotoFileResult(filePath: String?) {
            if (filePath != null) {
                methodResult.success(filePath)
            } else {
                methodResult.error(RwCameraPlugin::class.java.canonicalName, "Unable to take photo", null)
            }
        }

        @JvmStatic
        fun onVideoResult(filePath: String?) {
            if (filePath != null) {
                methodResult.success(filePath)
            } else {
                methodResult.error(RwCameraPlugin::class.java.canonicalName, "Unable to record video", null)
            }
        }

        @JvmStatic
        fun onEmptyResult() {
            methodResult.success(null)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "takePhotoToBytes") {
            val intent = Intent(activity, CameraPluginActivity::class.java)

            intent.putExtra("video", false)
            intent.putExtra("basicPhoto", true)
            intent.putExtra("format", call.argument<String>("format"))
            intent.putExtra("quality", call.argument<Int>("quality"))

            methodResult = result
            activity.startActivity(intent)
        } else if (call.method == "takePhotoToFile") {
            val intent = Intent(activity, CameraPluginActivity::class.java)

            intent.putExtra("video", false)
            intent.putExtra("basicPhoto", false)

            methodResult = result
            activity.startActivity(intent)
        } else if (call.method == "recordVideo") {
            val intent = Intent(activity, CameraPluginActivity::class.java)

            intent.putExtra("video", true)
            methodResult = result
            activity.startActivity(intent)
        } else {
            result.notImplemented()
        }
    }
}
