package com.example.cableguyportalchecker.Util

import java.util.Date

object PortalData {

    var frontend_url: Url? = null;
    var backend_url: Url? = null;
    var name: String? = null;
    var importance: Int = 0;
    var last_check: Date? = null;

    fun PortalData(row: List<String>) {
        frontend_url = Url(row[0])
        backend_url = Url(row[1])
        name = row[2]
        importance = row[3].toInt()
    }

}