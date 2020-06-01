package com.example.captureimage.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.captureimage.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Customlist extends ArrayAdapter<String>
{
    private ArrayList<String> type;
    private ArrayList<String> unit;
    private ArrayList<String> price;
    private ArrayList<String> image;
    private ArrayList<String> vname;
    private ArrayList<String> city;
    private ArrayList<String> num;
    private ArrayList<String> info;
    private Activity context;

    public Customlist(Activity context, ArrayList<String> type, ArrayList<String> unit,ArrayList<String> price, ArrayList<String> image,
                      ArrayList<String> vname,ArrayList<String> city,ArrayList<String> num,ArrayList<String> info)
    {
        super(context, R.layout.row, type);
        this.context = context;
        this.type = type;
        this.unit = unit;
        this.price = price;
        this.image = image;
        this.vname = vname;
        this.city = city;
        this.num = num;
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.row, null, true);

        TextView textViewType = (TextView) listViewItem.findViewById(R.id.material_name);
        TextView textViewUnit = (TextView) listViewItem.findViewById(R.id.unit);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.priceofMaterial);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.v_name);
        TextView textViewCity = (TextView) listViewItem.findViewById(R.id.cityofVendor);
        TextView textViewNum = (TextView) listViewItem.findViewById(R.id.num);
        TextView textViewInfo = (TextView) listViewItem.findViewById(R.id.info);
        TextView textViewUrl = (TextView) listViewItem.findViewById(R.id.imgurl);

        ImageView imageholder=(ImageView)listViewItem.findViewById(R.id.listimage);
        String i=image.get(position);
        Picasso.with(context).load(i).into(imageholder);

        textViewType.setText(type.get(position));
        textViewUnit.setText(unit.get(position));
        textViewPrice.setText("Rate: Rs. "+price.get(position));
        textViewUrl.setText(image.get(position));
        textViewName.setText(vname.get(position));
        textViewCity.setText(city.get(position));
        textViewNum.setText(num.get(position));
        textViewInfo.setText(info.get(position));

        return listViewItem;
    }
}
