package com.bootcampinter.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bootcampinter.todolist.databinding.ActivityMainBinding
import com.bootcampinter.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvRecyclerView.adapter = adapter
        updateList()
        insertListeners()
    }

    private fun insertListeners() {
        binding.fbFloatingButton.setOnClickListener {
            startActivityForResult(Intent(this, CreateTask::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, CreateTask::class.java)
            intent.putExtra(CreateTask.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        binding.emptyStateMain.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE

        binding.rvRecyclerView.visibility = if (list.isEmpty()) View.GONE
        else View.VISIBLE

        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}