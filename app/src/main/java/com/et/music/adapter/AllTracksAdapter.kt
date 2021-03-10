package com.et.music.adapter

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.et.ItemClickListener
import com.et.music.R
import com.et.music.ui.model.SongResponseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.android.inject
import java.lang.reflect.Type


class AllTracksAdapter : RecyclerView.Adapter<AllTracksAdapter.ViewHolder>() {

    var playlists: MutableList<SongResponseItem> = mutableListOf()
    var itemClickListener: ItemClickListener<SongResponseItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(
            R.layout.playlist_item,
            parent,
            false
        )
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateDataSet(newList: List<SongResponseItem>) {
            playlists = newList.toMutableList()
            notifyDataSetChanged()
    }

    private fun getItem(position: Int) = playlists[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    private val PREFERENCES_FILE_KEY = "com.example.settings_preferences"

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        val albumImageView = view.findViewById<ImageView>(R.id.albumId)
        val albumName = view.findViewById<TextView>(R.id.name)
        val albumSubtitle= view.findViewById<TextView>(R.id.subtitle)
        val itemAdd= view.findViewById<ImageButton>(R.id.item_add)
        val container= view.findViewById<LinearLayout>(R.id.container)

        fun bind(playlist: SongResponseItem) {
            val preferenceMgr = view.context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            Glide.with(view.context).load(playlist.image_url).into(albumImageView)
            albumName.text = playlist.title
            albumSubtitle.text = playlist.genre
            if (checkItemExists(playlist, preferenceMgr)) {
                itemAdd.setImageDrawable(view.context.getDrawable(R.drawable.ic_remove))
            } else
                itemAdd.setImageDrawable(view.context.getDrawable(R.drawable.ic_add))

            itemAdd.setOnClickListener {

                val gson = Gson()
                val type: Type = object : TypeToken<ArrayList<SongResponseItem?>?>() {}.type
                var list = arrayListOf(playlist)
                if (!preferenceMgr.getString("playlist_arr", null).isNullOrEmpty()) {
                    list = gson.fromJson(preferenceMgr.getString("playlist_arr", null), type)
                    if (list.contains(playlist)){
                        list.remove(playlist)
                        itemAdd.setImageDrawable(view.context.getDrawable(R.drawable.ic_add))
                    }else {
                        list.add(playlist)
                        itemAdd.setImageDrawable(view.context.getDrawable(R.drawable.ic_remove))
                    }
                } else {
                    list.add(playlist)
                    itemAdd.setImageDrawable(view.context.getDrawable(R.drawable.ic_remove))
                }
                var json: String? = Gson().toJson(list, type)
                preferenceMgr.edit().putString("playlist_arr", json).apply()
            }
            container.setOnClickListener{
                itemClickListener!!.onItemClick(
                    view,
                    adapterPosition,
                    getItem(adapterPosition)
                )
            }


        }

        private fun checkItemExists(item: SongResponseItem, preferences: SharedPreferences): Boolean {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<SongResponseItem?>?>() {}.type
            if (!preferences.getString("playlist_arr", null).isNullOrEmpty()) {
                val list: ArrayList<SongResponseItem> =
                    gson.fromJson(preferences.getString("playlist_arr", null), type)
                return list.contains(item)
            }
            return false
        }

        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.container -> if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(
                        view,
                        layoutPosition,
                        getItem(layoutPosition)
                    )
                }
            }
        }
    }
}