package com.et.music.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.et.ItemClickListener
import com.et.music.R
import com.et.music.adapter.AllTracksAdapter
import com.et.music.ui.OnSongClick
import com.et.music.ui.model.SongResponse
import com.et.music.ui.model.SongResponseItem
import com.et.music.ui.viewmodel.FragmentsViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_all_tracks.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class AllTracksFragment : Fragment(), ItemClickListener<SongResponseItem> {
    private val viewmodel: FragmentsViewmodel by viewModel()

    private lateinit var allTracksAdapter: AllTracksAdapter
    var onSongClick: OnSongClick? = null

    override fun onAttach(context: Context) {
        onSongClick = activity as OnSongClick
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        val list = Gson().fromJson(readFile("seed_data.txt")?.get(0), SongResponse::class.java)
        allTracksAdapter.updateDataSet(list)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun readFile(path:String?): List<String>?{
        val am: AssetManager? = activity?.assets
        val inputS = am?.open(path!!)
        return viewmodel.readLine(inputS)
    }




    private fun init() {
        allTracksAdapter = AllTracksAdapter().apply {
            itemClickListener = this@AllTracksFragment
        }

        list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allTracksAdapter
        }
    }

    override fun onItemClick(view: View, position: Int, item: SongResponseItem) {
        onSongClick?.openSongDetailPage(item)
    }


}