package com.example.cableguyportalchecker

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cableguyportalchecker.Util.Url
import com.example.cableguyportalchecker.Util.Url.Ping

class PortalItem {

    @Composable
    fun PortalItems(domain: String, name: String, user: String, pass: String, importance: Int, context: Context) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp, 0.dp, 10.dp)
        ) {

            var isStatePing by remember {
                mutableStateOf(0)
            }
            var pingResponse by remember {
                mutableStateOf(Ping())
            }
            var isStateBrowser by remember {
                mutableStateOf(0)
            }
            var portalResponse by remember {
                mutableStateOf(0)
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4.5f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
            loaderIcon(
                modifier = Modifier
                    .weight(1f),
                icon = R.drawable.network_ping,
                successIcon = R.drawable.network_ping_success,
                isState = isStatePing,
                desc = "Check if the Server Network is UP",
                onclick =  {
                    isStatePing = 4;
//                    Log.d("test","test" + isStatePing)
                    pingResponse = Url(
                        domain
                    ).ping(context)
//                    Log.d(TAG, "PortalItems: " + pingResponse.host)
                    if (pingResponse.status) {
                        if (pingResponse.cnt > 100) {
                            isStatePing = 2
                        } else {
                            isStatePing = 1
                        }
                    } else {
                        isStatePing = 3
                    }
                })
            loaderIcon(
                modifier = Modifier
                    .weight(1f),
                icon = R.drawable.web_up,
                successIcon = R.drawable.web_up_success,
                isState = isStateBrowser,
                desc = "Check if the Portal is Opening",
                onclick =  {
                    isStateBrowser = 4;
//                    Log.d("test","test" + isStatePing)
                    portalResponse = Url(
                        domain
                    ).isUP(context)
                    Log.d(TAG, "PortalItems: " + portalResponse)
                    if (portalResponse == 1) {
                        isStateBrowser = 1
                    } else {
                        isStateBrowser = 3
                    }
                }
            )
            loaderIcon(
                modifier = Modifier
                    .weight(1f),
                icon = R.drawable.visible,
                isState = 0,
                desc = "Open in Browser"
            ) {

            }
        }
    }

    @Composable
    fun loaderIcon(modifier: Modifier,icon: Int,successIcon: Int = 0, isState: Int = 0, desc: String, onclick: () -> Unit) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(10.dp, 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onclick) {
                if (isState == 4) {
                    CircularProgressIndicator()
                } else if (isState == 3) {
                    Image(painterResource(id = R.drawable.error), contentDescription = "ERROR : " + desc)
                } else if (isState == 2) {
                    Image(painterResource(id = R.drawable.warning), contentDescription = "WARNING : " + desc)
                } else if (isState == 1) {
                    if (successIcon != 0) {
                        Image(painterResource(id = successIcon), contentDescription = "SUCCESS : " + desc)
                    } else {
                        Image(painterResource(id = icon),contentDescription = desc)
                    }
                } else {
                    Image(painterResource(id = icon),contentDescription = desc)
                }
            }
        }
    }
}