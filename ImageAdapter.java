package com.example.raja.testurlconnection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raja on 2/3/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

        private ArrayList<ImageInfo> imageList;

        public ImageAdapter(ArrayList<ImageInfo> imageList) {
                    this.imageList = imageList;
                }

        @Override
        public int getItemCount() {
                    return imageList.size();
                }

        @Override
        public void onBindViewHolder(ImageViewHolder imageViewHolder, int i) {
                    ImageInfo ii = imageList.get(i);
                    imageViewHolder.title.setText("Image " + ii);
                    imageViewHolder.imageView.setImageBitmap(ii.bmImage);
                }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
                    return new ImageViewHolder(itemView);
                }

        public static class ImageViewHolder extends RecyclerView.ViewHolder{
            protected TextView title;
            protected ImageView imageView;

            public ImageViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.imageTitle);
                imageView = (ImageView) v.findViewById(R.id.imgViews);
            }
        }

}
