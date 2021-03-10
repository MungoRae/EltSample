package uk.me.mungorae.eltinterview.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.me.mungorae.eltinterview.api.Task

class ListAdapter(
    private val inflater: LayoutInflater
) : RecyclerView.Adapter<TaskViewHolder>() {

    private val items = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

    fun setTasks(tasks: List<Task>) {
        items.clear()
        items.addAll(tasks)
        notifyDataSetChanged()
    }
}