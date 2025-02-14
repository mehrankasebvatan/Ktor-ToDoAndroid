package ir.kasebvatan.ktor_todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kasebvatan.ktor_todo.databinding.ItemMainListBinding
import ir.kasebvatan.ktor_todo.model.TaskModel

class MainAdapter(
    private val list: List<TaskModel>,
    private val callback: (String, TaskModel) -> Unit
) : RecyclerView.Adapter<MainAdapter.VH>() {
    class VH(val binding: ItemMainListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            ItemMainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = list[position]
        holder.binding.apply {
            txtTitle.text = data.title
            txtContent.text = data.content
            txtDate.text = data.lastModifiedDate

            imgDelete.setOnClickListener {
                callback.invoke("delete", data)
            }

            imgEdit.setOnClickListener {
                callback.invoke("edit", data)
            }


        }
    }


}