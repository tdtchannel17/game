package com.ddona.ailatrieuphu

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ddona.ailatrieuphu.databinding.ActivityQuestionBinding
import com.ddona.ailatrieuphu.db.DataBaseManager
import com.ddona.ailatrieuphu.model.Question
import com.ddona.ailatrieuphu.ui.dialog.CallDialog
import com.ddona.ailatrieuphu.ui.dialog.ConfirmDialog
import com.ddona.ailatrieuphu.ui.dialog.HelpDialog
import java.util.concurrent.Executors

class QuestionActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuestionBinding
    private lateinit var database: DataBaseManager
    private val questions = mutableListOf<Question>()
    private lateinit var mQuestion: Question
    private var trueCase = 0
    private var currentQuestion = 0
    private var asyn: QuestionAsyntask? = null
    private val musicIds = mutableListOf<Int>()
    private var media: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question)
        database = DataBaseManager(this)
        initData()
        setDataQuestion(questions[currentQuestion])
    }

    private fun initData() {
        questions.addAll(
            database.getFifteen()
        )
    }

    private fun setDataQuestion(question: Question) {
        asyn?.isRunning = true
        if (question == null) {
            return
        }
        musicIds.add(R.raw.question)
        musicIds.add(R.raw.ting_effect)
        musicIds.add(R.raw.wrong_effect)
        media?.release()
        media = MediaPlayer.create(
            this,
            musicIds[0]
        )
        //mo nhac
        media?.start()

        var ex = Executors.newFixedThreadPool(1)
        asyn = QuestionAsyntask()
        asyn?.executeOnExecutor(ex, 60, 0)

        mQuestion = question

        binding.answerA.setBackgroundResource(R.drawable.bg_choose1)
        binding.answerB.setBackgroundResource(R.drawable.bg_choose1)
        binding.answerC.setBackgroundResource(R.drawable.bg_choose1)
        binding.answerD.setBackgroundResource(R.drawable.bg_choose1)

        var qt = "Câu " + question.level + " : " + question.question
        var a = "A." + question.caseA
        var b = "B." + question.caseB
        var c = "C." + question.caseC
        var d = "D." + question.caseD

        binding.tvQuestion.setText(qt)
        binding.answerA.setText(a)
        binding.answerB.setText(b)
        binding.answerC.setText(c)
        binding.answerD.setText(d)
        // click answer
        binding.answerA.setOnClickListener(this)
        binding.answerB.setOnClickListener(this)
        binding.answerC.setOnClickListener(this)
        binding.answerD.setOnClickListener(this)
        // click help
        binding.btnCall.setOnClickListener(this)
        binding.btnHelp.setOnClickListener(this)
        binding.btnPercent50.setOnClickListener(this)
        binding.btnRestart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.answer_a -> {
                binding.answerB.setOnClickListener(null)
                binding.answerC.setOnClickListener(null)
                binding.answerD.setOnClickListener(null)
                binding.answerA.setBackgroundResource(R.drawable.bg_choose2)
                trueCase = 1
                checkAnswer(binding.answerA, mQuestion, trueCase)
            }
            R.id.answer_b -> {
                binding.answerA.setOnClickListener(null)
                binding.answerC.setOnClickListener(null)
                binding.answerD.setOnClickListener(null)
                binding.answerB.setBackgroundResource(R.drawable.bg_choose2)
                trueCase = 2
                checkAnswer(binding.answerB, mQuestion, trueCase)
            }
            R.id.answer_c -> {
                binding.answerA.setOnClickListener(null)
                binding.answerB.setOnClickListener(null)
                binding.answerD.setOnClickListener(null)
                binding.answerC.setBackgroundResource(R.drawable.bg_choose2)
                trueCase = 3
                checkAnswer(binding.answerC, mQuestion, trueCase)
            }
            R.id.answer_d -> {
                binding.answerA.setOnClickListener(null)
                binding.answerB.setOnClickListener(null)
                binding.answerC.setOnClickListener(null)
                binding.answerD.setBackgroundResource(R.drawable.bg_choose2)
                trueCase = 4
                checkAnswer(binding.answerD, mQuestion, trueCase)
            }
            R.id.btn_call -> {
                val dialog = CallDialog(QuestionActivity@ this)
                when (questions[currentQuestion].truecase) {
                    1 -> dialog.dataBinding("A." + questions[currentQuestion].caseA)
                    2 -> dialog.dataBinding("B." + questions[currentQuestion].caseB)
                    3 -> dialog.dataBinding("C." + questions[currentQuestion].caseC)
                    4 -> dialog.dataBinding("D." + questions[currentQuestion].caseD)
                }
                if (dialog.setBtn == true) {
                    binding.btnCall.setBackgroundResource(R.drawable.x)
                    binding.btnCall.setOnClickListener(null)
                }
                dialog.create()
                dialog.show()
            }
            R.id.btn_help -> {
                val dialog = HelpDialog(QuestionActivity@ this)
                dialog.dataBinding(questions[currentQuestion].truecase)
                binding.btnHelp.setBackgroundResource(R.drawable.x)
                binding.btnHelp.setOnClickListener(null)
                dialog.create()
                dialog.show()
            }
            R.id.btn_percent50 -> {
                when (questions[currentQuestion].truecase) {
                    1 -> {
                        binding.answerB.setText("")
                        binding.answerC.setText("")
                    }
                    2 -> {
                        binding.answerA.setText("")
                        binding.answerD.setText("")
                    }
                    3 -> {
                        binding.answerA.setText("")
                        binding.answerD.setText("")
                    }
                    4 -> {
                        binding.answerB.setText("")
                        binding.answerC.setText("")
                    }
                }
                binding.btnPercent50.setBackgroundResource(R.drawable.x)
                binding.btnPercent50.setOnClickListener(null)
            }
            R.id.btn_restart -> {
                nextQuestion()
                binding.btnRestart.setBackgroundResource(R.drawable.x)
                binding.btnRestart.setOnClickListener(null)
            }
        }
    }

    private fun checkAnswer(tv: TextView, question: Question, trueCase: Int) {
        Handler().postDelayed({
            if (question.truecase == trueCase) {
                media?.release()
                media = MediaPlayer.create(
                    this,
                    musicIds[1]
                )
                //mo nhac
                media?.start()
                tv.setBackgroundResource(R.drawable.bg_true2)
                nextQuestion()
            } else {
                media?.release()
                media = MediaPlayer.create(
                    this,
                    musicIds[2]
                )
                //mo nhac
                media?.start()
                tv.setBackgroundResource(R.drawable.bg_faile1)
                showAnswer(question, trueCase)
                SystemClock.sleep(1200)
                gameOver()
            }
        }, 1500)
    }

    private fun nextQuestion() {
        asyn?.isRunning = false
        if (currentQuestion == questions.size - 1) {
            showDialogWin("You Win !!!")
        } else {
            currentQuestion++
            Handler().postDelayed({
                setDataQuestion(questions[currentQuestion])
            }, 1500)
        }
    }

    private fun showAnswer(question: Question, trueCase: Int) {
        if (question == null || question.truecase == null
            || question.caseA == null || question.caseB == null
            || question.caseC == null || question.caseD == null
        ) {
            return
        }

        if (question.truecase == 1) {
            binding.answerA.setBackgroundResource(R.drawable.bg_true2)
        } else if (question.truecase == 2) {
            binding.answerB.setBackgroundResource(R.drawable.bg_true2)
        } else if (question.truecase == 3) {
            binding.answerC.setBackgroundResource(R.drawable.bg_true2)
        } else if (question.truecase == 4) {
            binding.answerD.setBackgroundResource(R.drawable.bg_true2)
        }
    }

    private fun gameOver() {
        asyn?.isRunning = false
        Handler().postDelayed({
            showDialogLost("Game Over")
        }, 500)
    }

    private fun showDialogWin(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(message)
        builder.setCancelable(false)

        builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int ->
            media?.pause()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        })

        builder.create()
        builder.show()
    }

    private fun showDialogLost(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(message)
        builder.setMessage("You want play again ???")
        builder.setCancelable(false)

        builder.setPositiveButton("YES", { dialogInterface: DialogInterface, i: Int ->
            media?.pause()
            val intent = Intent(this, IntroduceActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        })
        builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->
            media?.pause()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        })

        builder.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        media?.pause()
        val intent = Intent(this, IntroduceActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onStart() {
        super.onStart()
        media?.start()
    }

    override fun onPause() {
        super.onPause()
        media?.pause()
    }

    inner class QuestionAsyntask : AsyncTask<Int, String, Void>() {
        var isRunning = true
        override fun onPreExecute() {}

        override fun doInBackground(vararg values: Int?): Void? {
            SystemClock.sleep(100)
            for (i in values[0]!! downTo values[1]!!) {
                publishProgress(i.toString())
                SystemClock.sleep(1000)
                if (!isRunning) {
                    break
                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            binding.tvTimeQuestion.setText(values[0])
            if (values[0] == "0") {
                gameOver()
            }
        }
    }
}