package udit.programmer.co.todos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Clock
import java.util.*

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalender: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            DB_NAME
        )
    }
    val list = arrayOf("Bussiness", "Personal", "Insurance", "Shooping", "Banking")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        date_et.setOnClickListener(this)
        time_et.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val spin_adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list)
        list.sort()
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
        date_et.setText(sdf.format(myCalender.time))

        time_input.visibility = View.VISIBLE
    }

}
