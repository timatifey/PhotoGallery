package dev.timatifey.gallery.screens.common

import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import dev.timatifey.gallery.R
import dev.timatifey.gallery.screens.common.base.BaseActivity

class MainActivity : BaseActivity(), ToolbarController {
    private lateinit var toolbar: Toolbar
    private lateinit var title: TextView
    private lateinit var backButton: ImageButton
    private lateinit var backTitle: TextView

    private lateinit var noConnection: TextView
    private lateinit var reconnectBtn: Button
    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        title = toolbar.findViewById(R.id.toolbar_title)
        backButton = toolbar.findViewById(R.id.toolbar_back)
        backTitle = toolbar.findViewById(R.id.toolbar_back_title)
        backButton.setOnClickListener {
            onBackPressed()
        }

        noConnection = findViewById(R.id.activity_main__no_connection)
        reconnectBtn = findViewById(R.id.activity_main__reconnect)
        container = findViewById(R.id.activity_main__container)

        tryInit(savedInstanceState)
    }

    fun tryInit(savedInstanceState: Bundle?) {
        if (hasConnection()) {
            container.isVisible = true
            noConnection.isVisible = false
            reconnectBtn.isVisible = false
            appRouter.initialize(savedInstanceState)
        } else {
            setTitle("No connection")
            container.isVisible = false
            noConnection.isVisible = true
            reconnectBtn.apply {
                isVisible = true
                setOnClickListener {
                    tryInit(savedInstanceState)
                }
            }
        }
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setupBack(backTitle: String, isVisible: Boolean) {
        backButton.isVisible = isVisible
        this.backTitle.apply {
            this.isVisible = isVisible
            text = backTitle
        }
    }

}

interface ToolbarController {
    fun setTitle(title: String)
    fun setupBack(backTitle: String, isVisible: Boolean)
}