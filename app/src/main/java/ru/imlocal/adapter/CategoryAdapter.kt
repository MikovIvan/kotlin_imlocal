package ru.imlocal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_category.view.*
import ru.imlocal.R

class CategoryAdapter(
    category: String,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    var data: List<String> = when (category) {
        "place", "action" -> listOf("Еда", "Дети", "Спорт", "Красота", "Покупки", "Все")
        else -> listOf("Еда", "Дети", "Спорт", "Город", "Театр", "Шоу", "Ярмарка", "Творчество", "Все")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(data, position, listener)
    }

    class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: List<String>, position: Int, listener: (String) -> Unit) {
            val category: String = data.get(position)
            itemView.tv_category.text = category

            itemView.setOnClickListener {
                listener(category)
            }
        }
    }
}


