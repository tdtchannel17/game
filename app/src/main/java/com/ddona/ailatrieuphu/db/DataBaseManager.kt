package com.ddona.ailatrieuphu.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log
import com.ddona.ailatrieuphu.model.Question
import java.io.File
import java.io.FileOutputStream

class DataBaseManager {
    companion object {
        val DB_NAME = "latrieuphu"
    }

    private val context: Context

    private var database: SQLiteDatabase? = null

    constructor(context: Context) {
        this.context = context
        copyDatabase()
    }

    fun copyDatabase() {
        val input = context.assets.open(DB_NAME)
        val path = Environment.getDataDirectory().path +
                File.separator + "data" +
                File.separator + context.packageName +
                File.separator + "db"
        File(path).mkdir()
        val fullPath = path + File.separator + DB_NAME
        if (File(fullPath).exists()) {
            return
        }
        val output = FileOutputStream(fullPath)
        val byte = ByteArray(1024)
        var len = input.read(byte)
        while (len >= 0) {
            output.write(byte, 0, len)
            len = input.read(byte)
        }

        input.close()
        output.close()
    }

    private fun openDatabase() {
        if (database == null || !database!!.isOpen()) {
            val path = Environment.getDataDirectory().path +
                    File.separator + "data"+
                    File.separator + context.packageName +
                    File.separator + "db"
            val fullPath = path + File.separator + DB_NAME
            database = SQLiteDatabase.openDatabase(
                fullPath, null,
                SQLiteDatabase.OPEN_READWRITE
            )
        }
    }

    private fun closeDatabase() {
        if (database != null && database!!.isOpen()) {
            database?.close()
            database = null
        }
    }

    fun getFifteen(): MutableList<Question> {
        val questions = mutableListOf<Question>()
        openDatabase()
        val sql =
            "select * from  (select * from question order by random()) group by level order by level asc"
        //bat dau query
        val cursor = database!!.rawQuery(sql, null)
        cursor.moveToFirst()
        val indexId = cursor.getColumnIndex("_id")
        val indexQuestion = cursor.getColumnIndex("question")
        val indexLevel = cursor.getColumnIndex("level")
        val indexCauseA = cursor.getColumnIndex("casea")
        val indexCauseB = cursor.getColumnIndex("caseb")
        val indexCauseC = cursor.getColumnIndex("casec")
        val indexCauseD = cursor.getColumnIndex("cased")
        val indexTrueCase = cursor.getColumnIndex("truecase")
        while (!cursor.isAfterLast) {
            val id = cursor.getInt(indexId)
            val question = cursor.getString(indexQuestion)
            val level = cursor.getInt(indexLevel)
            val casea = cursor.getString(indexCauseA)
            val caseb = cursor.getString(indexCauseB)
            val casec = cursor.getString(indexCauseC)
            val cased = cursor.getString(indexCauseD)
            val truecase = cursor.getInt(indexTrueCase)

            questions.add(
                Question(
                    id, level, question, casea, caseb,
                    casec, cased, truecase
                )
            )
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()

        return questions
    }
}