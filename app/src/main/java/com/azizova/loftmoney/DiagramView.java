package com.azizova.loftmoney;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DiagramView extends View {

    private float mExpences;
    private float mIncome;
    private Paint expencePaint = new Paint();
    private Paint incomePaint = new Paint();


    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPie, defStyleAttr, 0);
        int colorExpence = a.getColor(R.styleable.ColorPie_color_expence, Color.BLACK);
        int colorIncome = a.getColor(R.styleable.ColorPie_color_income, Color.BLACK);
        a.recycle();
        init(colorExpence, colorIncome);
    }

    public void update(float expences, float income) {
        mExpences = expences;
        mIncome = income;
        invalidate();
    }

    private void init(int colorExpence, int colorIncome){
        //expencePaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_sky_blue));
        //incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));
        incomePaint.setColor(colorIncome);
        expencePaint.setColor(colorExpence);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = mExpences + mIncome;
        float expenceAndle = 360f*mExpences/total;
        float incomeAngle = 360f*mIncome/total;

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space*2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenceAndle/2, expenceAndle, true, expencePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle/2, incomeAngle,true, incomePaint);
    }


}
