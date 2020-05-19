package udit.programmer.co.todos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalender: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    val db by lazy {
        AppDatabase.getDataBase(this)
    }
    val categories = arrayOf("Bussiness", "Personal", "Insurance", "Shooping", "Banking")
    var finalDate = 0L
    var finalTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        date_et.setOnClickListener(this)
        time_et.setOnClickListener(this)
        save_btn.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val spin_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, categories)
        categories.sort()
        spinner.adapter = spin_adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.date_et -> {
                setDateListener()
            }
            R.id.time_et -> {
                setTimeListener()
            }
            R.id.save_btn -> {
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val title = title_input.editText?.text.toString()
        val category = spinner.selectedItem.toString()
        val description = task_input.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertTask(
                    ToDoModel(
                        title, description, category, finalDate, finalTime
                    )
                )
            }
            finish()
        }
    }

    private fun setTimeListener() {
        myCalender = Calendar.getInstance()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalender.set(Calendar.MINUTE, minute)
                UpdateTime()
            }
        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalender.get(Calendar.HOUR_OF_DAY),
            myCalender.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun UpdateTime() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalender.time.time
        time_et.setText(sdf.format(myCalender.time))
    }

    private fun setDateListener() {
        myCalender = Calendar.getInstance()
        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalender.set(Calendar.YEAR, year)
                myCalender.set(Calendar.MONTH, month)
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                UpdateDate()
            }
        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, myCalender.get(Calendar.YEAR),
            myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun UpdateDate() {
        val myFormat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalender.time.time
        date_et.setText(sdf.format(myCalender.time))

        time_input.visibility = View.VISIBLE
    }

}
