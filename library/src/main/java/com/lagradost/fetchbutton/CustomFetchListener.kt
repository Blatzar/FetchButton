package com.lagradost.fetchbutton

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.lagradost.fetchbutton.utils.Coroutines.mainThread
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock

object DownloadListener {
    val currentDownloadData: HashMap<Long, Download> = hashMapOf()
    val liveData = MutableLiveData<Download>()

    fun observe(scope: LifecycleOwner, collector: (Download) -> Unit) {
        mainThread {
            liveData.observe(scope) {
                collector(it)
            }
        }
    }
}


fun runDownloadAction(state: DownloadActionType, downloadId: Int) {
    when (state) {
        DownloadActionType.Pause -> globalFetch?.pause(downloadId)
        DownloadActionType.Stop -> globalFetch?.cancel(downloadId)
        DownloadActionType.Resume -> globalFetch?.resume(downloadId)
    }
}


class CustomFetchListener : FetchListener {
    private fun setDownloadStatus(download: Download) {
        DownloadListener.currentDownloadData[download.identifier] = download
        println("EMIT DOWNLOAD $download")
        DownloadListener.liveData.postValue(download)
    }

    override fun onAdded(download: Download) {
        println("onAdded ${download.created}")
        setDownloadStatus(download)
    }

    override fun onCancelled(download: Download) {
        setDownloadStatus(download)
    }

    override fun onCompleted(download: Download) {
        println("onCompleted ${download.downloaded}")
        setDownloadStatus(download)
    }

    override fun onDeleted(download: Download) {
        setDownloadStatus(download)
    }

    override fun onDownloadBlockUpdated(
        download: Download,
        downloadBlock: DownloadBlock,
        totalBlocks: Int
    ) {
    }

    override fun onError(
        download: Download,
        error: com.tonyodev.fetch2.Error,
        throwable: Throwable?
    ) {
        println("onError $error")
        setDownloadStatus(download)
    }

    override fun onPaused(download: Download) {
        setDownloadStatus(download)
    }

    override fun onProgress(
        download: Download,
        etaInMilliSeconds: Long,
        downloadedBytesPerSecond: Long
    ) {
        println("onProgress $etaInMilliSeconds ::: $downloadedBytesPerSecond ::: DOWNLOADED ${download.total}")
        setDownloadStatus(download)
    }

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        println("onQueued ${download.downloaded} ::: $waitingOnNetwork")
        setDownloadStatus(download)
    }

    override fun onRemoved(download: Download) {
        println("onRemoved ${download.downloaded}")
        setDownloadStatus(download)
    }

    override fun onResumed(download: Download) {
        setDownloadStatus(download)
    }

    override fun onStarted(
        download: Download,
        downloadBlocks: List<DownloadBlock>,
        totalBlocks: Int
    ) {
        println("onStarted ${download.identifier}")
        setDownloadStatus(download)
    }

    override fun onWaitingNetwork(download: Download) {
        println("onWaitingNetwork ${download.downloaded}")
        setDownloadStatus(download)
    }
}