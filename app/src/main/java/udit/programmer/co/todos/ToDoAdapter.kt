package udit.programmer.co.todos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter(val list: ArrayList<ToDoModel>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(toDoModel: ToDoModel) {
            with(itemView) {
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[java.util.Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                txtShowTitle.setText(toDoModel.title)
                txtShowTask.setText(toDoModel.description)
                txtShowCategory.setText(toDoModel.category)
                upDateDate(toDoModel.date)
                upDateTime(toDoModel.time)
            }
        }

        private fun upDateDate(date: Long) {
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat)
            itemView.txtShowDate.text = sdf.format(Date(date))
        }

        private fun upDateTime(time: Long) {
            val myFormat = "h:mm a"
            val sdf = SimpleDateFormat(myFormat)
            itemView.txtShowDate.text = sdf.format(Date(time))
        }
    }
}