

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.File

class CSV(private var ctx: Context) {

    fun selectFile(): Intent {

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            addCategory(Intent.CATEGORY_ALTERNATIVE)
            type = "text/csv";
        }
        return intent
    }

    fun readFile(uri: Uri) {
        var path: String? = null;
        if ("CONTENT".equals(uri.scheme!!.toUpperCase())) {
            val projection: String = "_data"
            var cursor: Cursor;

            try {
                cursor = ctx.contentResolver.query(uri, arrayOf(projection), null, null, null)!!
                var column_index: Int = cursor.getColumnIndexOrThrow(projection)
                if (cursor.moveToFirst()) {
                    path = cursor.getString(column_index)
                }
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if ("FILE".equals(uri.scheme!!.toUpperCase())) {
            path = uri.path;
        }

        if (!path.isNullOrEmpty()) {
            if (checkPermissionREAD_EXTERNAL_STORAGE()) {
                Log.d(TAG, "readFile: $path")
                val file = File(path.toString())
                file.readLines().forEach { line ->
                    Log.d(TAG, "readFile: $line")
                }
            }
        }

    }

    fun checkPermissionREAD_EXTERNAL_STORAGE(): Boolean {
        var hasPermission = false

        val currentAPIVersion: Int = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ctx.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                hasPermission = true
            }
        }

        return hasPermission
    }

}