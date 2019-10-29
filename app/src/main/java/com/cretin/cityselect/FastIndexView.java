package com.cretin.cityselect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FastIndexView extends View {

    private static final String INDEX_NAME = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private OnLetterUpdateListener listener;
    private Paint mPaint;
    private float cellHeight, viewWidth;
    private int touchIndex = -1, selectedColor;

    public FastIndexView(Context context) {
        this(context, null);
    }

    public FastIndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setTextSize(AppUtils.dp2px(14));
        mPaint.setAntiAlias(true);
        //获取文字被选中的颜色
//        selectedColor = ContextCompat.getColor(context, );
        selectedColor = Color.parseColor("#ff0000");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < INDEX_NAME.length(); i++) {
            String text = INDEX_NAME.substring(i, i + 1);
            //计算绘制字符的X方向起点
            int x = (int) (viewWidth / 2.0f - mPaint.measureText(text) / 2.0f);
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height();
            //计算绘制字符的Y方向起点
            int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i
                    * cellHeight);
            //绘制选中字符的背景
            if (touchIndex == i) {
                mPaint.setColor(Color.RED);
                canvas.drawCircle(viewWidth / 2, cellHeight * i + cellHeight / 2, cellHeight / 2, mPaint);
            }
            mPaint.setColor(touchIndex == i ? Color.WHITE : selectedColor);
            canvas.drawText(text, x, y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //计算当前触摸的字符索引
                index = (int) (event.getY() / cellHeight);
                if (index >= 0 && index < INDEX_NAME.length()) {
                    if (index != touchIndex) {
                        if (listener != null) {
                            listener.onLetterUpdate(INDEX_NAME.substring(index, index + 1));
                        }
                        touchIndex = index;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸的字符索引
                index = (int) (event.getY() / cellHeight);
                if (index >= 0 && index < INDEX_NAME.length()) {
                    if (index != touchIndex) {
                        if (listener != null) {
                            listener.onLetterUpdate(INDEX_NAME.substring(index, index + 1));
                        }
                        touchIndex = index;
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //得到当前控件的宽度
        viewWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        //获取单个字符能够拥有的高度
        cellHeight = mHeight * 1.0f / INDEX_NAME.length();
    }

    public interface OnLetterUpdateListener {
        void onLetterUpdate(String letter);
    }

    public void setListener(OnLetterUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * 匹配字母
     *
     * @param letter
     */
    public void matchingLetter(String letter) {
        for (int i = 0; i < INDEX_NAME.length(); i++) {
            if (letter.equals(INDEX_NAME.substring(i, i + 1))) {
                touchIndex = i;
                invalidate();
            }
        }
    }

}