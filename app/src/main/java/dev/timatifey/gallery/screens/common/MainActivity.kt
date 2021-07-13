package dev.timatifey.gallery.screens.common

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import dev.timatifey.gallery.R
import dev.timatifey.gallery.screens.common.base.BaseActivity

class MainActivity : BaseActivity(), ToolbarController {
    lateinit var toolbar: Toolbar
    lateinit var title: TextView
    lateinit var backButton: ImageButton
    lateinit var backTitle: TextView

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
        appRouter.initialize(savedInstanceState)
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