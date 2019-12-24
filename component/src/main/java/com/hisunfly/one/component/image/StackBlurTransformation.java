//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.support.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.internal.FastBlur;

public class StackBlurTransformation extends BitmapTransformation {
    private static final int VERSION = 1;
    private static final String ID = "com.hisunflytone.core.image.transformation.StackBlurTransformation.1";
    private int radius;
    private float scaleX;
    private float scaleY;

    public StackBlurTransformation(int radius, float scaleX, float scaleY) {
        this.radius = radius;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = (int)((float)width * this.scaleX);
        int scaledHeight = (int)((float)height * this.scaleY);
        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(this.scaleX, this.scaleY);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(toTransform, 0.0F, 0.0F, paint);
        bitmap = FastBlur.blur(bitmap, this.radius, true);
        return bitmap;
    }

    public String toString() {
        return "StackBlurTransformation(radius=" + this.radius + ", scaleX=" + this.scaleX + ",scaleY=" + this.scaleY + ")";
    }

    public boolean equals(Object o) {
        return o instanceof StackBlurTransformation && ((StackBlurTransformation)o).radius == this.radius && ((StackBlurTransformation)o).scaleX == this.scaleX && ((StackBlurTransformation)o).scaleY == this.scaleY;
    }

    public int hashCode() {
        return "com.hisunflytone.core.image.transformation.StackBlurTransformation.1".hashCode() + this.radius * 1000 + (int)(this.scaleX * 10.0F) + (int)(this.scaleY * 10.0F);
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("com.hisunflytone.core.image.transformation.StackBlurTransformation.1" + this.radius + this.scaleX + this.scaleY).getBytes(CHARSET));
    }
}
