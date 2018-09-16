package com.kidsharu.kidsharu.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import com.kidsharu.kidsharu.R
import com.kidsharu.kidsharu.other.ActivityUtil
import com.kidsharu.kidsharu.other.ServerClient
import com.kidsharu.kidsharu.recyclerView.AlbumRecyclerAdapter
import com.kidsharu.kidsharu.recyclerView.ChildRecyclerAdapter
import kotlinx.android.synthetic.main.activity_parent_home.*
import kotlinx.android.synthetic.main.toolbar.*

class ParentHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_home)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        ActivityUtil.setStatusBarColor(this, Color.WHITE, true)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.icon_hamberger)
        toggle.setToolbarNavigationClickListener {
            if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        toggle.syncState()

        // TODO pull to refresh
        ServerClient.parentAlbumList { albums, errMsg ->
            errMsg?.let { println(it) }
            album_recycler_view.adapter = AlbumRecyclerAdapter(albums)
        }

        child_recycler_view.adapter = ChildRecyclerAdapter(emptyArray())
        child_recycler_view.layoutManager = LinearLayoutManager(this)

        ServerClient.parentChildList { children, errMsg ->
            errMsg?.let { println(it) }
            child_recycler_view.adapter = ChildRecyclerAdapter(children).apply {
                itemTouchCallback = { _ -> drawer_layout.closeDrawer(GravityCompat.START) }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
