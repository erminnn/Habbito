package com.example.habbito.fragments

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
import com.example.habbito.repository.ActivityRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_timer.*


/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {
    private lateinit var chronometer: Chronometer
    enum class TimerState {Stopped,Running,Paused}
    var timerState = TimerState.Stopped
    private var pauseOffset : Long = 0
    private var base : Long = 0
    private var id : Long? = 0
    private lateinit var vm: ActivityListViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v : View =  inflater.inflate(R.layout.fragment_timer, container, false)
        chronometer =  v.findViewById<Chronometer>(R.id.chronometer)
        chronometer.setFormat("00:%s")
        val bundle : Bundle = this.requireArguments()
        id = bundle.getLong("timer_id")
        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.activityDao
        val repository = ActivityRepository(dao)
        val factory = TimerViewModelFactory(repository, activity?.application!!)
        vm = ViewModelProvider(this, factory).get(ActivityListViewModel::class.java)
        var currentTimerState: String
        vm.getTimerById(id as Long).observe(viewLifecycleOwner, Observer {
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

    private fun stopTimer(){
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
        return "$hh:$mm:$ss"
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            TimerFragment().apply {
                arguments = Bundle().apply {
                    putLong("timer_id", param1)
                }
            }
    }

}
