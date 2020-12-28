package com.ddona.ailatrieuphu.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.ddona.ailatrieuphu.databinding.DialogCallBinding
import com.ddona.ailatrieuphu.databinding.DialogConfirmBinding

class ConfirmDialog : Dialog {
    private lateinit var binding: DialogConfirmBinding
    var setBtn: Boolean = true

    constructor(context: Context) : super(context) {
        inits()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        inits()
    }

    private fun inits() {
        binding = DialogConfirmBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        binding.btnYes.setOnClickListener {
            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
        }
        binding.btnNo.setOnClickListener {
            dismiss()
        }
    }
}