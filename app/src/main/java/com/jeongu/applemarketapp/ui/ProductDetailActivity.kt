package com.jeongu.applemarketapp.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeongu.applemarketapp.R
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.databinding.ActivityProductDetailBinding
import com.jeongu.applemarketapp.ui.extensions.applyNumberFormat

class ProductDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProductDetailBinding.inflate(layoutInflater)
    }
    private lateinit var product: ProductInfo

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
        getProduct()
    }

    private fun getProduct() {
        // 상품 정보를 불러와서 화면에 표시
        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PRODUCT, ProductInfo::class.java) ?: ProductInfo(-1, 0, 0, 0, 0, 0, 0, 0, 0)
        } else {
            intent.getParcelableExtra(EXTRA_PRODUCT) ?: ProductInfo(-1, 0, 0, 0, 0, 0, 0, 0, 0)
        }

        if (product.id != -1) {
            setProductInfo()
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
        }
    }
}