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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_all_tracks.*
import org.koin.android.ext.android.inject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class AllTracksFragment : Fragment(), ItemClickListener<SongResponseItem> {

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
        val list = Gson().fromJson(readLine("seed_data.txt")?.get(0), SongResponse::class.java)
        allTracksAdapter.updateDataSet(list)
        super.onViewCreated(view, savedInstanceState)
    }


    private fun readLine(path: String?): List<String>? {
        val mLines: MutableList<String> = ArrayList()
        val am: AssetManager? = activity?.assets
        try {
            val inputS = am?.open(path!!)
            val stringBuilder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputS))
            var string: String? = null
            while (true) {
                try {
                    if (reader.readLine().also { string = it } == null) break
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                stringBuilder.append(string).append("\n")
                mLines.add(string!!)
            }
            inputS?.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return mLines
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