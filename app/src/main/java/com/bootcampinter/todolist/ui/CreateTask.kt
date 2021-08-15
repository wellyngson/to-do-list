package com.bootcampinter.todolist.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bootcampinter.todolist.databinding.ActivityCreateTaskBinding
import com.bootcampinter.todolist.datasource.TaskDataSource
import com.bootcampinter.todolist.extensions.format
import com.bootcampinter.todolist.extensions.text
import com.bootcampinter.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class CreateTask : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findByID(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                binding.tilDate.text = Date(it).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timerPicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H).build()
            timerPicker.addOnPositiveButtonClickListener {
                binding.tilHour.text = "${timerPicker.hour}:${timerPicker.minute}"
            }
            timerPicker.show(supportFragmentManager, null)
        }

        binding.btCreateTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )

            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}