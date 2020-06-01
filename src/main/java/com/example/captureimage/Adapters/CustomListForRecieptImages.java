package com.example.captureimage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.captureimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListForRecieptImages extends ArrayAdapter<String>
{
    private ArrayList<String> image;
    private Activity context;

    public CustomListForRecieptImages(Activity context,ArrayList<String> image)
    {
        super(context, R.layout.row_for_receipts_images,image);
        this.context = context;
        this.image = image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.row_for_receipts_images, null, true);

        TextView textViewUrl = (TextView) listViewItem.findViewById(R.id.imgurl);
        ImageView imageholder = (ImageView) listViewItem.findViewById(R.id.listimage);
        String i = image.get(position);
        Picasso.with(context).load(i).into(imageholder);

        return listViewItem;
    }
}
