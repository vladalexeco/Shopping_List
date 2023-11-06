package com.example.shoppinglist.domain.usecase

import com.example.shoppinglist.domain.api.ShopListRepository
import com.example.shoppinglist.domain.model.ShopItem

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    operator fun invoke(id: Int): ShopItem {
        return shopListRepository.getShopItemById(id)
    }
}