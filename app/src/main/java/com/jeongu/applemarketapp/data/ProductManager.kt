package com.jeongu.applemarketapp.data

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
                R.string.data_product_01_favorite_count,
                R.string.data_product_01_comment_count
            ),
            ProductInfo(
                2,
                R.drawable.img_product_02,
                R.string.data_product_02_title,
                R.string.data_product_02_introduction,
                R.string.data_product_02_seller_name,
                R.string.data_product_02_price,
                R.string.data_product_02_trading_place,
                R.string.data_product_02_favorite_count,
                R.string.data_product_02_comment_count
            ),
            ProductInfo(
                3,
                R.drawable.img_product_03,
                R.string.data_product_03_title,
                R.string.data_product_03_introduction,
                R.string.data_product_03_seller_name,
                R.string.data_product_03_price,
                R.string.data_product_03_trading_place,
                R.string.data_product_03_favorite_count,
                R.string.data_product_03_comment_count
            ),
            ProductInfo(
                4,
                R.drawable.img_product_04,
                R.string.data_product_04_title,
                R.string.data_product_04_introduction,
                R.string.data_product_04_seller_name,
                R.string.data_product_04_price,
                R.string.data_product_04_trading_place,
                R.string.data_product_04_favorite_count,
                R.string.data_product_04_comment_count
            ),
            ProductInfo(
                5,
                R.drawable.img_product_05,
                R.string.data_product_05_title,
                R.string.data_product_05_introduction,
                R.string.data_product_05_seller_name,
                R.string.data_product_05_price,
                R.string.data_product_05_trading_place,
                R.string.data_product_05_favorite_count,
                R.string.data_product_05_comment_count
            ),
            ProductInfo(
                6,
                R.drawable.img_product_06,
                R.string.data_product_06_title,
                R.string.data_product_06_introduction,
                R.string.data_product_06_seller_name,
                R.string.data_product_06_price,
                R.string.data_product_06_trading_place,
                R.string.data_product_06_favorite_count,
                R.string.data_product_06_comment_count
            ),
            ProductInfo(
                7,
                R.drawable.img_product_07,
                R.string.data_product_07_title,
                R.string.data_product_07_introduction,
                R.string.data_product_07_seller_name,
                R.string.data_product_07_price,
                R.string.data_product_07_trading_place,
                R.string.data_product_07_favorite_count,
                R.string.data_product_07_comment_count
            ),
            ProductInfo(
                8,
                R.drawable.img_product_08,
                R.string.data_product_08_introduction,
                R.string.data_product_08_title,
                R.string.data_product_08_seller_name,
                R.string.data_product_08_price,
                R.string.data_product_08_trading_place,
                R.string.data_product_08_favorite_count,
                R.string.data_product_08_comment_count
            ),
            ProductInfo(
                9,
                R.drawable.img_product_09,
                R.string.data_product_09_title,
                R.string.data_product_09_introduction,
                R.string.data_product_09_seller_name,
                R.string.data_product_09_price,
                R.string.data_product_09_trading_place,
                R.string.data_product_09_favorite_count,
                R.string.data_product_09_comment_count
            ),
            ProductInfo(
                10,
                R.drawable.img_product_10,
                R.string.data_product_10_title,
                R.string.data_product_10_introduction,
                R.string.data_product_10_seller_name,
                R.string.data_product_10_price,
                R.string.data_product_10_trading_place,
                R.string.data_product_10_favorite_count,
                R.string.data_product_10_comment_count
            ),
        )
    }

    fun getProductList(): MutableList<ProductInfo> {
        return productInfoList
    }
}