package com.jeongu.applemarketapp.ui.extensions

import android.icu.text.DecimalFormat
import android.widget.TextView
import com.jeongu.applemarketapp.R

fun TextView.applyNumberFormat(price: Int) {
    text = context.getString(R.string.format_price_unit, price.convertThreeDigitComma())
}

fun Number.convertThreeDigitComma(): String { // 수신 객체 Number -> Int, Long, Double, Float 모두 사용 가능
    val decimalFormat = DecimalFormat("#,###")
    return decimalFormat.format(this)
}
