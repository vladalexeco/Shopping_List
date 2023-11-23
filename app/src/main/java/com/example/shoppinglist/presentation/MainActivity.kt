package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.model.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var adapter: ShopListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopListLiveData.observe(this) {
            adapter?.setList(it)
        }

    }

    private fun setUpRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_shop_list)

        adapter = ShopListAdapter()

        recyclerView.adapter = adapter

        val callback = simpleCallback()

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter!!.onLongClickListener = { shopItem ->
            viewModel.changeEnableState(shopItem)
        }

        adapter!!.onClickListener = {
            Log.d("Adapter", it.toString())
        }
    }

    private fun simpleCallback() = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = adapter?.shopList?.get(viewHolder.adapterPosition)
            if (item != null) {
                viewModel.deleteShopItem(item)
            }

        }
    }
}