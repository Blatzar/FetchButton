package com.lagradost.fetchbutton.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.lagradost.fetchbutton.CustomFetchListener
import com.lagradost.fetchbutton.R
import com.lagradost.fetchbutton.globalFetch
import com.lagradost.fetchbutton.utils.Coroutines.ioThread
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2.Status

open class PieFetchButton(context: Context, attributeSet: AttributeSet) :
    BaseFetchButton(context, attributeSet) {

    private lateinit var progressBarBackground: View
    private lateinit var statusView: ImageView
    override var fetchListener: FetchListener = CustomFetchListener()

    override fun init() {
        inflate(R.layout.download_button_view)
        progressBar = findViewById(R.id.progress_downloaded)
        progressBarBackground = findViewById(R.id.progress_downloaded_background)
        statusView = findViewById(R.id.image_download_status)
        setStatus(Status.NONE)
    }

    open fun setDefaultClickListener(requestGetter: suspend (Context) -> Request) {
        this.setOnClickListener {
            when (this.currentStatus) {
                null, Status.NONE -> ioThread {
                    val request = requestGetter.invoke(context)
                    performDownload(request)
                }
                Status.PAUSED -> {
                    this.resumeDownload()
                }
                Status.DOWNLOADING -> {
                    this.pauseDownload()
                }
                else -> {}
            }
        }
    }

    /** Also sets currentStatus */
    open fun setStatus(status: Status) {
        currentStatus = status

        progressBar.isVisible = status != Status.NONE && status != Status.COMPLETED
        progressBarBackground.isVisible = status != Status.NONE && status != Status.COMPLETED
        statusView.isVisible = status == Status.NONE || status == Status.COMPLETED

        if (status == Status.QUEUED || status == Status.ADDED) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.rotate_around_center_point)
            progressBarBackground.startAnimation(animation)
        } else {
            progressBarBackground.clearAnimation()
        }

        val progressDrawable =
            if (status == Status.DOWNLOADING) R.drawable.circle_shape else R.drawable.circle_shape_dotted

        progressBarBackground.background =
            ContextCompat.getDrawable(context, progressDrawable)

        val drawable = getDrawableFromStatus(status)
        statusView.setImageDrawable(drawable)
    }

    override fun resetView() {
        setStatus(Status.NONE)
        progressBar.progress = 0
    }

    override fun updateViewOnDownload(download: Download) {
        if (arrayOf(
                Status.REMOVED,
                Status.DELETED,
                Status.FAILED
            ).contains(download.status)
        ) {
            resetView()
        } else {
            progressBar.progress = download.progress
            setStatus(download.status)
        }
    }

    open fun getDrawableFromStatus(status: Status): Drawable? {
        val drawableInt = when (status) {
            Status.PAUSED -> null
            Status.DOWNLOADING -> null
            Status.QUEUED -> null
            Status.FAILED -> R.drawable.netflix_download
            Status.COMPLETED -> R.drawable.download_icon_load
            Status.NONE -> R.drawable.netflix_download
            else -> R.drawable.netflix_download
        }
        return drawableInt?.let { ContextCompat.getDrawable(this.context, it) }
    }
}