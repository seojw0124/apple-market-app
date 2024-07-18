package com.jeongu.applemarketapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.jeongu.applemarketapp.R
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.data.ProductManager
import com.jeongu.applemarketapp.databinding.ActivityProductDetailBinding
import com.jeongu.applemarketapp.ui.extensions.applyNumberFormat

class ProductDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProductDetailBinding.inflate(layoutInflater)
    }

    private val defaultProduct = ProductInfo(-1, 0, 0, 0, 0, 0, 0, 0, 0, false)
    private val product: ProductInfo by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PRODUCT, ProductInfo::class.java) ?: defaultProduct
        } else {
            intent.getParcelableExtra(EXTRA_PRODUCT) ?: defaultProduct
        }
    }

    private var isLikeUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setLayout()
    }

    private fun setLayout() {
        if (product.id == -1) {
            finish()
        } else {
            setProductInfo()
            initToolbar()
            initLikeImageView()
            setOnBackPressedHandler()
        }
    }

    private fun setProductInfo() {
        with(binding) {
            ivProductDetailImage.setImageResource(product.image)
            tvSellerName.setText(product.sellerName)
            tvProductDetailTradingPlace.setText(product.tradingPlace)
            tvProductDetailTitle.setText(product.title)
            tvProductDetailIntroduction.setText(product.introduction)
            tvProductDetailPrice.applyNumberFormat(getString(product.price).toInt())
            ivProductDetailLikeIcon.isChecked = product.isLiked
        }
    }

    private fun initToolbar() {
        binding.ivToolbarBackIcon.setOnClickListener { navigateToHome() }
    }

    private fun initLikeImageView() {
        with(binding) {
            ivProductDetailLikeIcon.setOnClickListener { handleLikeIconClick() }
        }
    }

    private fun handleLikeIconClick() {
        val isUpdated = ProductManager.updateLike(product.id, binding.ivProductDetailLikeIcon.isChecked)
        showSnackBar(isUpdated, binding.ivProductDetailLikeIcon.isChecked)
    }

    private fun showSnackBar(isUpdated: Boolean, isLikeImageChecked: Boolean) {
        val message = when {
            isUpdated && isLikeImageChecked -> getString(R.string.message_add_interest_list)
            isUpdated && !isLikeImageChecked -> getString(R.string.message_remove_interest_list)
            else -> getString(R.string.message_unknown_error)
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setOnBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHome()
            }
        })
    }

    private fun navigateToHome() {
        isLikeUpdated = product.isLiked != binding.ivProductDetailLikeIcon.isChecked
        if (isLikeUpdated) {
            val intent = Intent(this@ProductDetailActivity, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_BOOLEAN_LIKE_UPDATE, isLikeUpdated)
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    companion object {
        const val EXTRA_PRODUCT = "product"
    }
}