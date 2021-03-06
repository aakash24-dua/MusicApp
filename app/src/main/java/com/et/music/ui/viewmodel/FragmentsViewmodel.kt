package com.et.music.ui.viewmodel

import android.content.SharedPreferences
import android.graphics.Movie
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.et.music.ui.model.SongResponseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

class FragmentsViewmodel(val preferences: SharedPreferences) : ViewModel() {


    val liveData = MutableLiveData<List<SongResponseItem>>()
    lateinit var playList: ArrayList<SongResponseItem>

     fun readLine(inputS:InputStream?): List<String>? {
        val mLines: MutableList<String> = ArrayList()
        try {
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

    fun getPlayListData() {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<SongResponseItem?>?>() {}.type
        var ss:List<SongResponseItem>
        ss = gson.fromJson(preferences.getString("playlist_arr", null), type)
        liveData.postValue(ss)
    }

    fun getPlayListFromPreferences(): ArrayList<SongResponseItem> {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<SongResponseItem?>?>() {}.type
        return gson.fromJson(preferences.getString("playlist_arr", null), type)
    }

    fun formatMilliseconds(duration: Long): String {
        val seconds = (duration / 1000).toInt() % 60
        val minutes = (duration / (1000 * 60) % 60).toInt()
        val hours = (duration / (1000 * 60 * 60) % 24).toInt()
        "${timeAddZeros(hours, false)}:${timeAddZeros(minutes)}:${timeAddZeros(seconds)}".apply {
            return if (startsWith(":")) replaceFirst(":", "") else this
        }
    }

    private fun timeAddZeros(number: Int?, showIfIsZero: Boolean = true): String {
        return when (number) {
            0 -> if (showIfIsZero) "0${number}" else ""
            1, 2, 3, 4, 5, 6, 7, 8, 9 -> "0${number}"
            else -> number.toString()
        }
    }

}