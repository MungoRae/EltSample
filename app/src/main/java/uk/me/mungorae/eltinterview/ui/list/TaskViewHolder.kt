package uk.me.mungorae.eltinterview.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.me.mungorae.eltinterview.R
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.Type
import uk.me.mungorae.eltinterview.databinding.ListItemTaskBinding

class TaskViewHolder(
    inflater: LayoutInflater,
    container: ViewGroup,
    private val binding: ListItemTaskBinding = ListItemTaskBinding.inflate(inflater, container, false)
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(task: Task) {
        binding.listItemTaskName.text = task.name
        binding.listItemTaskDescription.text = task.description
        binding.listItemTaskIcon.setImageResource(
            when(task.type) {
                Type.GENERAL -> R.drawable.general
                Type.HYDRATION -> R.drawable.hydration
                Type.MEDICATION -> R.drawable.medication
                Type.NUTRITION -> R.drawable.nutrition
            }
        )
        binding.listItemTaskIcon.contentDescription = task.type.name
    }
}