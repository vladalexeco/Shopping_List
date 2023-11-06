package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.model.ShopItem
import com.example.shoppinglist.domain.usecase.DeleteShopItemUseCase
import com.example.shoppinglist.domain.usecase.EditShopItemUseCase
import com.example.shoppinglist.domain.usecase.GetShopListUseCase

class MainViewModel : ViewModel() {

    private val shopListRepository = ShopListRepositoryImpl()

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)

    private var _shopListLiveData = getShopListUseCase()
    val shopListLiveData: LiveData<List<ShopItem>> = _shopListLiveData

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase(newItem)
    }
}