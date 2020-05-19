package udit.programmer.co.todos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ToDoDao {

    @Insert
    suspend fun insertTask(toDoModel: ToDoModel)

    @Query("SELECT * FROM ToDoModel WHERE isFinished != -1")
    fun getTask(): LiveData<List<ToDoModel>>

    @Query("UPDATE ToDoModel SET isFinished = 1 WHERE id = :uid")
    fun finishTask(uid: Long)

    @Query("DELETE FROM ToDoModel WHERE id = :uid")
    fun deleteTask(uid: Long)

}