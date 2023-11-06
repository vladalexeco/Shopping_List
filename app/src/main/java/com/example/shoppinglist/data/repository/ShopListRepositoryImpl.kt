package com.example.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.api.ShopListRepository
import com.example.shoppinglist.domain.model.ShopItem
import java.lang.RuntimeException

class ShopListRepositoryImpl: ShopListRepository {

    private val shopList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})

    private var _shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopListLD: LiveData<List<ShopItem>> = _shopListLD

    private var autoIncrement = 0

    init {
        for (i in 0..9) {
            val item = ShopItem(
                name = "Name $i",
                count = i,
                enabled = true,
            )
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrement++
        }
        shopList.add(shopItem)
        updateShopList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateShopList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItemById(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find { it.id == id } ?: throw RuntimeException("Element not found")
    }

    override fun getShopItemList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateShopList() {
        _shopListLD.value = shopList.toList()
    }
}