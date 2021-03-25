package com.charkosoff.runner

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import java.util.concurrent.atomic.AtomicBoolean


class DrawThread(private val surfaceHolder: SurfaceHolder, context: Context) : Thread() {

    var isFire = AtomicBoolean(false)
    private val REDRAW_TIME = 33 //частота обновления экрана - 10 мс

    private var mRunning //запущен ли процесс
            = false
    private var mStartTime //время начала анимации
            : Long = 0
    private var mPrevRedrawTime //предыдущее время перерисовки
            : Long = 0

    private var mPaint: Paint

    var steve = BitmapFactory.decodeResource(context.resources, R.drawable.qwr)
    var fire = BitmapFactory.decodeResource(context.resources, R.drawable.qw)
    var ground = BitmapFactory.decodeResource(context.resources, R.drawable.zemlya)
    var creeper = BitmapFactory.decodeResource(context.resources, R.drawable.creeper)


    init {
        mRunning = false

        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setStyle(Paint.Style.FILL)

    }

    fun setRunning(running: Boolean) { //запускает и останавливает процесс
        mRunning = running
        mPrevRedrawTime = System.currentTimeMillis()
    }

    var firestart = 0L
    var currentTs = 0
    var maxTr = -100
    val speed = 10 // скорость смещения экрана
    var prep1 = MutableList(
        400,
        {
            PointF(
                ((it * 300 + 500)..(it * 300 + 800)).random().toFloat(),
                (surfaceHolder.surfaceFrame.height() - ground.height - creeper.height).toFloat()
            )
        })

    var score = 0
    var char = true


    override fun run() {


        var canvas: Canvas?
        mStartTime = System.currentTimeMillis()

        while (mRunning) {
            val curTime: Long = System.currentTimeMillis()
            val elapsedTime = curTime - mPrevRedrawTime
            if (elapsedTime < REDRAW_TIME) //проверяет, прошло ли 10 мс
                continue
            //если прошло, перерисовываем картинку
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                //получаем canvas
                synchronized(surfaceHolder) {
                    canvas.drawColor(Color.WHITE)

                    draw(canvas, currentTs) //функция рисования
                    prep1.toList().forEach {
                        canvas.drawBitmap(creeper, it.x, it.y, mPaint)
                    }

                    mPaint.color = Color.BLACK
                    mPaint.textSize = 56f
                    canvas.drawText(score.toString(), 10f, 70f, mPaint)
                }

                firetime()
                //смещение
                for (i in prep1.indices) {
                    prep1[i].x -= speed
                    score += 1
                }
                currentTs -= speed
                if (currentTs <= maxTr)
                    currentTs = 0

            } catch (e: NullPointerException) { /*если canvas не доступен*/
            } finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas) //освобождаем canvas
            }
            mPrevRedrawTime = curTime
        }
    }


    private fun draw(canvas: Canvas, transition: Int) {
        val width = canvas.width
        val height = canvas.height

        canvas.drawText(score.toString(), (canvas.width - 50).toFloat(), 10f, mPaint)

        fun drawGrooud(canvas: Canvas, transition: Int) {
            for (i in transition..width step 100) {
                canvas.drawBitmap(
                    ground,
                    null,
                    RectF(
                        i.toFloat(),
                        height.toFloat() - ground.height,
                        i.toFloat() + 100f,
                        height.toFloat()
                    ),
                    mPaint
                )

            }
        }

        if (isFire.get()) {
            canvas.drawBitmap(
                fire,
                switcher(char),
                RectF(
                    100f,
                    (canvas.height - ground.height - steve.height).toFloat(),
                    (steve.width + 100).toFloat(),
                    (canvas.height - ground.height).toFloat()
                ), mPaint
            )
        } else {
            canvas.drawBitmap(
                steve,
                switcher(char),
                RectF(
                    100f,
                    (canvas.height - ground.height - steve.height).toFloat(),
                    (steve.width + 100).toFloat(),
                    (canvas.height - ground.height).toFloat()
                ), mPaint
            )
        }
        char = !char
        for (i in 0..prep1.size - 2) {
            if (prep1[i].x <= (steve.width + 100).toFloat()) {
                if (isFire.get()) {
                    prep1.removeAt(0)
                } else {
                    setRunning(false)
                    mPaint.textSize = 72f
                    mPaint.color = Color.RED
                    canvas.drawText("Вы погибли", 0f, canvas.height / 2f, mPaint)
                }
            }
        }

        drawGrooud(canvas, transition)
    }

    fun switcher(i: Boolean) = if (i)
        Rect(0, 0, steve.width / 2, steve.height)
    else
        Rect(steve.width / 2, 0, steve.width, steve.height)

    var isBurn = false
    fun firetime(){

        if (!isFire.get()) {firestart = 0L}

        if (isFire.get() && !isBurn) {
            firestart = System.currentTimeMillis()
            isBurn = true
            return
        }

        var currentTime = System.currentTimeMillis()

        if (currentTime - firestart > 1000) {
            isFire.set(false)
            isBurn = false
        }


    }

}
