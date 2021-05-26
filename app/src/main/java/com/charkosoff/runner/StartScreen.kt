package com.charkosoff.runner

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationAttributes
import android.util.AttributeSet
import android.view.View
import android.widget.Button

class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}