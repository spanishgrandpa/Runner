package com.charkosoff.runner

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class MySW(context: Context, attributeSet: AttributeSet) : SurfaceView(context, attributeSet),
    SurfaceHolder.Callback {
    private lateinit var mMyThread //наш поток прорисовки
            : DrawThread

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        var retry = true
        mMyThread.setRunning(false) //останавливает процесс

        while (retry) {
            try {
                mMyThread.join() //ждет окончательной остановки процесса
                retry = false
            } catch (e: InterruptedException) {
                //не более чем формальность
            }
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        mMyThread = DrawThread(holder, context)
        mMyThread.setRunning(true);
        mMyThread.start(); //запускает процесс в отдельном потоке
    }

    fun BURN(){
        mMyThread.isFire != mMyThread.isFire
    }

}
