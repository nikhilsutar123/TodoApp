package com.example.mvvmtodo.ui.deleteAllCompleted

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvvmtodo.data.TaskDao
import com.example.mvvmtodo.di.AppModule.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class deleteAllCompletedViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
   @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteAllCompletedTasks()
    }
}