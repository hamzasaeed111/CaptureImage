package com.example.captureimage.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.captureimage.R;

public class CustomListForExpenseLog extends ArrayAdapter<String>
{
    private String[] names;
    private Bitmap[] bitmaps;
    private Activity context;

    public CustomListForExpenseLog(Activity context, String[] names, Bitmap[] bitmaps)
    {
        super(context, R.layout.expenselogslist, names);//sending R.layoutfile and one array is necessary here
        this.context= context;
        this.names= names;
        this.bitmaps= bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater= context.getLayoutInflater();
        View listViewItem= inflater.inflate(R.layout.expenselogslist, null, true);
        TextView textViewnames= (TextView) listViewItem.findViewById(R.id.name);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageDownloaded2);
        textViewnames.setText(names[position]);
        image.setImageBitmap(bitmaps[position]);
        return listViewItem;
    }
}
