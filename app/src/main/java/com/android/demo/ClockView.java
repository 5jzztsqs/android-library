package com.android.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ClockView extends View{


    private static final String TAG = ClockView.class.getSimpleName();

    private static final int DEFAULT_MAX_SIZE = 300;
    private static final int DEFAULT_STROKE_WIDTH = 20;
    private static final long COUNT_DOWN_INTERVAL = 1000;
    private static final int DEFAULT_MAX_CLOCK = 10;

    private int size = DEFAULT_MAX_SIZE;
    private int strokeWidth = DEFAULT_STROKE_WIDTH;

    private int maxClock = DEFAULT_MAX_CLOCK;
    private boolean clocking;
    private Paint paint = new Paint();
    private RectF rectF;

    private int sweepAngle = 0;
    private OnClockListener onClockListener;
    private long lastTouchEventUpTime;
    private CountDownTimer countDownTimer;
    private int clockSeconds = 0;
    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initPaint();
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        rectF = new RectF(strokeWidth, strokeWidth, size-strokeWidth , size-strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (clocking) {
            canvas.drawColor(Color.TRANSPARENT);
            paint.setColor(Color.WHITE);
            //内圆
            paint.setStyle(Paint.Style.FILL);
            //内圆
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(size / 2, size / 2, (int) (size / 2 * 0.3), paint);
            //外圆
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
            canvas.drawCircle(size / 2, size / 2, (size-strokeWidth*2) / 2, paint);
            paint.setColor(Color.GREEN);
            canvas.drawArc(rectF, -90, sweepAngle, false, paint);
        } else {
            paint.setColor(Color.WHITE);
            //内圆
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(size / 2, size / 2, (int) (size / 2 * 0.7), paint);
            //外圆
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth / 2);
            canvas.drawCircle(size / 2, size / 2,(size-strokeWidth) / 2, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //防抖动
                if((System.currentTimeMillis() - lastTouchEventUpTime) > 300){
                    startClock();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                lastTouchEventUpTime = System.currentTimeMillis();
                finishClock();
                break;
        }
        return true;
    }

    private void startClock(){
        clocking = true;
        sweepAngle = 0;
        clockSeconds = 0;
        countDownTimer = new CountDownTimer(maxClock * COUNT_DOWN_INTERVAL,COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                sweepAngle += 360 / maxClock;
                clockSeconds++;
                invalidate();
            }

            @Override
            public void onFinish() {
                finishClock();
            }
        };
        countDownTimer.start();
        if(onClockListener != null){
            onClockListener.onStartClock();
        }
    }

    private void finishClock(){
        clocking = false;
        sweepAngle = 0;
        invalidate();
        if (onClockListener != null) {
            onClockListener.onFinishClock(clockSeconds);
        }
        countDownTimer.cancel();
        clockSeconds = 0;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size, size);
    }

    public void setMaxClock(int maxClock) {
        this.maxClock = maxClock;
    }

    public void setOnClockListener(OnClockListener onClockListener) {
        this.onClockListener = onClockListener;
    }

    interface OnClockListener {
        void onStartClock();
        void onFinishClock(int seconds);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

}
