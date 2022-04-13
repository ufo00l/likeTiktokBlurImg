package com.ljq.blurimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * @author :ljq
 * @des : 图片变形处理类
 * @DATA : 2022/4/9
 */
public class BitmapUtil {
    private final static String TAG = BitmapUtil.class.getSimpleName();

    /**
     * 用于做出cent_crop的效果
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImgCentCrop(Bitmap bm, int newWidth, int newHeight) {
        int w = bm.getWidth(); // 得到图片的宽，高
        int h = bm.getHeight();
        int retX;
        int retY;
        double wh = (double) w / (double) h;
        double nwh = (double) newWidth / (double) newHeight;
        if (wh > nwh) {
            retX = h * newWidth / newHeight;
            retY = h;
        }
        else {
            retX = w;
            retY = w * newHeight / newWidth;
        }
        int startX = w > retX ? (w - retX) / 2 : 0;//基于原图，取正方形左上角x坐标
        int startY = h > retY ? (h - retY) / 2 : 0;
        Bitmap bit = Bitmap.createBitmap(bm, startX, startY, retX, retY, null, false);
//        bm.recycle();
        return bit;
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius) {

        //如果图片本身是RGB_565 在有些手机系统上会模糊失败 这里需要统一转成RGB_888
        if (bitmap.getConfig() == Bitmap.Config.RGB_565) {
//            long start = System.currentTimeMillis();
            bitmap = rgb565to888(bitmap);
//            long end = System.currentTimeMillis();
//            Log.d(TAG," spend time 888 : "  +(end-start));
        }

        // 1.创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);

        // 2. 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间,创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation input = Allocation.createFromBitmap(rs, bitmap);


        //追查问题代码
//        Element e = input.getElement();

//        return ((mSize == e.mSize) &&
//                (mType != Element.DataType.NONE) &&
//                (mType == e.mType) &&
//                (mVectorSize == e.mVectorSize));

//        Element u84 = Element.U8_4(rs);
//        Element u8 = Element.U8(rs);

//        e.getBytesSize();
//        e.getDataType();
//        e.getVectorSize();

//        Log.d(TAG,
//                " e.getBytesSize() " + e.getBytesSize() + " e.getDataType() " + e.getDataType() + " e.getVectorSize() "+ e.getVectorSize() + " bitmap.getConfig() " + bitmap.getConfig());
//
//        Log.d(TAG,
//                " u84.getBytesSize() " + u84.getBytesSize() + " e.getDataType() " + u84.getDataType() + " e.getVectorSize() "+ u84.getVectorSize());
//
//        Log.d(TAG,
//                " u8.getBytesSize() " + u8.getBytesSize() + " u8.getDataType() " + u8.getDataType() + " e.getVectorSize() "+ u8.getVectorSize());

        // 3. 创建相同类型的Allocation对象用来输出
        Type type = input.getType();
        Allocation output = Allocation.createTyped(rs, type);

        // 4. 创建一个模糊效果的RenderScript的工具对象，第二个参数Element相当于一种像素处理的算法，高斯模糊的话用这个就好
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 5. 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);

//        output.getElement().isCompatible()

        // 6. 设置blurScript对象的输入内存
        blurScript.setInput(input);

        // 7. 将输出数据保存到输出内存中
        blurScript.forEach(output);

        // 8. 将数据填充到bitmap中
        output.copyTo(bitmap);

        // 9. 销毁它们释放内存
        input.destroy();
        output.destroy();
        blurScript.destroy();
        rs.destroy();

        //在Allocation会destroy掉 不用重复destroy 会报错
//        type.destroy();

        return bitmap;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        return newBM;
    }


    public static Bitmap overlayBitmapHorizontal(Bitmap background, Bitmap forward) {
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(background.getWidth(), background.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);
        // 在 0，0坐标开始画入bg

        Log.d(TAG, "overlayBitmap: background.getWidth() " + background.getWidth() + " forward.getWidth() " + forward.getWidth());

        // draw fg into
        cv.drawBitmap(forward, (background.getWidth() - forward.getWidth()) / 2, 0, null);

//        background.recycle();
//        forward.recycle();
        return newbmp;
    }

    public static Bitmap overlayBitmapVertical(Bitmap background, Bitmap forward) {
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(background.getWidth(), background.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(background, 0, 0, null);
        // 在 0，0坐标开始画入bg

        Log.d(TAG, "overlayBitmap: background.getHeight() " + background.getHeight() + " forward.getHeight() " + forward.getHeight());

        // draw fg into
        cv.drawBitmap(forward, 0, (background.getHeight() - forward.getHeight()) / 2, null);

//        background.recycle();
//        forward.recycle();
        return newbmp;
    }




    /**
     * 按比例低质量图片减少内存占用
     * @param bitmap
     * @param inSampleSize
     * @return
     */
    private static Bitmap simpleBitmap(Bitmap bitmap,int inSampleSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        Bitmap sample_bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                options);
//        bitmap.recycle();
//        bitmap = null;
        return sample_bitmap;
    }

    public static Bitmap rgb565to888(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap_888 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                options);
//        bitmap.recycle();
//        bitmap = null;
        return bitmap_888;
    }

}
