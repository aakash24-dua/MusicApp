package com.et.music.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.et.ItemClickListener
import com.et.music.R
import com.et.music.adapter.PlaylistAdapter
import com.et.music.ui.OnSongClick
import com.et.music.ui.model.SongResponseItem
import com.et.music.ui.viewmodel.FragmentsViewmodel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_all_tracks.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Type


class PlayListFragment : Fragment(), ItemClickListener<SongResponseItem> {

    private lateinit var playlistAdapter: PlaylistAdapter
    var onSongClick : OnSongClick? = null
    private val viewmodel: FragmentsViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_tracks, container, false)
    }

    override fun onAttach(context: Context) {
        onSongClick = activity as OnSongClick
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.liveData.observe(this, Observer {
            playlistAdapter.updateDataSet(it)
        })
        init()
        viewmodel.getPlayListData()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun init() {
        playlistAdapter = PlaylistAdapter().apply {
            itemClickListener = this@PlayListFragment
        }
        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }
    }

    override fun onItemClick(view: View, position: Int, item: SongResponseItem) {
        onSongClick?.openSongDetailPage(item)
    }


}