package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemColorBinding

enum class TextColor(
    val hexString: String
) {
    BLUE("#3F51B5"),
    GREEN("#4CAF50"),
    RED("#E91E63"),
    YELLOW("#FFEB3B")
}

class ColorItemRecyclerView(
    private val colors: List<TextColor>,
    private val onItemClick: (TextColor) -> Unit
): RecyclerView.Adapter<ColorItemRecyclerView.ViewHolder>() {

    class ViewHolder(binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = colors[position]
        val colorInt = item.hexString.toColorInt()
        holder.itemView.setBackgroundColor(colorInt)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

}