package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.model.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button

    private var screenMode: String? = UNDEFINED_SCREEN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        parseIntent()

        initViews()

        when (screenMode) {
            MODE_ADD -> launchAddBlock()
            MODE_EDIT -> launchEditBlock()
        }
    }

    private fun launchEditBlock() {

        viewModel.getShopItemById(shopItemId)

        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text.toString(), etCount.text.toString())
        }

        viewModel.shopItem.observe(this) { shopItem ->
            if (shopItem.id != ShopItem.UNDEFINED_ID) {
                etName.setText(shopItem.name)
                etCount.setText(shopItem.count.toString())
            }
        }

        initErrorLiveData()
    }

    private fun launchAddBlock() {
        btnSave.setOnClickListener {
            viewModel.addShopItem(etName.text.toString(), etCount.text.toString())
        }

        initErrorLiveData()
    }

    private fun initErrorLiveData() {
        viewModel.errorInputName.observe(this) { errorInputName ->
            if (errorInputName) {
                tilName.error = "Wrong name input"
            }
        }

        viewModel.errorInputCount.observe(this) { errorInputCount ->
            if (errorInputCount) {
                tilCount.error = "Wrong count input"
            }
        }

        viewModel.shouldCloseScreen.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        etName = findViewById(R.id.et_name)
        tilCount = findViewById(R.id.til_count)
        etCount = findViewById(R.id.et_count)
        btnSave = findViewById(R.id.btn_save)
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Intent don't have screen mode")
        }

        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Screen mode not equal MODE_EDIT or MODE_ADD")
        }

        if (intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val UNDEFINED_SCREEN_MODE = ""
    }

}