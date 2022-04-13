package com.ljq.blurimage;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * @author :ljq
 * @des : 图片操作类
 * @DATA : 2022/4/9
 */
public class BlurUtil {
    private final static String TAG = BlurUtil.class.getSimpleName();

    public static final String HORIZONTAL_TYPE = "0"; // 横图
    public static final String VERTICAL_TYPE = "1"; // 竖图

    public static final int BLUR_RADIUS = 15; // 模糊系数 范围 1-25 影响计算速度 越大越清晰越慢

    public static void loadImage(ImageView img, String url) {
        Glide.with(img).load(url).into(img);
    }

    /**
     * 高斯模糊图片
     * 如果是方向和显示组件一样 则正常显示
     * 如果不是 则图片缩放居中 然后高斯模糊center_crop以后的图片做背景
     *
     * @param img        图片空间
     * @param url        图片地址
     * @param radiusDp   圆角角度
     * @param viewWidth  控件宽度
     * @param viewHeight 控件高度
     */
    public static void loadBlurImageVertical(ImageView img, String url,
                                             float radiusDp, int viewWidth, int viewHeight) {
        RoundedCorners roundedCorners = new RoundedCorners((int) radiusDp);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(img).load(url).placeholder(R.drawable.ic_launcher_background).apply(requestOptions.
                centerCrop().transform(roundedCorners,new BlurImageVerticalTransformation(img,viewWidth,viewHeight))).into(img);
    }

    public static void loadBlurImageHorizontal(ImageView img, String url, float radiusDp,
                                               int viewWidth,
                                               int viewHeight) {
        RoundedCorners roundedCorners = new RoundedCorners((int) radiusDp);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(img).load(url).placeholder(R.drawable.ic_launcher_background).apply(requestOptions.
                centerCrop().transform(roundedCorners,new BlurImageHorizontalTransformation(img,viewWidth,viewHeight))).into(img);
    }

    /**
     * Glide横向模糊图片转换类
     */
    static class BlurImageHorizontalTransformation extends BitmapTransformation {
        private static final String ID = "com.ljq.blurimage";
        private final byte[] ID_BYTES = ID.getBytes(CHARSET);

        private ImageView img;
        private int viewWidth;
        private int viewHeight;

        public BlurImageHorizontalTransformation(ImageView img,
                                                 int viewWidth,
                                                 int viewHeight) {
            this.img = img;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;

        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof BlurImageVerticalTransformation;
        }

        @Override
        public int hashCode() {
            return ID.hashCode();
        }


        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap = null;
            int imgw = img.getWidth() == 0 ? viewWidth : img.getWidth();
            int imgh = img.getHeight() == 0 ? viewHeight : img.getHeight();
            if (toTransform.getWidth() > toTransform.getHeight()) {//如果是横图不操作
                Log.d(TAG,"BlurImageHorizontalTransformation 111 bitmap_org.getConfig() " + toTransform.getConfig());
//                BitmapUtil.zoomImg(toTransform, imgw, imgh);
                bitmap = toTransform;
            }
            else {//如果是竖图就进行操作

                //1.原图进行裁剪做成背景图
                Bitmap bitmapBottom = BitmapUtil
                        .zoomImgCentCrop(toTransform, imgw,
                                imgh);
                Log.d(TAG,"BlurImageHorizontalTransformation 222 bitmapBottom.getConfig() " + bitmapBottom.getConfig());
                //2.把背景图模糊
                bitmapBottom = BitmapUtil
                        .blurBitmap(img.getContext(), bitmapBottom, BLUR_RADIUS);
                Bitmap bitmapTop;
                //3.算出图片比例
                float ratio = Float.valueOf(bitmapBottom.getHeight()) /
                        Float.valueOf(toTransform.getHeight());
                //4.缩放图片作为前景图
                bitmapTop = BitmapUtil.scaleBitmap(toTransform, ratio);
                //5.重叠图片
                bitmap = BitmapUtil
                        .overlayBitmapHorizontal(bitmapBottom, bitmapTop);
            }
            return bitmap;
        }
    }

    /**
     * Glide竖图模糊图片转换类
     */
    static class BlurImageVerticalTransformation extends BitmapTransformation {
        private static final String ID = "com.ljq.blurimage";
        private final byte[] ID_BYTES = ID.getBytes(CHARSET);

        private ImageView img;
        private int viewWidth;
        private int viewHeight;

        public BlurImageVerticalTransformation(ImageView img,
                                                 int viewWidth,
                                                 int viewHeight) {
            this.img = img;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;

        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof BlurImageVerticalTransformation;
        }

        @Override
        public int hashCode() {
            return ID.hashCode();
        }


        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap;
            int imgw = img.getWidth() == 0 ? viewWidth : img.getWidth();
            int imgh = img.getHeight() == 0 ? viewHeight : img.getHeight();
            if (toTransform.getWidth() < toTransform.getHeight()) {////如果是竖图不操作
                Log.d(TAG, "BlurImageVerticalTransformation 111 bitmapBottom.getConfig() " +
                        toTransform.getConfig());
//              BitmapUtil.zoomImgCentCrop(toTransform, imgw, imgh);
                bitmap = toTransform;
            }
            else {//如果是横图就进行操作

                //1.原图进行裁剪做成背景图
                Bitmap bitmapBottom = BitmapUtil
                        .zoomImgCentCrop(toTransform, imgw,
                                imgh);

                Log.d(TAG, "BlurImageVerticalTransformation 222 bitmapBottom.getConfig() " +
                        bitmapBottom.getConfig());
                //2.把背景图模糊
                bitmapBottom = BitmapUtil
                        .blurBitmap(img.getContext(), bitmapBottom,
                                BLUR_RADIUS);
                Bitmap bitmapTop;
                //3.算出图片比例
                float ratio =
                        Float.valueOf(bitmapBottom.getWidth()) /
                                Float.valueOf(toTransform.getWidth());
                //4.缩放图片作为前景图
                bitmapTop = BitmapUtil.scaleBitmap(toTransform, ratio);
                //5.重叠图片
                bitmap = BitmapUtil
                        .overlayBitmapVertical(bitmapBottom, bitmapTop);
            }
            return bitmap;
        }
    }
}
