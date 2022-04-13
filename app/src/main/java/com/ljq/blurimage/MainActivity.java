package com.ljq.blurimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author :ljq
 * @des :
 * @DATA : 2022/4/9
 */
public class MainActivity extends AppCompatActivity {
    //横图网址
    public static String PIC_ADDRESS_HORIZONTAL = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.yhsport.com%2Fdata%2Fupload%2Fimage%2F20200528%2F1590634830974906.jpg&refer=http%3A%2F%2Fwww.yhsport.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1646044342&t=7743dbf24a863c141b45526b856ffc75";
    //竖图网址
    public static String PIC_ADDRESS_VERTICAL = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic3.zhimg.com%2F50%2Fv2-5ce9c04caf8a6875299912d0b30e3b6a_hd.jpg&refer=http%3A%2F%2Fpic3.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1649408053&t=af0563051c4767f3c54f1f7d901cd019";

    //原图
    ImageView original;
    //横向模糊
    ImageView hor_blur;
    //竖向模糊
    ImageView ver_blur;

    //设置横图
    Button set_hor;
    //设置竖图
    Button set_ver;
    //前往列表页面
    Button go_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        original = findViewById(R.id.original);
        hor_blur = findViewById(R.id.hor_blur);
        ver_blur = findViewById(R.id.ver_blur);
        set_hor = findViewById(R.id.button);
        set_ver = findViewById(R.id.button2);
        go_list = findViewById(R.id.button1);



        set_hor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlurUtil.loadImage(original, PIC_ADDRESS_HORIZONTAL);
                BlurUtil.loadBlurImageHorizontal(hor_blur, PIC_ADDRESS_HORIZONTAL,15,hor_blur.getWidth(),hor_blur.getHeight());
                BlurUtil.loadBlurImageVertical(ver_blur, PIC_ADDRESS_HORIZONTAL,15,ver_blur.getWidth(),ver_blur.getHeight());
            }
        });

        set_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlurUtil.loadImage(original, PIC_ADDRESS_VERTICAL);
                BlurUtil.loadBlurImageHorizontal(hor_blur, PIC_ADDRESS_VERTICAL,15,hor_blur.getWidth(),hor_blur.getHeight());
                BlurUtil.loadBlurImageVertical(ver_blur, PIC_ADDRESS_VERTICAL,15,ver_blur.getWidth(),ver_blur.getHeight());
            }
        });

        go_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}