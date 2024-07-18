package com.jeongu.applemarketapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.databinding.ItemProductBinding
import com.jeongu.applemarketapp.ui.extensions.applyNumberFormat

class ProductListAdapter(
    private val onClick: (ProductInfo) -> Unit,
    private val onLongClick: (ProductInfo) -> Unit,
    private val listener: ProductItemClickListener
) : ListAdapter<ProductInfo, ProductListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    private fun updateLikeCount(isLikeImageChecked: Boolean, tvLikeCount: TextView) {
        val likeCount = tvLikeCount.text.toString().toInt()
        tvLikeCount.text = if (isLikeImageChecked) {
            (likeCount + 1).toString()
        } else {
            (likeCount - 1).toString()
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductInfo, listener: ProductItemClickListener) {
            itemView.setOnClickListener {
                onClick(product)
            }
            itemView.setOnLongClickListener {
                onLongClick(product)
                true
            }
            val context = itemView.context
            with(binding) {
                ivProduct.setImageResource(product.image)
                tvProductTitle.text = context.getString(product.title)
                tvProductTradingPlace.text = context.getString(product.tradingPlace)
                tvProductPrice.applyNumberFormat(context.getString(product.price).toInt())
                tvCommentCount.text = product.commentCount.toString()
                tvLikeCount.text = product.likeCount.toString()
                ivLikeIcon.apply {
                    isChecked = product.isLiked
                    setOnClickListener {
                        listener.onLikeClick(product.id, isChecked)
                        updateLikeCount(isChecked, tvLikeCount)
                    }
                }
            }
        }
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<ProductInfo>() {
    override fun areItemsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
        return oldItem == newItem
    }
}