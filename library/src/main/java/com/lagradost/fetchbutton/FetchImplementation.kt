package com.lagradost.fetchbutton

import android.content.Context
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2okhttp.OkHttpDownloader

const val FETCH_KEY_INFO_KEY = "FETCH_INFO"

//fun downloadEpisodeUsingFetch(
//    context: Context?,
//    folder: String?,
//    ep: NotificationMetaData,
//    links: List<ExtractorLink>,
//) {
//    val fetchConfiguration = FetchConfiguration.Builder(context)
//        .setDownloadConcurrentLimit(5)
//        .setHttpDownloader(OkHttpDownloader(app.baseClient))
//        .setNotificationManager(EasierFetchNotificationManager(context))
//        .build()
//
//    globalFetch = globalFetch ?: Fetch.Impl.getInstance(fetchConfiguration)
//    globalFetch?.let { fetch ->
//        val request = Request(firstUrl.url, realFile.uri).apply {
//            this.addHeader("User-Agent", USER_AGENT)
//
//            this.identifier = ep.id.toLong()
//            this.groupId = ep.mainName.hashCode()
//            this.priority = Priority.HIGH
//
//            firstUrl.headers.forEach { this.addHeader(it.key, it.value) }
//        }
//
//        fetch.enqueue(request, {}, { println("FETCH ENQUEUE ERROR: $it") })
//            .addListener(CustomFetchListener())
//        println("ENQUEUING REQUEST ${request.url}")
//    }
//
//}