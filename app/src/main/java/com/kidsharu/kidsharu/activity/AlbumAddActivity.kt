package com.kidsharu.kidsharu.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.esafirm.imagepicker.features.ImagePicker
import com.kidsharu.kidsharu.R
import com.kidsharu.kidsharu.other.ActivityUtil
import com.kidsharu.kidsharu.service.AlbumAddService
import kotlinx.android.synthetic.main.activity_album_add.*
import kotlinx.android.synthetic.main.toolbar_album_add.*

class AlbumAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_add)
        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        ActivityUtil.setStatusBarColor(this, Color.WHITE, true)

        confirm_button.setOnClickListener {
            ImagePicker.create(this).folderMode(true).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            startService(Intent(this, AlbumAddService::class.java).apply {
                putExtra(AlbumAddService.TITLE_INTENT_KEY, album_title_field.text.toString())
                putExtra(AlbumAddService.CONTENT_INTENT_KEY, album_content_field.text.toString())
                putExtra(AlbumAddService.PATHS_INTENT_KEY, ImagePicker.getImages(data).map { it.path }.toTypedArray())
            })

            finish()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}