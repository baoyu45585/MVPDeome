//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hisunfly.one.component.thread.ExecutorServiceHelp;
import com.hisunfly.one.component.utils.AppUtils;
import com.hisunfly.one.component.utils.DensityUtils;
import com.hisunfly.one.component.utils.FileUtils;
import com.hisunfly.one.component.utils.PrintLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    public static final int DEFAULT_ROUNDDED_CORNER = 3;
    private static int sDefaultImg;
    private static int sDefaultCornerRadius;
    private static float sDefaultGifSizeMultiplier = 1.0F;

    public ImageLoader() {
    }

    public static void init(Context context, int defaultImg) {
        sDefaultImg = defaultImg;
        sDefaultCornerRadius = DensityUtils.dp2px(context, 3.0F);
    }

    public static void setDefaultGifSizeMultiplier(float gifSizeMultiplier) {
        sDefaultGifSizeMultiplier = gifSizeMultiplier;
    }

    private static ImageLoader.RequestBuilder emptyRequest() {
        return new ImageLoader.RequestBuilder((RequestManager)null, (Context)null);
    }

    public static ImageLoader.RequestBuilder with(Context context) {
        if (!AppUtils.checkContext(context)) {
            return emptyRequest();
        } else {
            try {
                RequestManager requestManager = Glide.with(context);
                return new ImageLoader.RequestBuilder(requestManager, context);
            } catch (Exception var2) {
                var2.printStackTrace();
                return emptyRequest();
            }
        }
    }

    public static ImageLoader.RequestBuilder with(Fragment fragment) {
        if (fragment != null && !fragment.isDetached() && !fragment.isHidden() && !fragment.isRemoving()) {
            Context context = fragment.getContext();
            if (!AppUtils.checkContext(context)) {
                return emptyRequest();
            } else {
                try {
                    RequestManager requestManager = Glide.with(context);
                    return new ImageLoader.RequestBuilder(requestManager, context);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return emptyRequest();
                }
            }
        } else {
            return emptyRequest();
        }
    }

    public static ImageLoader.RequestBuilder with(View imageView) {
        if (imageView == null) {
            return emptyRequest();
        } else if (!(imageView instanceof ImageView)) {
            throw new IllegalArgumentException("必须传ImageView");
        } else {
            Context context = imageView.getContext();
            if (!AppUtils.checkContext(context)) {
                return emptyRequest();
            } else {
                try {
                    RequestManager requestManager = Glide.with(context);
                    return new ImageLoader.RequestBuilder(requestManager, (ImageView)imageView);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return emptyRequest();
                }
            }
        }
    }

    public static void onTrimMemory(Context context, int level) {
        try {
            Glide.get(context).onTrimMemory(level);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void clearMemory(Context context) {
        try {
            Glide.get(context).clearMemory();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void clearDiskCache(Context context) {
        try {
            Glide.get(context).clearDiskCache();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void clear(View view) {
        try {
            Glide.with(view).clear(view);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static String[] getOriImgs(String[] imgs, String suffix) {
        String[] oriImgs = new String[imgs.length];

        for(int i = 0; i < imgs.length; ++i) {
            String img = imgs[i];
            oriImgs[i] = getOriImg(img, suffix);
        }

        return oriImgs;
    }

    public static String getOriImg(String img, String suffix) {
        String originImg;
        if (img.contains(suffix)) {
            int index = img.indexOf(suffix) + suffix.length();
            String param = img.substring(index);
            index = img.lastIndexOf(".");
            String str = img.substring(0, index);
            int index2 = img.lastIndexOf("?");
            String fmt = img.substring(index, index2);
            originImg = str + param + fmt;
        } else {
            originImg = img;
        }

        return originImg;
    }

    public static boolean saveImageFileToSdcard(Bitmap bm, String url, String fileName) {
        if (bm != null && fileName != null) {
            File myCaptureFile = new File(fileName);
            myCaptureFile.delete();

            try {
                myCaptureFile.createNewFile();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                if (null != bos) {
                    if (!".png".equals(getSuffix(url).toLowerCase()) && !".HISUN".equals(getSuffix(url))) {
                        bm.compress(CompressFormat.JPEG, 80, bos);
                    } else {
                        bm.compress(CompressFormat.PNG, 80, bos);
                    }

                    bos.flush();
                    bos.close();
                    return true;
                }
            } catch (FileNotFoundException var7) {
                myCaptureFile.delete();
                var7.printStackTrace();
            } catch (IOException var8) {
                myCaptureFile.delete();
                var8.printStackTrace();
            } catch (Exception var9) {
                var9.printStackTrace();
            }

            return false;
        } else {
            return false;
        }
    }

    private static String getSuffix(String imageName) {
        String subffix = "";
        int startCharindex = imageName.lastIndexOf(46);
        if (startCharindex != -1) {
            subffix = imageName.substring(startCharindex);
        }

        return subffix.trim();
    }

    public static class RequestBuilder<T> {
        RequestManager requestManager;
        RequestOptions requestOptions;
        Context context;
        ImageView imageView;
        T model;
        T thumbnailModel;
        int placeholder;
        boolean noPlaceholder;
        int error;
        boolean noError;
        int resizeWidth;
        int resizeHeight;
        float sizeMultiplier;
        boolean sizeMultiplierByUser;
        boolean useMemoryCache;
        boolean useDiskCache;
        boolean asBitmap;
        boolean asGif;
        @ImageScaleType.Val
        int imageScaleType;
        Transformation[] customTransformations;
        @TransformType.Val
        int transformType;
        Object[] transformArgs;
        ImageCallback callback;
        boolean dontAnimate;
        boolean supportCrossFade;
        int crossFadeDuration;
        boolean enableAlpha;

        public RequestBuilder(RequestManager requestManager, Context context) {
            this.placeholder = ImageLoader.sDefaultImg;
            this.noPlaceholder = false;
            this.error = ImageLoader.sDefaultImg;
            this.noError = false;
            this.resizeWidth = -1;
            this.resizeHeight = -1;
            this.sizeMultiplierByUser = false;
            this.useMemoryCache = true;
            this.useDiskCache = true;
            this.asBitmap = false;
            this.asGif = false;
            this.imageScaleType = 0;
            this.transformType = 0;
            this.supportCrossFade = false;
            this.enableAlpha = false;
            this.requestManager = requestManager;
            this.context = context;
            this.requestOptions = new RequestOptions();
        }

        public RequestBuilder(RequestManager requestManager, ImageView imageView) {
            this.placeholder = ImageLoader.sDefaultImg;
            this.noPlaceholder = false;
            this.error = ImageLoader.sDefaultImg;
            this.noError = false;
            this.resizeWidth = -1;
            this.resizeHeight = -1;
            this.sizeMultiplierByUser = false;
            this.useMemoryCache = true;
            this.useDiskCache = true;
            this.asBitmap = false;
            this.asGif = false;
            this.imageScaleType = 0;
            this.transformType = 0;
            this.supportCrossFade = false;
            this.enableAlpha = false;
            this.requestManager = requestManager;
            this.imageView = imageView;
            this.context = imageView.getContext();
            this.requestOptions = new RequestOptions();
        }

        public ImageLoader.RequestBuilder<T> load(T model) {
            this.model = model;
            return this;
        }

        public ImageLoader.RequestBuilder<T> thumbnail(T thumbnailModel) {
            this.thumbnailModel = thumbnailModel;
            return this;
        }

        public ImageLoader.RequestBuilder<T> resize(int width, int height) {
            this.resizeWidth = width;
            this.resizeHeight = height;
            return this;
        }

        public ImageLoader.RequestBuilder<T> sizeMultiplier(@FloatRange(from = 0.0D,to = 1.0D) float sizeMultiplier) {
            this.sizeMultiplier = sizeMultiplier;
            this.sizeMultiplierByUser = true;
            return this;
        }

        public ImageLoader.RequestBuilder<T> resize(int size) {
            this.resizeWidth = size;
            this.resizeHeight = size;
            return this;
        }

        public ImageLoader.RequestBuilder<T> placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public ImageLoader.RequestBuilder<T> noPlacehoder(boolean noPlaceholder) {
            this.noPlaceholder = noPlaceholder;
            return this;
        }

        public ImageLoader.RequestBuilder<T> error(int error) {
            this.error = error;
            return this;
        }

        public ImageLoader.RequestBuilder<T> noError(boolean noError) {
            this.noError = noError;
            return this;
        }

        public ImageLoader.RequestBuilder<T> useCache(boolean useCache) {
            this.useMemoryCache = this.useDiskCache = useCache;
            return this;
        }

        public ImageLoader.RequestBuilder<T> useMemoryCache(boolean useMemoryCache) {
            this.useMemoryCache = useMemoryCache;
            return this;
        }

        public ImageLoader.RequestBuilder<T> useDiskCache(boolean useDiskCache) {
            this.useDiskCache = useDiskCache;
            return this;
        }

        public ImageLoader.RequestBuilder<T> asBitmap(boolean asBitmap) {
            this.asBitmap = asBitmap;
            return this;
        }

        public ImageLoader.RequestBuilder<T> asGif(boolean asGif) {
            this.asGif = asGif;
            return this;
        }

        public ImageLoader.RequestBuilder<T> centerCrop() {
            this.imageScaleType = 1;
            return this;
        }

        public ImageLoader.RequestBuilder<T> fitCenter() {
            this.imageScaleType = 2;
            return this;
        }

        public ImageLoader.RequestBuilder<T> centerInside() {
            this.imageScaleType = 3;
            return this;
        }

        public ImageLoader.RequestBuilder<T> transformations(Transformation... transformations) {
            this.customTransformations = transformations;
            return this;
        }

        public ImageLoader.RequestBuilder<T> circle() {
            this.transformType = 3;
            return this;
        }

        public ImageLoader.RequestBuilder<T> roundedCorners() {
            this.transformType = 2;
            this.transformArgs = new Object[2];
            this.transformArgs[0] = ImageLoader.sDefaultCornerRadius;
            this.transformArgs[1] = 0;
            return this;
        }

        public ImageLoader.RequestBuilder<T> roundedCorners(int cornerRadius) {
            this.transformType = 2;
            this.transformArgs = new Object[2];
            this.transformArgs[0] = cornerRadius;
            this.transformArgs[1] = 0;
            return this;
        }

        public ImageLoader.RequestBuilder<T> blur(int radius, float scaleX, float scaleY) {
            this.transformType = 1;
            this.transformArgs = new Object[3];
            this.transformArgs[0] = radius;
            this.transformArgs[1] = scaleX;
            this.transformArgs[2] = scaleY;
            return this;
        }

        public ImageLoader.RequestBuilder dontAnimate() {
            this.dontAnimate = true;
            return this;
        }

        public ImageLoader.RequestBuilder crossFade() {
            this.supportCrossFade = true;
            this.crossFadeDuration = 300;
            return this;
        }

        public ImageLoader.RequestBuilder crossFade(int duration) {
            this.supportCrossFade = true;
            this.crossFadeDuration = duration;
            return this;
        }

        public ImageLoader.RequestBuilder enableAlpha(boolean enableAlpha) {
            this.enableAlpha = enableAlpha;
            return this;
        }

        public ImageLoader.RequestBuilder<T> callback(ImageCallback callback) {
            this.callback = callback;
            return this;
        }

        public Bitmap intoBitmap() {
            if (this.requestManager == null) {
                return null;
            } else {
                try {
                    this.processRequestOptions();
                    return (Bitmap)this.requestManager.asBitmap().load(this.model).apply(this.requestOptions).submit().get();
                } catch (Exception var2) {
                    var2.printStackTrace();
                    return null;
                }
            }
        }

        public Drawable intoDrawable() {
            if (this.requestManager == null) {
                return null;
            } else {
                try {
                    return (Drawable)this.requestManager.asDrawable().load(this.model).submit(-2147483648, -2147483648).get();
                } catch (Exception var2) {
                    var2.printStackTrace();
                    return null;
                }
            }
        }

        public void postInto() {
            this.postInto(this.imageView);
        }

        public void postInto(final ImageView imageView) {
            if (imageView != null) {
                imageView.post(new Runnable() {
                    public void run() {
                        RequestBuilder.this.into(imageView);
                    }
                });
            }

        }

        public void into() {
            this.into(this.imageView);
        }

        public void into(ImageView imageView) {
            try {
                if (this.requestManager == null || imageView == null) {
                    return;
                }

                this.processRequestOptions();
                com.bumptech.glide.RequestBuilder requestBuilder;
                if (this.asBitmap) {
                    requestBuilder = this.requestManager.asBitmap().load(this.model).apply(this.requestOptions);
                } else if (this.asGif) {
                    requestBuilder = this.requestManager.asGif().load(this.model).apply(this.requestOptions);
                } else {
                    requestBuilder = this.requestManager.load(this.model).apply(this.requestOptions);
                }

                if (this.supportCrossFade) {
                    requestBuilder.transition(DrawableTransitionOptions.withCrossFade(this.crossFadeDuration));
                }

                this.processCallback(requestBuilder);
                if (this.thumbnailModel != null) {
                    requestBuilder = requestBuilder.thumbnail(Glide.with(this.context).load(this.thumbnailModel).apply(this.requestOptions));
                }

                requestBuilder.into(imageView);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }

        public void into(final File dst) {
            if (this.requestManager != null && dst != null) {
                this.requestManager.asFile().load(this.model).into(new SimpleTarget<File>() {
                    public void onResourceReady(@NonNull final File resource, @Nullable Transition<? super File> transition) {
                        ExecutorServiceHelp.executeDisplay(new Runnable() {
                            public void run() {
                                try {
                                    if (!dst.exists()) {
                                        dst.delete();
                                    }

                                    File parentFile = dst.getParentFile();
                                    if (!parentFile.exists()) {
                                        parentFile.mkdir();
                                    }

                                    FileUtils.copyFile(resource, dst);
                                    PrintLog.e("ImageLoader", "into File 下载成功");
                                    if (RequestBuilder.this.callback != null) {
                                        RequestBuilder.this.callback.onSuccess(dst);
                                    }
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                    if (RequestBuilder.this.callback != null) {
                                        RequestBuilder.this.callback.onError(new Exception("image load failed"));
                                    }
                                }

                            }
                        });
                    }

                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        PrintLog.e("ImageLoader", "into File 下载失败");
                        if (RequestBuilder.this.callback != null) {
                            RequestBuilder.this.callback.onError(new Exception("image load failed"));
                        }

                    }
                });
            }
        }

        public void intoFile(final File destFile) {
            if (this.requestManager != null && destFile != null) {
                this.processRequestOptions();
                this.requestManager.asBitmap().load(this.model).apply(this.requestOptions).listener(new RequestListener<Bitmap>() {
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (RequestBuilder.this.callback != null) {
                            RequestBuilder.this.callback.onError(new Exception("image load failed"));
                        }

                        return false;
                    }

                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        FileOutputStream fos = null;

                        try {
                            if (!destFile.exists()) {
                                destFile.delete();
                            }

                            fos = new FileOutputStream(destFile);
                            resource.compress(CompressFormat.PNG, 100, fos);
                            fos.flush();
                            if (RequestBuilder.this.callback != null) {
                                RequestBuilder.this.callback.onSuccess(destFile);
                            }
                        } catch (FileNotFoundException var18) {
                            var18.printStackTrace();
                            if (RequestBuilder.this.callback != null) {
                                RequestBuilder.this.callback.onError(new Exception("image load failed"));
                            }
                        } catch (Exception var19) {
                            var19.printStackTrace();
                            if (RequestBuilder.this.callback != null) {
                                RequestBuilder.this.callback.onError(new Exception("image load failed"));
                            }
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException var17) {
                                var17.printStackTrace();
                            }

                        }

                        return true;
                    }
                }).submit();
            }
        }

        public File downloadSync() throws ExecutionException, InterruptedException {
            if (this.requestManager == null) {
                return null;
            } else {
                FutureTarget<File> target = this.requestManager.asFile().load(this.model).submit(-2147483648, -2147483648);
                return (File)target.get();
            }
        }

        public void downloadAsync() {
            if (this.requestManager != null) {
                try {
                    this.requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    this.requestManager.load(this.model).into(new SimpleTarget<Drawable>() {
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (RequestBuilder.this.callback != null) {
                                RequestBuilder.this.callback.onSuccess(resource);
                            }

                        }

                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            if (RequestBuilder.this.callback != null) {
                                RequestBuilder.this.callback.onError(new Exception("image load failed"));
                            }

                        }
                    });
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        }

        private void processCallback(com.bumptech.glide.RequestBuilder requestBuilder) {
            if (this.callback != null) {
                requestBuilder.listener(new RequestListener<Drawable>() {
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (RequestBuilder.this.callback != null) {
                            RequestBuilder.this.callback.onError(e);
                        }

                        return false;
                    }

                    public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        if (RequestBuilder.this.callback != null) {
                            RequestBuilder.this.callback.onSuccess(resource);
                        }

                        return false;
                    }
                });
            }
        }

        private void processRequestOptions() {
            this.requestOptions.skipMemoryCache(!this.useMemoryCache).diskCacheStrategy(this.useDiskCache ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.NONE);
            if (this.enableAlpha) {
                this.requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            }

            if (this.resizeWidth != -1 || this.resizeHeight != -1) {
                this.requestOptions.override(this.resizeWidth > 0 ? this.resizeWidth : -2147483648, this.resizeHeight > 0 ? this.resizeHeight : -2147483648);
            }

            if (this.sizeMultiplierByUser) {
                this.requestOptions.sizeMultiplier(this.sizeMultiplier);
            } else if (this.isGif()) {
                this.requestOptions.sizeMultiplier(ImageLoader.sDefaultGifSizeMultiplier);
            }

            this.applyTransformation();
            if (!this.noPlaceholder) {
                this.requestOptions.placeholder(this.placeholder);
            }

            if (!this.noError) {
                this.requestOptions.error(this.error);
            }

            if (this.dontAnimate) {
                this.requestOptions.dontAnimate();
            }

        }

        private boolean isGif() {
            if (this.model != null && this.model instanceof String) {
                String targetUrl = (String)this.model;
                return targetUrl.toUpperCase().endsWith("GIF");
            } else {
                return false;
            }
        }

        private void applyTransformation() {
            List<Transformation> transformations = new ArrayList();
            Transformation scaleTypeTransformation = this.processImageScaleType();
            if (scaleTypeTransformation != null) {
                transformations.add(scaleTypeTransformation);
            }

            if (this.customTransformations != null) {
                Transformation[] var3 = this.customTransformations;
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Transformation transformation = var3[var5];
                    if (transformation != null) {
                        transformations.add(transformation);
                    }
                }
            }

            Transformation transformTypeTransformation = this.processTransformType();
            if (transformTypeTransformation != null) {
                transformations.add(transformTypeTransformation);
            }

            if (transformations.size() > 0) {
                MultiTransformation multiTransformation = new MultiTransformation(transformations);
                this.requestOptions.apply(RequestOptions.bitmapTransform(multiTransformation).optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(multiTransformation)));
            }

        }

        private Transformation processImageScaleType() {
            Transformation transformation = null;
            switch(this.imageScaleType) {
            case 1:
                transformation = new CenterCrop();
                break;
            case 2:
                transformation = new FitCenter();
                break;
            case 3:
                transformation = new CenterInside();
            }

            return (Transformation)transformation;
        }

        private Transformation processTransformType() {
            Transformation transformation = null;
            switch(this.transformType) {
            case 1:
                transformation = new StackBlurTransformation((Integer)this.transformArgs[0], (Float)this.transformArgs[1], (Float)this.transformArgs[2]);
                break;
            case 2:
                transformation = new RoundedCornersTransformation((Integer)this.transformArgs[0], (Integer)this.transformArgs[1]);
                break;
            case 3:
                transformation = new CircleCrop();
            }

            return (Transformation)transformation;
        }
    }
}
