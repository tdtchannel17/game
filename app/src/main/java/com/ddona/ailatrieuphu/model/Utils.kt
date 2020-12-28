package com.ddona.ailatrieuphu.model

import android.widget.TextView
import androidx.databinding.BindingAdapter

object Utils {
    @JvmStatic
    @BindingAdapter("setText")
    fun setText(tv: TextView, content: String?) {
        tv.setText(content)
    }
}