package com.example.sih.studentList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sih.database.StudentDatabaseDao
import com.example.sih.session.Student
import kotlinx.coroutines.*

class StudentListViewModel(
    private val database: StudentDatabaseDao,
    application: Application
): AndroidViewModel(application){

    private val _students = MutableLiveData<ArrayList<Student>>()
    val students : LiveData<ArrayList<Student>>
        get() = _students
    private val _student = MutableLiveData<String>()
    val student : LiveData<String>
        get() = _student
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val t = database.getIds()
                val keys = mutableSetOf<String>()

                for(item in t){
                    keys.add(item.id.trim().split(",").last())
                }

                val temp = mutableListOf<Student>()
                for(k in keys){
                    temp.add(Student(k))
                }

                _students.postValue(temp as ArrayList<Student>)

            }
        }
    }

    fun onStudentClicked(student : Student){
        _student.value = student.name
    }

    fun onDoneNavigation(student: Student){
        _student.value= null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}