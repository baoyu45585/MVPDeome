//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.hisunfly.one.component.R;


public class RoundImageView extends ImageView {
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    private static final int BODER_RADIUS_DEFAULT = 10;
    private int mBorderRadius;
    private Paint mBitmapPaint;
    private int mRadius;
    private Matrix mMatrix;
    private BitmapShader mBitmapShader;
    private int mWidth;
    private RectF mRoundRect;
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMatrix = new Matrix();
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        this.mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius, (int)TypedValue.applyDimension(1, 10.0F, this.getResources().getDisplayMetrics()));
        this.type = a.getInt(R.styleable.RoundImageView_type, 0);
        a.recycle();
    }

    public RoundImageView(Context context) {
        this(context, (AttributeSet)null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.type == 0) {
            this.mWidth = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
            this.mRadius = this.mWidth / 2;
            this.setMeasuredDimension(this.mWidth, this.mWidth);
        }

    }

    private void setUpShader() {
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            Bitmap bmp = this.drawableToBitamp(drawable);
            this.mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
            float scale = 1.0F;
            if (this.type == 0) {
                int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
                scale = (float)this.mWidth * 1.0F / (float)bSize;
            } else if (this.type == 1) {
                Log.e("TAG", "b'w = " + bmp.getWidth() + " , b'h = " + bmp.getHeight());
                if (bmp.getWidth() != this.getWidth() || bmp.getHeight() != this.getHeight()) {
                    scale = Math.max((float)this.getWidth() * 1.0F / (float)bmp.getWidth(), (float)this.getHeight() * 1.0F / (float)bmp.getHeight());
                }
            }

            this.mMatrix.setScale(scale, scale);
            this.mBitmapShader.setLocalMatrix(this.mMatrix);
            this.mBitmapPaint.setShader(this.mBitmapShader);
        }
    }

    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "onDraw");
        if (this.getDrawable() != null) {
            this.setUpShader();
            if (this.type == 1) {
                canvas.drawRoundRect(this.mRoundRect, (float)this.mBorderRadius, (float)this.mBorderRadius, this.mBitmapPaint);
            } else {
                canvas.drawCircle((float)this.mRadius, (float)this.mRadius, (float)this.mRadius, this.mBitmapPaint);
            }

        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.type == 1) {
            this.mRoundRect = new RectF(0.0F, 0.0F, (float)w, (float)h);
        }

    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable)drawable;
            return bd.getBitmap();
        } else {
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        }
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state_instance", super.onSaveInstanceState());
        bundle.putInt("state_type", this.type);
        bundle.putInt("state_border_radius", this.mBorderRadius);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(((Bundle)state).getParcelable("state_instance"));
            this.type = bundle.getInt("state_type");
            this.mBorderRadius = bundle.getInt("state_border_radius");
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = this.dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            this.invalidate();
        }

    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != 1 && this.type != 0) {
                this.type = 0;
            }

            this.requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int)TypedValue.applyDimension(1, (float)dpVal, this.getResources().getDisplayMetrics());
    }
}
