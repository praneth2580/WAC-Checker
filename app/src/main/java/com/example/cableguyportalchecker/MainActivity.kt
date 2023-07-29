package com.example.cableguyportalchecker

import CSV
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.bottomAppBarFabElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cableguyportalchecker.ui.theme.CableguyPortalCheckerTheme
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.json.JSONArray

class MainActivity : ComponentActivity() {

    val csv: CSV = CSV(this@MainActivity);

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CableguyPortalCheckerTheme {
                // A surface container using the 'background' color from the theme

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(onClick = {

                                    val intent: Intent = csv.selectFile();
                                    startActivityForResult(intent,csv.PICK_CSV_FILE)
//                                    resultLauncher.launch(intent);

                                }) {
                                    Image(
                                        painterResource(id = R.drawable.add),
                                        contentDescription = "Ping All protals",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Image(
                                        painterResource(id = R.drawable.network_ping),
                                        contentDescription = "Ping All protals",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Image(
                                        painterResource(id = R.drawable.web_up),
                                        contentDescription = "Check if All Portals",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Image(
                                        painterResource(id = R.drawable.settings),
                                        contentDescription = "Settings",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { /* FAB onClick */ },
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = bottomAppBarFabElevation()
                                ) {
                                    Icon(Icons.Filled.Refresh, "Refresh All Portals")
                                }
                            }
                        )
                    }
//                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting(
                        JSONArray(),
                        modifier = Modifier.padding(it),
                        this
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == csv.PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {

            data?.data?.also { uri ->
                csv.readFile(uri)
//                val contentResolver = applicationContext.contentResolver
//                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                contentResolver.takePersistableUriPermission(uri, takeFlags)
            }

        }

    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onCreate: ${result.data?.toURI()}")
            if (!csv.checkPermissionREAD_EXTERNAL_STORAGE()) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE: false")
                requestPermissions(arrayOf(Manifest.permission_group.STORAGE),101);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE: true")
                csv.readFile(Uri.parse(result.data?.toURI()))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode + " " + grantResults)
        if (requestCode == 101) {
            resultLauncher.launch(intent);
        }
    }
}

@Composable
fun Greeting(list: JSONArray, modifier: Modifier = Modifier, ctx: Context ) {
    LazyColumn(
        modifier = modifier
    ) {
        items(10) {
            PortalItem().PortalItems(domain = "https://www.google.com/", name = "Google", user = "A", pass = "B",0, context = ctx)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CableguyPortalCheckerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                painterResource(id = R.drawable.add),
                                contentDescription = "Ping All protals",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                painterResource(id = R.drawable.network_ping),
                                contentDescription = "Ping All protals",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                painterResource(id = R.drawable.web_up),
                                contentDescription = "Check if All Portals",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* FAB onClick */ },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Refresh, "Refresh All Portals")
                        }
                    }
                )
            }
//                    color = MaterialTheme.colorScheme.background,
        ) {
            Greeting(
                JSONArray(),
                modifier = Modifier.padding(it),
                LocalContext.current
            )
        }
    }
}