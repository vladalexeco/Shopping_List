package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.model.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    var onLongClickListener: ((ShopItem) -> Unit)? = null
    var onClickListener: ((ShopItem) -> Unit)? = null

    val shopList = mutableListOf<ShopItem>()

    fun setList(list: List<ShopItem>) {
        val diffCallBack = ShopListDiffCallback(shopList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        shopList.clear()
        shopList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when(viewType) {
            ITEM_VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            ITEM_VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown viewType number with value $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.count()
    }

    override fun getItemViewType(position: Int): Int {
        val currentShopItem = shopList[position]
        return if (currentShopItem.enabled) ITEM_VIEW_TYPE_ENABLED else ITEM_VIEW_TYPE_DISABLED
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(shopList[position])

        val item = shopList[position]

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(item)
            true
        }

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item   )
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_ENABLED = 1
        const val ITEM_VIEW_TYPE_DISABLED = 2
    }
}

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvName: TextView = itemView.findViewById(R.id.tv_name)
    private val tvCount: TextView = itemView.findViewById(R.id.tv_count)

    fun bind(shopItem: ShopItem) {
        tvName.text = shopItem.name
        tvCount.text = shopItem.count.toString()
    }
}