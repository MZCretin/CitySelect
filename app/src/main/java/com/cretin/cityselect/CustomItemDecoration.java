package com.cretin.cityselect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private final int mTitleHeight;
    private final int mTitleTextSize;
    private final Paint mPaint;
    private final Paint mTextPaint;
    private final Rect textRect;
    private TitleDecorationCallback callback;
    private final Paint mGrayPaint;
    private float titlePaddingLeft;

    public CustomItemDecoration(Context context, TitleDecorationCallback callback) {
        this.callback = callback;
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        mTitleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
        titlePaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTitleTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#aaaaaa"));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

        mGrayPaint = new Paint();
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setColor(Color.WHITE);

        textRect = new Rect();
    }

    // 这个方法用于给item隔开距离，类似直接给item设padding
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (position == 0 || isFirst(position)) {
            outRect.top = mTitleHeight;
        } else {
            outRect.top = 0;
        }
    }

    // 这个方法用于给getItemOffsets()隔开的距离填充图形,
    // 在item绘制之前时被调用，将指定的内容绘制到item view内容之下；
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

        // 获取当前屏幕可见 item 数量，而不是 RecyclerView 所有的 item 数量
        int childCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            final View view = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();

            if (position == 0 || isFirst(position)) {
                float top = view.getTop() - mTitleHeight;
                float bottom = view.getTop();
                canvas.drawRect(left, top, right, bottom, mPaint);

                String groupName = callback.getGroupName(position);
                mTextPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                float x = view.getPaddingLeft() + titlePaddingLeft;
                float y = top + (mTitleHeight - textRect.height()) / 3 * 2 + textRect.height();
                canvas.drawText(callback.getGroupName(position), x, y, mTextPaint);
            } else {
                float top = view.getTop() - 1;
                float bottom = view.getTop();
                canvas.drawRect(left, top, right, bottom, mGrayPaint);
            }
        }
    }

    // 在item被绘制之后调用，将指定的内容绘制到item view内容之上
    // 这个方法可以将内容覆盖在item上，可用于制作悬停效果，角标等（这里只实现悬停效果）
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (position <= -1 || position >= parent.getAdapter().getItemCount() - 1) {
            // 越界检查
            return;
        }

        View firstVisibleView = parent.findViewHolderForAdapterPosition(position).itemView;

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + mTitleHeight;


        // 如果当前屏幕上第二个显示的item是下一组的的第一个，并且第一个被title覆盖，则开始移动上个title。
        // 原理就是不断改变title所在矩形的top与bottom的值。
        if (isFirst(position + 1) && firstVisibleView.getBottom() < mTitleHeight) {
            if (mTitleHeight <= firstVisibleView.getHeight()) {
                int d = firstVisibleView.getHeight() - mTitleHeight;
                top = firstVisibleView.getTop() + d;
            } else {
                int d = mTitleHeight - firstVisibleView.getHeight();
                top = firstVisibleView.getTop() - d;// 这里有bug,mTitleHeight过高时 滑动有问题
            }
            bottom = firstVisibleView.getBottom();
        }
        c.drawRect(left, top, right, bottom, mPaint);

        String groupName = callback.getGroupName(position);
        mTextPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
        float x = left + firstVisibleView.getPaddingLeft() + titlePaddingLeft;
        float y = top + (mTitleHeight - textRect.height()) / 3 * 2 + textRect.height();
        c.drawText(groupName, x, y, mTextPaint);
    }

    /**
     * 判断是否是同一组的第一个item
     *
     * @param position
     * @return
     */
    private boolean isFirst(int position) {
        if (position == 0) {
            return true;
        } else {
            String prevGroupId = callback.getGroupId(position - 1);
            String groupId = callback.getGroupId(position);
            return !prevGroupId.equals(groupId);
        }
    }

    public interface TitleDecorationCallback {

        String getGroupId(int position);

        String getGroupName(int position);
    }
}