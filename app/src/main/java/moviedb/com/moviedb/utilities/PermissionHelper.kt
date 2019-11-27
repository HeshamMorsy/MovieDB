package moviedb.com.moviedb.utilities

import android.Manifest
import androidx.core.os.HandlerCompat.postDelayed
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.app.Activity
import android.content.Context
import moviedb.com.moviedb.utilities.Constants.Companion.EXTERNAL_PERMISSION_CODE


class PermissionHelper {
    companion object {
        fun checkExternalStoragePermission(mActivity: Activity, mContext: Context): Boolean {
            var isGranted = false
            try {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        mActivity,
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        EXTERNAL_PERMISSION_CODE
                    )

                } else {
                    isGranted = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return isGranted
        }
    }
}