package com.kidsharu.kidsharu.other

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Toast
import com.kidsharu.kidsharu.activity.*
import com.kidsharu.kidsharu.dialog.LoadingDialogHelper
import com.kidsharu.kidsharu.model.Album
import com.kidsharu.kidsharu.model.Picture

object ActivityUtil {

    fun setFullScreen(window: Window) {
        val flags = arrayListOf(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE,
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN,
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,
                View.SYSTEM_UI_FLAG_FULLSCREEN
        )

        var tmp = window.decorView.systemUiVisibility
        for (flag in flags)
            tmp = tmp or flag
        window.decorView.systemUiVisibility = tmp

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Handler().postDelayed({ setFullScreen(window) }, 4000)
            }
        }
    }

    fun setStatusBarColor(activity: Activity, color: Int, isLightBackground: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.statusBarColor = color
            val flag = activity.window.decorView.systemUiVisibility
            activity.window.decorView.systemUiVisibility = if (isLightBackground) {
                flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    fun login(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    fun registerTeacher(context: Context) {
        context.startActivity(Intent(context, RegisterTeacherActivity::class.java))
    }

    fun registerParent(context: Context) {
        context.startActivity(Intent(context, RegisterParentActivity::class.java))
    }

    fun teacherHome(context: Context) {
        context.startActivity(Intent(context, TeacherHomeActivity::class.java))
    }

    fun parentHome(context: Context) {
        context.startActivity(Intent(context, ParentHomeActivity::class.java))
    }

    fun albumDetail(context: Context,
                    album: Album) {
        LoadingDialogHelper.show(context)
        ServerClient.albumPictureList(album.albumId) { pictures, errMsg ->
            LoadingDialogHelper.dismiss()
            if (errMsg != null) {
                Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
            } else {
                println("album $album")
                val intent = Intent(context, AlbumDetailActivity::class.java)
                intent.putExtra(AlbumDetailActivity.ALBUM_INTENT_KEY, album)
                intent.putExtra(AlbumDetailActivity.PICTURES_INTENT_KEY, pictures)
                context.startActivity(intent)
            }
        }
    }

    fun albumAdd(context: Context) {
        context.startActivity(Intent(context, AlbumAddActivity::class.java))
    }

    fun pictureDetail(context: Context,
                      pictures: Array<Picture>,
                      nowPage: Int? = null) {
        val intent = Intent(context, PictureActivity::class.java)
        nowPage?.let {
            intent.putExtra(PictureActivity.POSITION_INTENT_KEY, it)
        }
        intent.putExtra(PictureActivity.PICTURES_INTENT_KEY, pictures)
        context.startActivity(intent)
    }

    fun teacherPicture(context: Context,
                       album: Album,
                       pictures: Array<Picture>,
                       nowPage: Int? = null) {
        val intent = Intent(context, TeacherPictureActivity::class.java)
        nowPage?.let {
            intent.putExtra(TeacherPictureActivity.POSITION_INTENT_KEY, it)
        }
        intent.putExtra(TeacherPictureActivity.ALBUM_INTENT_KEY, album)
        intent.putExtra(TeacherPictureActivity.PICTURES_INTENT_KEY, pictures)
        context.startActivity(intent)
    }
}