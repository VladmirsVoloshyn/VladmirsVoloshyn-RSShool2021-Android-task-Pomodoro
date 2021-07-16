package com.example.rsshool2021_android_task_pomodoro.ui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rsshool2021_android_task_pomodoro.R
import com.example.rsshool2021_android_task_pomodoro.custom.CustomProgressBar
import com.example.rsshool2021_android_task_pomodoro.model.Timer
import com.example.rsshool2021_android_task_pomodoro.model.dispatchers.TimerDispatcher

class TimerListAdapter(
    private val listener: OnTimerClickListener,
    private val timersList: ArrayList<Timer>,
    context: Context
) : RecyclerView.Adapter<TimerListAdapter.ViewHolder>(), Timer.OnTimeUpdate {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.timer_container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.customProgressBar.setPeriod(timersList[position].startTimeInMills)
        holder.customProgressBar.setCurrent(timersList[position].timeLeftInMills)
        timersList[position].listener = this
        holder.textView.text = timersList[position].updatableStringTimer

        if (timersList[position].isRunning) {
            holder.startOrStopButton.changeSelfText(STOP)
            holder.animationDrawable.start()
            holder.imageView.show()
            TimerDispatcher.setTimer(timersList[position])
        }
        if (!timersList[position].isRunning) {
            holder.startOrStopButton.changeSelfText(START)
            holder.animationDrawable.stop()
            holder.imageView.hide()
        }
        if (timersList[position].isFinished) {
            holder.childContainer.setBackgroundResource(R.drawable.timer_container_finished_bg)
            holder.imageView.setImageResource(R.drawable.animation_one_24)
            holder.customProgressBar.hide()
            holder.startOrStopButton.changeSelfText("")
            holder.startOrStopButton.isEnabled = false
        }
    }

    override fun onUpdate(time: String) {
        notifyDataSetChanged()
    }

    override fun getItemCount() = timersList.size


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var deleteButton: ImageButton = v.findViewById(R.id.deleteButton)
        val textView: TextView = v.findViewById(R.id.timerTextView)
        var startOrStopButton: Button = v.findViewById(R.id.startOrStopButton)
        var childContainer: ConstraintLayout = v.findViewById(R.id.childContainer)
        var imageView: ImageView = v.findViewById(R.id.animationView)
        var animationDrawable: AnimationDrawable
        var customProgressBar: CustomProgressBar = v.findViewById(R.id.itWillCustomView)

        init {
            v.setOnClickListener(this)
            imageView.setBackgroundResource(R.drawable.running_timer_animation)
            animationDrawable = imageView.background as AnimationDrawable
            startOrStopButton.setOnClickListener {
                if (timersList[adapterPosition].isRunning) {
                    timersList[adapterPosition].stopTimer()
                    animationDrawable.stop()
                    imageView.hide()
                    startOrStopButton.changeSelfText(START)
                } else {
                    if (!timersList[adapterPosition].isRunning) {
                        timersList[adapterPosition].startTimer()
                        startOrStopButton.changeSelfText(STOP)
                        imageView.show()
                        animationDrawable.start()
                        for (item in timersList) {
                            if (item != timersList[adapterPosition]) {
                                item.stopTimer()
                            }
                        }
                        notifyDataSetChanged()
                    }
                }

                listener.onStartOrStopClick()
            }
            deleteButton.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }

        override fun onClick(v: View?) {
        }
    }

    interface OnTimerClickListener {
        fun onStartOrStopClick()
        fun onDeleteClick(position: Int)
    }

    companion object {
        private const val START = "START"
        private const val STOP = "STOP"
        private const val RESTART = "RESTART"
    }


}