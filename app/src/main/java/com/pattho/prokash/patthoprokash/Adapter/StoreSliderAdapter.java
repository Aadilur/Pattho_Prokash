package com.pattho.prokash.patthoprokash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pattho.prokash.patthoprokash.R;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoreSliderAdapter extends
        SliderViewAdapter<SliderVH> {

     Context context;
     List<String> mSliderItems = new ArrayList<>();

    public StoreSliderAdapter(Context context) {
    }

    public StoreSliderAdapter(Context context, List<String> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderVH onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_store_slider,null);
        return new SliderVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderVH viewHolder, int position) {
        String sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem)
                .fitCenter()
                .into(viewHolder.imageViewBackground);


    }


    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }



}


class SliderVH extends SliderViewAdapter.ViewHolder {


    public View itemView;
    public ImageView imageViewBackground;
    public   ImageView imageGifContainer;
    public TextView textViewDescription;

    public SliderVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
        textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
        this.itemView = itemView;
    }
}