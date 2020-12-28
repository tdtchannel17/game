package com.ddona.ailatrieuphu

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ddona.ailatrieuphu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val musicIds = mutableListOf<Int>()
    private var media:MediaPlayer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnPlay.setOnClickListener(this)
        binding.btnQuit.setOnClickListener(this)

        musicIds.add(R.raw.main)
        media?.release()
        media = MediaPlayer.create(
            this,
            musicIds[0]
        )
        //mo nhac
        media?.start()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_play -> {
                media?.pause()
                val intent = Intent(this, IntroduceActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
            R.id.btn_quit -> {
                media?.pause()
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
    }
}
