package com.ddona.ailatrieuphu.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.ddona.ailatrieuphu.databinding.DialogHelpBinding

class HelpDialog : Dialog {
    private lateinit var binding: DialogHelpBinding
    var setBtn: Boolean = true
    private var data = 0

    constructor(context: Context) : super(context) {
        inits()
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        inits()
    }

    private fun inits() {
        binding = DialogHelpBinding.inflate(
            LayoutInflater.from(context),
            null, false
        )
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        binding.btnOk.setOnClickListener { dismiss() }
    }

    fun dataBinding(data: Int){
        this.data = data
        when(data){
            1->{
                binding.tvA.setText("52% khán giả chọn A")
                binding.tvB.setText("16% khán giả chọn B")
                binding.tvC.setText("31% khán giả chọn C")
                binding.tvD.setText("1% khán giả chọn D")
            }
            2->{
                binding.tvA.setText("9% khán giả chọn A")
                binding.tvB.setText("61% khán giả chọn B")
                binding.tvC.setText("16% khán giả chọn C")
                binding.tvD.setText("14% khán giả chọn D")
            }
            3->{
                binding.tvA.setText("5% khán giả chọn A")
                binding.tvB.setText("12% khán giả chọn B")
                binding.tvC.setText("55% khán giả chọn C")
                binding.tvD.setText("28% khán giả chọn D")
            }
            4->{
                binding.tvA.setText("3% khán giả chọn A")
                binding.tvB.setText("0% khán giả chọn B")
                binding.tvC.setText("1% khán giả chọn C")
                binding.tvD.setText("96% khán giả chọn D")
            }
        }
    }
}