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
        if (product.id != -1) {
            setProductInfo()
            setSellerMannerGradeInfo()
            initToolbar()
            initLikeImageView()
            setOnBackPressedHandler()
            Log.d("ProductDetailActivity", "product: $product")
        } else {
            finish()
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

    private fun setSellerMannerGradeInfo() {
        val randomTemperature = (0..1000).random() / 10.0
        val (colorResId, imageResId) = getResIdByTemperature(randomTemperature)
        with(binding) {
            tvMannerTemperature.apply {
                text = getString(R.string.format_manner_temperature, randomTemperature)
                setTextColor(getColor(colorResId))
            }
            ivMannerGradeIcon.setImageResource(imageResId)
        }
    }

    private fun getResIdByTemperature(randomTemperature: Double): Pair<Int, Int> {
        val temperature = (randomTemperature * 10).toInt()
        val pair = when (temperature) {
            in 0..100 -> Pair(R.color.gray_500, R.drawable.ic_grade_01)
            in 101..250 -> Pair(R.color.blue_700, R.drawable.ic_grade_02)
            in 251..370 -> Pair(R.color.blue_300, R.drawable.ic_grade_03)
            in 371..500 -> Pair(R.color.green, R.drawable.ic_grade_04)
            in 501..650 -> Pair(R.color.yellow, R.drawable.ic_grade_05)
            else -> Pair(R.color.carrot, R.drawable.ic_grade_06)
        }
        return pair
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
        val updatedProduct = ProductManager.updateLike(product.id, binding.ivProductDetailLikeIcon.isChecked)
        showSnackBar(updatedProduct, binding.ivProductDetailLikeIcon.isChecked)
    }

    private fun showSnackBar(updatedProduct: ProductInfo?, isLikeImageChecked: Boolean) {
        updatedProduct?.let {
            val message = when {
                isLikeImageChecked -> getString(R.string.message_add_interest_list)
                !isLikeImageChecked -> getString(R.string.message_remove_interest_list)
                else -> getString(R.string.message_unknown_error)
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
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

    private fun setOnBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHome()
            }
        })
    }

    companion object {
        const val EXTRA_PRODUCT = "product"
    }
}