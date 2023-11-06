package com.example.shoppinglist.domain.api

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.model.ShopItem

interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItemById(id: Int): ShopItem

    fun getShopItemList(): LiveData<List<ShopItem>>
}