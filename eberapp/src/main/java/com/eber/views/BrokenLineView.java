package com.eber.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.icu.math.BigDecimal;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.eber.R;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zz on 2017/4/26.
 */

public class BrokenLineView extends View {

    private List<String> texts;//x轴文字
    private List<Float> nums;//数值
    private List<Float> numRatios;
    private int startColor;//渐变起始色
    private int endColor;//渐变结束色
    private int dashLineColor;//虚线底线颜色
    private int brokenLineColor;//折线颜色
    private int dashWidth;//虚线宽
    private int dashGap;//虚线缺口宽
    private int textSize;//文字大小
    private int divideHeight;// 文字图标间距
    private int space;//每份间隔量
    private boolean initialized;
    private int drawableHeight;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Paint paint;
    private TextPaint textPaint;
    private Path dashPath;
    private Path brokenPath;
    private Path circelPath;
    private Path gradientPath;
    private String unit;

    public BrokenLineView(Context context) {
        this(context, null);
    }

    public BrokenLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrokenLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        nums = new LinkedList<>();
        texts = new LinkedList<>();
        numRatios = new LinkedList<>();
        dashPath = new Path();
        brokenPath = new Path();
        circelPath = new Path();
        gradientPath = new Path();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BrokenLineView);
        startColor = a.getColor(R.styleable.BrokenLineView_startColor, Color.argb(255, 0, 183, 255));
        endColor = a.getColor(R.styleable.BrokenLineView_endColor, Color.argb(255, 159, 228, 255));
        brokenLineColor = a.getColor(R.styleable.BrokenLineView_brokenLineColor, Color.argb(255, 0, 114, 159));
        dashLineColor = a.getColor(R.styleable.BrokenLineView_dashLineColor, Color.argb(255, 112, 112, 112));
        dashWidth = a.getDimensionPixelSize(R.styleable.BrokenLineView_dashWidth, 5);
        dashGap = a.getDimensionPixelSize(R.styleable.BrokenLineView_dashGap, 5);
        textSize = a.getDimensionPixelSize(R.styleable.BrokenLineView_textSize, 20);
        divideHeight = a.getDimensionPixelSize(R.styleable.BrokenLineView_divideHeight, 10);
        paint = new Paint();
        paint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public List<String> getTexts() {
        return texts;
    }

    public BrokenLineView setTexts(List<String> texts) {
        if (texts != null && !texts.isEmpty()) {
            this.texts.clear();
            this.texts.addAll(texts);
        } else {
            this.texts.clear();
        }
        return this;
    }

    public BrokenLineView setUnitText(String unit){
        this.unit = unit;
        return this;
    }

    public void draw() {
        initialized = false;
        invalidate();
    }

    public List<Float> getNums() {
        return nums;
    }

    public BrokenLineView setNums(List<Float> nums) {
        if (nums != null && !nums.isEmpty()) {
            this.nums.clear();
            this.nums.addAll(nums);
            this.numRatios.clear();
            float max = Collections.max(this.nums);
            for (float num : this.nums) {
                this.numRatios.add(num / max);
            }
        } else {
            this.nums.clear();
            this.numRatios.clear();
        }
        return this;
    }

    public int getStartColor() {
        return startColor;
    }

    public BrokenLineView setStartColor(int startColor) {
        this.startColor = startColor;
        return this;
    }

    public int getEndColor() {
        return endColor;
    }

    public BrokenLineView setEndColor(int endColor) {
        this.endColor = endColor;
        return this;
    }

    public int getDashLineColor() {
        return dashLineColor;
    }

    public BrokenLineView setDashLineColor(int dashLineColor) {
        this.dashLineColor = dashLineColor;
        return this;
    }

    public int getBrokenLineColor() {
        return brokenLineColor;
    }

    public BrokenLineView setBrokenLineColor(int brokenLineColor) {
        this.brokenLineColor = brokenLineColor;
        return this;
    }

    public int getDashWidth() {
        return dashWidth;
    }

    public BrokenLineView setDashWidth(int dashWidth) {
        this.dashWidth = dashWidth;
        return this;
    }

    public int getDashGap() {
        return dashGap;
    }

    public BrokenLineView setDashGap(int dashGap) {
        this.dashGap = dashGap;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }

    public BrokenLineView setTextSize(int textSize) {
        this.textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        return this;
    }

    public BrokenLineView setTextSize(int unit, int textSize) {
        this.textSize = (int) TypedValue.applyDimension(unit, textSize, getResources().getDisplayMetrics());
        return this;
    }

    public int getDivideHeight() {
        return divideHeight;
    }

    public BrokenLineView setDivideHeight(int divideHeight) {
        this.divideHeight = divideHeight;
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initData();
        if (getBackground() != null)
            getBackground().draw(canvas);

        //绘制虚线、轴线
        paint.setStrokeWidth(2);
        dashPath.reset();
        brokenPath.reset();
        circelPath.reset();
        gradientPath.reset();
        for (int i = 0; i < numRatios.size(); i++) {
            float x = startX + space * i;
            float y = startY + (1 - numRatios.get(i)) * drawableHeight;
            dashPath.moveTo(x, endY);
            dashPath.lineTo(x, y);
            circelPath.addCircle(x, y, 5, Path.Direction.CW);

            //绘制渐变填充
            paint.setStyle(Paint.Style.FILL);
            if (i + 1 < numRatios.size()) {
                float nextX = startX + space * (i + 1);
                float nextY = startY + (1 - numRatios.get(i + 1)) * drawableHeight;
                gradientPath.reset();
                gradientPath.moveTo(x, y);
                gradientPath.lineTo(nextX, nextY);
                gradientPath.lineTo(nextX, endY);
                gradientPath.lineTo(x, endY);
                paint.setShader(getGradientShader(nextY > y ? nextX : x, nextY > y ? nextY : y));
                canvas.drawPath(gradientPath, paint);
            }

            if (i == 0) {
                brokenPath.moveTo(x, y);
                gradientPath.reset();
                gradientPath.moveTo(x, y);
            } else {
                brokenPath.lineTo(x, y);
            }
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dd=fnum.format(nums.get(i));
            String text = dd + unit;
            textPaint.setColor(brokenLineColor);
            float textWidth = textPaint.measureText(text);
            canvas.drawText(text, x - textWidth / 2, y - 20, textPaint);
        }
        //折线
        paint.setShader(null);
        paint.setColor(brokenLineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(brokenPath, paint);
        //虚线
        paint.setColor(dashLineColor);
        paint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashWidth}, 0));
        canvas.drawPath(dashPath, paint);
        //圆点
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPath(circelPath, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(brokenLineColor);
        canvas.drawPath(circelPath, paint);

        canvas.drawLine(getPaddingLeft(), endY, getWidth() - getPaddingRight(), endY, paint);
        //绘制文字
        float baseLine = getHeight() - getPaddingBottom() - textPaint.descent();
        textPaint.setColor(dashLineColor);
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            float textWidth = textPaint.measureText(text);
            canvas.drawText(text, startX + space * i - textWidth / 2, baseLine, textPaint);
        }
    }

    private Shader getGradientShader(float x, float y) {
        Shader mShader = new LinearGradient(x, y, x, endY,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        return mShader;
    }

    private void initData() {
        if (initialized) return;
        initialized = true;
        space = (getWidth() - getPaddingRight() - getPaddingLeft()) / (nums.size() + 1);
        startX = space + getPaddingLeft();
        startY = getPaddingTop() + textSize + 20;
        endX = getWidth() - getPaddingRight() - space;
        endY = getHeight() - getPaddingBottom() - divideHeight - textSize - 3;
        drawableHeight = endY - startY;
    }
}
