package com.et.music.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.et.music.R
import com.et.music.ui.model.SongResponseItem
import com.et.music.ui.viewmodel.FragmentsViewmodel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_song_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class SongDetailFragment : Fragment() {
    private val preferences: SharedPreferences by inject()
    private val viewmodel: FragmentsViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_song_detail, container, false)
    }

    var responseItem: SongResponseItem? = null
    lateinit var timer: CountDownTimer
    var millis: Long = 0
    var isPaused = false
    lateinit var playList: ArrayList<SongResponseItem>
    var current = 0

    private fun getPlayListFromPreferences(context: Context): ArrayList<SongResponseItem> {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<SongResponseItem?>?>() {}.type
        return gson.fromJson(preferences.getString("playlist_arr", null), type)
    }

    override fun onAttach(context: Context) {
        playList = getPlayListFromPreferences(context)
        responseItem = arguments?.getParcelable("ata")
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewComponents()
        back.setOnClickListener{
            activity?.onBackPressed()
        }
        playList.remove(responseItem)
        setData(responseItem)
        playList.shuffle(Random())
        playList.add(0, responseItem!!)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setData(item: SongResponseItem?) {
        seek_bar.chunksCount.apply {
            responseItem?.duration!!
        }
        song_title.text = item?.title
        description.text = item?.genre
        Glide.with(view!!).load(item?.image_url).into(now_playing_cover)
        millis = (item?.duration!!*1000).toLong()
        instantiateTimer(millis, item)
    }

    private fun instantiateTimer(duration: Long, item: SongResponseItem) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                millis = millisUntilFinished
                Log.println(
                    Log.VERBOSE,
                    "milliLeft",
                    item?.duration!!.times(1000).minus(millis).toString()
                )
                // time_left.text = formatMilliseconds(responseItem?.duration!!*1000.minus(millis))
                time_left?.text = formatMilliseconds(item?.duration!!.times(1000).toLong())
                current_time?.text =
                    formatMilliseconds(item?.duration!!.times(1000).minus(millis))
                seek_bar?.progress = (item?.duration!!.minus(millis / 1000)).toFloat()
            }

            override fun onFinish() {

            }
        }
        timer.start()
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

    private fun initSongDetailView() {

    }





    private fun initViewComponents() {
        time_left.text = formatMilliseconds(responseItem?.duration!! * 1000.minus(millis).toLong())
        play_btn.setOnClickListener {
            if (!isPaused) {
                play_btn.setImageResource(R.drawable.ic_play)
                isPaused = true
                timer.cancel()
            } else {
                play_btn.setImageResource(R.drawable.ic_pause_notification)
                isPaused = false
                timer.cancel()
                instantiateTimer(millis, playList[current])
            }
        }

        previous_btn.setOnClickListener {
            timer.cancel()
            if (current != 0) {
                current--
                setData(playList[current])
            }
            else
                Toast.makeText(view?.context, "Disabled", Toast.LENGTH_SHORT).show()
        }

        next_btn.setOnClickListener {
            timer.cancel()
            if (current+1 < playList.size) {
                isPaused = false
                play_btn.setImageResource(R.drawable.ic_pause_notification)
                current++
                setData(playList[current])
            }
            else {
                AlertDialog.Builder(view?.context!!)
                    .setTitle("All songs are already played. Do you want to repeat?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        current = 0
                        setData(playList[current])
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .create().show()
            }

        }

        initSongDetailView()
    }
}
