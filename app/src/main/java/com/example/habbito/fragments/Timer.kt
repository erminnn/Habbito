package com.example.habbito.fragments

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.example.habbito.R
import kotlinx.android.synthetic.main.fragment_timer.*


/**
 * A simple [Fragment] subclass.
 */
class Timer : Fragment() {
    private lateinit var chronometer: Chronometer
    enum class TimerState {Stopped,Running,Paused}
    private var timerState = TimerState.Stopped
    private var pauseOffset : Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var v : View =  inflater.inflate(R.layout.fragment_timer, container, false)
        chronometer =  v.findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("00:%s")
        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnTimerStart.setOnClickListener {
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
        if(timerState != TimerState.Running){
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start();
            timerState = TimerState.Running
        }
    }

    fun pauseTimer(){
        if(timerState == TimerState.Running){
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            timerState = TimerState.Paused
        }
    }
    fun stopTimer(){
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        timerState = TimerState.Stopped
        chronometer.stop()
    }

}
