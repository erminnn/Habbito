package com.example.habbito.fragments

import android.app.backup.SharedPreferencesBackupHelper
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Timer
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.TimerViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 */
class Timer : Fragment() {
    private lateinit var chronometer: Chronometer
    enum class TimerState {Stopped,Running,Paused}
    var timerState = TimerState.Stopped
    private var pauseOffset : Long = 0
    private var base : Long = 0
    private var id : Long? = 0
    private lateinit var vm: TimerViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var v : View =  inflater.inflate(R.layout.fragment_timer, container, false)
        chronometer =  v.findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("00:%s")
        val bundle : Bundle = this.requireArguments()
        id = bundle.getLong("timer_id")
        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.timerDao
        val repository = TimerRepository(dao)
        val factory = TimerViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(TimerViewModel::class.java)


        var currentTimerState: String
        vm.getTimerById(id as Long).observe(this, Observer {
            pauseOffset = it.pauseOffset as Long
            currentTimerState = it.timerState
            if (currentTimerState == "Stopped") {
                stopTimer()
            } else if (currentTimerState == "Paused") {
                base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.base = it.startBase
                timerState = TimerState.Paused
                chronometer.setText(setTimeText(pauseOffset))
            } else {
                Log.d("FRAG","usaoooooooo runing")
                chronometer.base = it.startBase
                timerState = TimerState.Running
                startTimer()
            }
        })









        btnTimerStart.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()-pauseOffset
            startTimer()
        }
        btnTimerStop.setOnClickListener {
            stopTimer()
        }
        btnTimerPause.setOnClickListener {
            pauseTimer()
        }
    }


    fun  startTimer(){

        chronometer.start();
        timerState = TimerState.Running

    }

    fun pauseTimer(){
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            base = SystemClock.elapsedRealtime() - pauseOffset
            timerState = TimerState.Paused
            chronometer.setText(setTimeText(pauseOffset))
            chronometer.stop()


    }
    fun stopTimer(){
        base = SystemClock.elapsedRealtime()
        chronometer.base = base
        pauseOffset = 0
        timerState = TimerState.Stopped
        chronometer.stop()
    }

    fun setTimeText(milis : Long) : String {
        val time: Long = milis
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - h * 3600000 - m * 60000).toInt() / 1000
        val hh = if (h < 10) "0$h" else h.toString() + ""
        val mm = if (m < 10) "0$m" else m.toString() + ""
        val ss = if (s < 10) "0$s" else s.toString() + ""

        return hh+":"+mm+":"+ss
    }




    override fun onStop() {
        super.onStop()
        if(timerState == TimerState.Running) {
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
          vm.updateTimer(chronometer.base,pauseOffset,id as Long,timerState.toString())
        }else{
            vm.updateTimer(chronometer.base,pauseOffset,id as Long,timerState.toString())
        }
    }











}
