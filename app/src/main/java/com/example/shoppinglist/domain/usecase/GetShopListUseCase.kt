package com.example.shoppinglist.domain.usecase

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.api.ShopListRepository
import com.example.shoppinglist.domain.model.ShopItem

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {
    operator fun invoke(): LiveData<List<ShopItem>> {
        return shopListRepository.getShopItemList()
    }
}