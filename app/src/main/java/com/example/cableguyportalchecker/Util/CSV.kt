

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.BufferedReader
import java.io.InputStreamReader

class CSV(private var ctx: Context) {

    public val PICK_CSV_FILE = 2;
    private val _csv_import_headers = listOf<String>("frontend","backend","name","importance")

    fun selectFile(): Intent {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv";

        }

        return intent
    }

    fun readFile(uri: Uri): List<List<String>> {

        var rows: List<List<String>> = listOf();
        var stringBuilder: String? = null;
        ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                stringBuilder = reader.readText()
            }
        }

        if (!stringBuilder.isNullOrEmpty()) {
            rows = csvReader().readAll(stringBuilder.toString())
        }
        if (validateCSV(rows[0])) return rows

        return listOf()
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

    fun validateCSV(headers: List<String>): Boolean {
        var is_csv_valid: Boolean = false;
        if (headers.size == _csv_import_headers.size) {
            var index = 0
            for (header in _csv_import_headers) {
                if (header === headers[index]) {
                    is_csv_valid = false
                    break
                }
                index++
            }

        } else {
            is_csv_valid = false
            // condition for header are not complete CSV
        }
        return is_csv_valid
    }

    fun saveCSV(rows: List<List<String>>) {

    }

}