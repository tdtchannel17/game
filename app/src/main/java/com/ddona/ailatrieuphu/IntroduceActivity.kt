package com.ddona.ailatrieuphu

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ddona.ailatrieuphu.databinding.ActivityIntroduceBinding

class IntroduceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityIntroduceBinding
    private val musicIds = mutableListOf<Int>()
    private var media:MediaPlayer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_introduce)
        binding.btnReady.setOnClickListener(this)

        musicIds.add(R.raw.introduce)
        media?.release()
        media = MediaPlayer.create(
            this,
            musicIds[0]
        )
        //mo nhac
        media?.start()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_ready -> {
                media?.pause()
                val intent = Intent(this, QuestionActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        media?.start()
    }

    override fun onPause() {
        super.onPause()
        media?.pause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        media?.pause()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}

