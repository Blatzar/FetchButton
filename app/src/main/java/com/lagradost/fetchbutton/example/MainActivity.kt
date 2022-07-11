package com.lagradost.fetchbutton.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.fetchbutton.ui.PieFetchButton
import com.lagradost.fetchbutton.initFetch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadButton = findViewById<PieFetchButton>(R.id.download_button)

        initFetch(this)

        val path = this.filesDir.path + "/FetchButtonTest/test.bin"

        downloadButton.setDefaultClickListener {
            com.tonyodev.fetch2.Request(
                "https://speedtest-co.turnkeyinternet.net/1000mb.bin",
                path,
            ).also {
                println(it.id)
            }
        }
    }
}

