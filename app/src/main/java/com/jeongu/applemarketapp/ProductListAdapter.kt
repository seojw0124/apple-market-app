package com.jeongu.applemarketapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.databinding.ItemProductBinding

class ProductListAdapter(
    private val list: MutableList<ProductInfo>,
    private val onClick: (ProductInfo) -> Unit,
    private val onLongClick: (ProductInfo) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        runCatching {
            list[position].run {
                holder.bind(this)
            }
        }.onFailure { exception ->
            Log.e("ProductListAdapter", "Exception! ${exception.message}")
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductInfo) {
            itemView.setOnClickListener {
                onClick(product)
            }
            itemView.setOnLongClickListener {
                onLongClick(product)
                true
            }
            with(binding) {
                ivProduct.setImageResource(product.image)
                tvProductTitle.text = itemView.context.getString(product.title)
                tvProductTradingPlace.text = itemView.context.getString(product.tradingPlace)
                tvProductPrice.text = itemView.context.getString(product.price)
                tvCommentCount.text = itemView.context.getString(product.commentCount)
                tvFavoriteCount.text = itemView.context.getString(product.favoriteCount)
            }
        }
    }
}