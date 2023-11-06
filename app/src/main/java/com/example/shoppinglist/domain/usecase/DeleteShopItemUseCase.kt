package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.domain.api.ShopListRepository
import com.example.shoppinglist.domain.model.ShopItem

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    operator fun invoke(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)
    }
}