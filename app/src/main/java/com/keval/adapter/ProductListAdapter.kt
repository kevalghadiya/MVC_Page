package com.keval.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keval.model.GetProductRes
import com.keval.mvc.databinding.AdapterProductListBinding
import android.content.Context
import android.view.View

class ProductListAdapter(
    private val context: Context,
    private val data: ArrayList<GetProductRes.Product>,
    private val onClick: OnClick
) : RecyclerView.Adapter<ProductListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = AdapterProductListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    inner class ItemViewHolder(itemView: View, private val binding: AdapterProductListBinding) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: GetProductRes.Product) {
            binding.tvText.text = item.id.toString()
            itemView.setOnClickListener {
                onClick.onClick(item.title)
            }
        }
    }

    interface OnClick {
        fun onClick(catId: String)
    }
}
