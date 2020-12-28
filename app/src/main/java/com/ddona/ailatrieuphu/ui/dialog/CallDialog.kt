package com.ddona.ailatrieuphu.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import com.ddona.ailatrieuphu.databinding.DialogCallBinding

class CallDialog : Dialog, View.OnClickListener {
    private lateinit var binding: DialogCallBinding
    var setBtn: Boolean = true
    private var data = ""

    constructor(context: Context) : super(context) {
        inits()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        inits()
    }

    private fun inits() {
        binding = DialogCallBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        binding.btnConvinh.setOnClickListener(this)
        binding.btnMessi.setOnClickListener(this)
        binding.btnBigronaldo.setOnClickListener(this)
        binding.btnBill.setOnClickListener(this)
        binding.btnLeymar.setOnClickListener(this)
        binding.btnRonaldo.setOnClickListener(this)
    }

    fun dataBinding(data: String){
        this.data = data
    }

    override fun onClick(v: View?) {
        var anwser = "Đáp án trợ giúp là : " + data
        SystemClock.sleep(2000)
        binding.tvAnswer.setText(anwser)
        if (data != ""){
            binding.btnOk.setOnClickListener {
                dismiss()
            }
        }
    }
}