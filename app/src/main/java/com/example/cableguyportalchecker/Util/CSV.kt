

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class CSV(private var ctx: Context) {

    public val PICK_CSV_FILE = 2;

    fun selectFile(): Intent {

//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
////            addCategory
//
//            it.addCategory(Intent.CATEGORY_OPENABLE)
//            it.type = "text/csv";
//            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv";

        }

        return intent
    }

    fun readFile(uri: Uri) {

//        val stringBuilder = StringBuilder()
        var stringBuilder: String? = null;
        ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                stringBuilder = reader.readText()
//                var line: String? = reader.readLine();
//                Log.d(TAG, "readFile: " + line)
//                while (line != null) {
//                    stringBuilder.append("$line\\n")
//                    line = reader.readLine()
//                    Log.d(TAG, "readFile: " + line)
//                }
            }
        }

        if (!stringBuilder.isNullOrEmpty()) {
            Log.d(TAG, "readFile: " + stringBuilder)
            val rows: List<List<String>> = csvReader().readAll(stringBuilder.toString())
            println(rows)
        }

    }

    fun checkPermissionREAD_EXTERNAL_STORAGE(): Boolean {
        var hasPermission = false

        val currentAPIVersion: Int = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
//            if (ctx.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissionREAD_EXTERNAL_STORAGE: " + ctx.checkPermission(Manifest.permission_group.STORAGE,android.os.Process.myPid(),android.os.Process.myUid()) + " " + PackageManager.PERMISSION_GRANTED)
            if (ctx.checkPermission(Manifest.permission_group.STORAGE,android.os.Process.myPid(),android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                hasPermission = true
            }
        }

        return hasPermission
    }

}