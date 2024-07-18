package com.jeongu.applemarketapp.data

import android.content.Context
import com.jeongu.applemarketapp.R

object ProductManager {

    private var productInfoList: MutableList<ProductInfo> = getDummyData()

    private fun getDummyData(): MutableList<ProductInfo> {
        return mutableListOf(
            ProductInfo(
                1,
                R.drawable.img_product_01,
                R.string.data_product_01_title,
                R.string.data_product_01_introduction,
                R.string.data_product_01_seller_name,
                R.string.data_product_01_price,
                R.string.data_product_01_trading_place,
                13,
                25,
                false
            ),
            ProductInfo(
                2,
                R.drawable.img_product_02,
                R.string.data_product_02_title,
                R.string.data_product_02_introduction,
                R.string.data_product_02_seller_name,
                R.string.data_product_02_price,
                R.string.data_product_02_trading_place,
                8,
                28,
                false
            ),
            ProductInfo(
                3,
                R.drawable.img_product_03,
                R.string.data_product_03_title,
                R.string.data_product_03_introduction,
                R.string.data_product_03_seller_name,
                R.string.data_product_03_price,
                R.string.data_product_03_trading_place,
                23,
                5,
                false
            ),
            ProductInfo(
                4,
                R.drawable.img_product_04,
                R.string.data_product_04_title,
                R.string.data_product_04_introduction,
                R.string.data_product_04_seller_name,
                R.string.data_product_04_price,
                R.string.data_product_04_trading_place,
                14,
                17,
                false
            ),
            ProductInfo(
                5,
                R.drawable.img_product_05,
                R.string.data_product_05_title,
                R.string.data_product_05_introduction,
                R.string.data_product_05_seller_name,
                R.string.data_product_05_price,
                R.string.data_product_05_trading_place,
                22,
                9,
                false
            ),
            ProductInfo(
                6,
                R.drawable.img_product_06,
                R.string.data_product_06_title,
                R.string.data_product_06_introduction,
                R.string.data_product_06_seller_name,
                R.string.data_product_06_price,
                R.string.data_product_06_trading_place,
                25,
                16,
                false
            ),
            ProductInfo(
                7,
                R.drawable.img_product_07,
                R.string.data_product_07_title,
                R.string.data_product_07_introduction,
                R.string.data_product_07_seller_name,
                R.string.data_product_07_price,
                R.string.data_product_07_trading_place,
                142,
                54,
                false
            ),
            ProductInfo(
                8,
                R.drawable.img_product_08,
                R.string.data_product_08_title,
                R.string.data_product_08_introduction,
                R.string.data_product_08_seller_name,
                R.string.data_product_08_price,
                R.string.data_product_08_trading_place,
                31,
                7,
                false
            ),
            ProductInfo(
                9,
                R.drawable.img_product_09,
                R.string.data_product_09_title,
                R.string.data_product_09_introduction,
                R.string.data_product_09_seller_name,
                R.string.data_product_09_price,
                R.string.data_product_09_trading_place,
                7,
                28,
                false
            ),
            ProductInfo(
                10,
                R.drawable.img_product_10,
                R.string.data_product_10_title,
                R.string.data_product_10_introduction,
                R.string.data_product_10_seller_name,
                R.string.data_product_10_price,
                R.string.data_product_10_trading_place,
                40,
               6,
                false
            ),
        )
    }

    fun getList(): MutableList<ProductInfo> = productInfoList

    fun removeItem(product: ProductInfo): Boolean {
        return productInfoList.remove(product)
    }

    fun updateLike(productId: Int, isLiked: Boolean): Boolean {
        val product = getProduct(productId) ?: return false
        val likeCount = if (isLiked) product.likeCount + 1 else product.likeCount - 1
        val newProduct = product.copy(isLiked = isLiked, likeCount = likeCount)
        productInfoList[productInfoList.indexOf(product)] = newProduct
        return true
    }

    private fun getProduct(productId: Int): ProductInfo? {
        return productInfoList.find { it.id == productId }
    }
}