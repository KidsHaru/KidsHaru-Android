package com.kidsharu.kidsharu.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.kidsharu.kidsharu.R
import com.kidsharu.kidsharu.dialog.LoadingDialogHelper
import com.kidsharu.kidsharu.model.Album
import com.kidsharu.kidsharu.model.AlbumStatus
import com.kidsharu.kidsharu.model.Picture
import com.kidsharu.kidsharu.other.AlbumModifyEvent
import com.kidsharu.kidsharu.other.CrashUtil
import com.kidsharu.kidsharu.other.EventBus
import com.kidsharu.kidsharu.other.ServerClient
import com.kidsharu.kidsharu.viewPager.PicturePagerAdapter
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_teacher_picture.*
import kotlinx.android.synthetic.main.toolbar_teacher_picture.*

class TeacherPictureActivity : AppCompatActivity() {
    companion object {
        const val POSITION_INTENT_KEY = "position"
        const val PICTURES_INTENT_KEY = "previews"
        const val ALBUM_INTENT_KEY = "album"
    }

    private var nowPosition = 0
    private lateinit var pictures: Array<Picture>
    private lateinit var album: Album
    private var isFaceMode = BehaviorSubject.create<Boolean>().apply { onNext(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_picture)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.picture_activity_toolbar_color)
            window.statusBarColor = ContextCompat.getColor(this, R.color.picture_activity_toolbar_color)
        }

        nowPosition = intent.getIntExtra(POSITION_INTENT_KEY, 0)
        pictures = intent.getParcelableArrayExtra(PICTURES_INTENT_KEY).map { it as Picture }.toTypedArray()
        album = intent.getParcelableExtra(ALBUM_INTENT_KEY)

        face_btn.setOnClickListener { faceBtnClicked() }
        total_page_label.text = "${pictures.size}"

        view_pager.adapter = PicturePagerAdapter(pictures, isFaceMode)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                now_page_label.text = (position + 1).toString()

                if (album.status == AlbumStatus.checking)
                    share_confirm_button.visibility = if (position == pictures.size - 1) View.VISIBLE else View.INVISIBLE
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        share_confirm_button.setOnClickListener {
            LoadingDialogHelper.show(this)

            ServerClient.albumModify(album.albumId, status=AlbumStatus.done) { errMsg ->
                if (errMsg != null) {
                    CrashUtil.onServerError(errMsg, this)
                    return@albumModify
                }

                ServerClient.albumDetail(album.albumId) { newAlbum, errMsg ->
                    if (newAlbum != null) {
                        EventBus.main.post(AlbumModifyEvent(newAlbum))
                    }
                }

                finish()
                LoadingDialogHelper.dismiss()
            }
        }

        view_pager.currentItem = nowPosition
        now_page_label.text = (nowPosition + 1).toString()
    }

    private fun faceBtnClicked() {
        val isFace = isFaceMode.value?.not() ?: return

        isFaceMode.onNext(isFace)
        face_btn.setImageResource(if (isFace) R.drawable.ic_face_green_24dp else R.drawable.ic_face_white_24dp)
    }
}