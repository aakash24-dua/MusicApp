package com.et.music.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.et.music.R
import com.et.music.extensions.gone
import com.et.music.extensions.setVisible
import com.et.music.extensions.visible
import com.et.music.ui.fragments.AllTracksFragment
import com.et.music.ui.fragments.SongDetailFragment
import com.et.music.ui.model.SongResponseItem
import kotlinx.android.synthetic.main.activity_main.*
interface OnSongClick{
    fun openSongDetailPage( songResponseItem: SongResponseItem)
}

class MainActivity : AppCompatActivity(),OnSongClick {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavMenu()
    }
    override fun openSongDetailPage(songResponseItem: SongResponseItem){
        val bundle = bundleOf("ata" to songResponseItem)
        navController.navigate(R.id.action_global_songDetail,bundle)
    }

    private fun setupBottomNavMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        navHostFragment?.let {
            navController = it.navController
            NavigationUI.setupWithNavController(
                bottomNavView,
                it.navController
            )

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavView?.setVisible(destination.id != R.id.songDetailFragmentt)

                title = when (destination.id) {
                    R.id.allTracksFragment -> {
                        "All Tracks"
                    }
                    R.id.playListFragment -> {
                        "PlayList"
                    }
                    else -> {
                        "Now Playing"
                    }
                }
            }
        }
    }
}