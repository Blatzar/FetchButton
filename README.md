# FetchButton

[Work in progress]

Just a simple library to have a download button which works with Fetch. Mostly for personal usage.

TODO:
- [] Themeable/Attributes
- [] Testing with recyclerview
- [] More default download buttons

### Usage:
```xml
<com.lagradost.fetchbutton.ui.PieFetchButton
    android:id="@+id/download_button"
    android:layout_width="80dp"
    android:layout_height="80dp">
</com.lagradost.fetchbutton.ui.PieFetchButton>
```

```kotlin
val downloadButton = findViewById<PieFetchButton>(R.id.download_button)

// This is required to run once in the app before any fetch usage.
initFetch(this)

// Arbitrary path
val path = this.filesDir.path + "/FetchButtonTest/test.bin"

// This is just to play around with the default behavior, use
// downloadButton.pauseDownload() and such to control the download easily
// To subscribe to another download, just use downloadButton.currentRequestId = fetchRequestId
downloadButton.setDefaultClickListener {
    com.tonyodev.fetch2.Request(
        // Download url
        "https://speedtest-co.turnkeyinternet.net/1000mb.bin",
        path,
    )
}
```