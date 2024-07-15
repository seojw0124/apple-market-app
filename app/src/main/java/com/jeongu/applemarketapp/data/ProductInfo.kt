package com.jeongu.applemarketapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInfo(
    val id: Int,
    val image: Int,
    val title: Int,
    val introduction: Int,
    val sellerName: Int,
    val price: Int,
    val tradingPlace: Int,
    val likeCount: Int,
    val commentCount: Int,
): Parcelable
