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
import androidx.constraintlayout.widget.ConstraintLayout

class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        val backgrounds = listOf(R.drawable.splactwp0, R.drawable.splactwp1, R.drawable.splactwp2, R.drawable.splactwp3, R.drawable.splactwp4, R.drawable.splactwp5, R.drawable.splactwp6, R.drawable.splactwp7, R.drawable.splactwp8, R.drawable.splactwp9)
        var view = findViewById<ConstraintLayout>(R.id.layout)
        view.setBackgroundResource(backgrounds.random())

        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}