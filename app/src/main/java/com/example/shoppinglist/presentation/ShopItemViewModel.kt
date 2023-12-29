package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.repository.ShopListRepositoryImpl
import com.example.shoppinglist.domain.model.ShopItem
import com.example.shoppinglist.domain.usecase.AddShopItemUseCase
import com.example.shoppinglist.domain.usecase.EditShopItemUseCase
import com.example.shoppinglist.domain.usecase.GetShopItemByIdUseCase

class ShopItemViewModel : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    private val shopListRepository = ShopListRepositoryImpl.getInstance()

    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)
    private val addShopItemUseCase = AddShopItemUseCase(shopListRepository)

    fun getShopItemById(id: Int) {
        val shopItem =  getShopItemByIdUseCase(id)
        _shopItem.value = shopItem
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateInput(name, count)) {

            _shopItem.value?.let {
                val shopItem = it.copy(name = name, count = count)
                editShopItemUseCase(shopItem)
                finishWork()
            }
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateInput(name, count)) {
            val shopItem = ShopItem(
                name = name,
                count = count,
                enabled = true
            )
            addShopItemUseCase(shopItem)
        }

        finishWork()
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (exception: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var nameBooleanResult: Boolean? = null
        var countBooleanResult: Boolean? = null

        if (name.isBlank()) {
            _errorInputName.value = true
            nameBooleanResult = false
        } else {
            nameBooleanResult = true
        }

        if (count <= 0) {
            _errorInputCount.value = true
            countBooleanResult = false
        } else {
            countBooleanResult = true
        }

        return nameBooleanResult && countBooleanResult
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}