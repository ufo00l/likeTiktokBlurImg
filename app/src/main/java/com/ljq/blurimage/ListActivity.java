package com.ljq.blurimage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author :ljq
 * @des :  列表类
 * @DATA : 2022/4/9
 */
public class ListActivity extends AppCompatActivity {
    RecyclerView mRecyclerViewHor, mRecyclerViewVer;
    RecyclerView.Adapter mAdapterHor, mAdapterVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAdapterHor = new HorAdapter();
        mRecyclerViewHor = (RecyclerView) findViewById(R.id.rv1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewHor.setLayoutManager(linearLayoutManager);
        mRecyclerViewHor.setAdapter(mAdapterHor);

        mAdapterVer = new VerAdapter();
        mRecyclerViewVer = (RecyclerView) findViewById(R.id.rv2);
        mRecyclerViewVer.setLayoutManager(new GridLayoutManager(this,6));
        mRecyclerViewVer.setAdapter(mAdapterVer);

    }

    class HorAdapter extends RecyclerView.Adapter<HorAdapter.MyViewHolder>
    {
        int i = 0;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ListActivity.this).inflate(R.layout.list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            if(i++%2==0){
                BlurUtil.loadBlurImageHorizontal(holder.iv, MainActivity.PIC_ADDRESS_VERTICAL,15,holder.iv.getWidth(),holder.iv.getHeight());
            }else{
                BlurUtil.loadBlurImageHorizontal(holder.iv, MainActivity.PIC_ADDRESS_HORIZONTAL,15,holder.iv.getWidth(),holder.iv.getHeight());
            }
        }

        @Override
        public int getItemCount()
        {
            return 24;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView iv;

            public MyViewHolder(View view)
            {
                super(view);
                iv = (ImageView) view.findViewById(R.id.id_image);
            }
        }
    }

    class VerAdapter extends RecyclerView.Adapter<VerAdapter.MyViewHolder>
    {
        int i = 0;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ListActivity.this).inflate(R.layout.list_item2, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            if(i++%2==0){
                BlurUtil.loadBlurImageVertical(holder.iv, MainActivity.PIC_ADDRESS_VERTICAL,15,holder.iv.getWidth(),holder.iv.getHeight());
            }else{
                BlurUtil.loadBlurImageVertical(holder.iv, MainActivity.PIC_ADDRESS_HORIZONTAL,15,holder.iv.getWidth(),holder.iv.getHeight());
            }
        }

        @Override
        public int getItemCount()
        {
            return 48;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            ImageView iv;

            public MyViewHolder(View view)
            {
                super(view);
                iv = (ImageView) view.findViewById(R.id.id_image);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}