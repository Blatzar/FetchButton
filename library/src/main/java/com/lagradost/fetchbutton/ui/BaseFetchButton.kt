package com.lagradost.fetchbutton.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.lagradost.fetchbutton.DownloadListener
import com.lagradost.fetchbutton.globalFetch
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2.Status

abstract class BaseFetchButton(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    lateinit var progressBar: ContentLoadingProgressBar
    abstract var fetchListener: FetchListener

    var currentRequestId: Int? = null
    var currentStatus: Status? = null

    fun inflate(@LayoutRes layout: Int) {
        inflate(context, layout, this)
    }

    /**
     * Create your view here with inflate(layout) and other stuff.
     * Akin to onCreateView.
     * */
    abstract fun init()

    init {
        init()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Always listens to downloads
        observeAllDownloads(::updateViewOnDownloadWithChecks)
    }

    /**
     * Safer internal updateViewOnDownload
     * */
    private fun updateViewOnDownloadWithChecks(download: Download) {
        if (download.id == currentRequestId) {
            updateViewOnDownload(download)
        }
    }

    /**
     * No checks required. Arg will always include a download with current id
     * */
    abstract fun updateViewOnDownload(download: Download)

    /**
     * Look at all global downloads, used to subscribe to one of them.
     * */
    private fun observeAllDownloads(observer: (Download) -> Unit) {
        this.findViewTreeLifecycleOwner()?.let {
            DownloadListener.observe(it, observer)
        }
    }

    /**
     * Get a clean slate again, might be useful in recyclerview?
     * */
    abstract fun resetView()

    open fun performDownload(request: Request) {
        resetView()

        currentRequestId = request.id

        globalFetch?.enqueue(request, {}, { println("Failed download: $it") })
            ?.addListener(fetchListener)
    }

    fun pauseDownload() {
        currentRequestId?.let { id ->
            globalFetch?.pause(id)
        }
    }

    fun resumeDownload() {
        currentRequestId?.let { id ->
            globalFetch?.resume(id)
        }
    }

    fun cancelDownload() {
        currentRequestId?.let { id ->
            globalFetch?.cancel(id)
        }
    }
}