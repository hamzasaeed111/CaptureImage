package com.example.captureimage.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.captureimage.Classes.VendorDetails;
import com.example.captureimage.R;
import com.squareup.picasso.Picasso;

public class FragmentAdDetailPage extends Fragment
{
    TextView textViewType,textViewUnit,textViewPrice,textViewCity,textViewNum,textViewInfo,textViewVendorName;
    private String mType,mUnit,mPrice,mImg,mCity,mNum,mInfo,vendor_name;
    private VendorDetails materials = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_detail_page, container, false);
        //mPrice = getArguments().getString("data");
        materials  = (VendorDetails) getArguments().getSerializable("data");
        Log.e("Data received. ", materials.toString());
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mType = materials.getMaterialType();
        mUnit = materials.getMaterialUnit();
        mPrice = materials.getPrice();
        vendor_name = materials.getVendorName();
        mCity = materials.getCity();
        mNum = materials.getPhone();
        mInfo = materials.getAdInfo();
        mImg = materials.getImageURL();
        //Log.e("unit",mUnit);

        textViewType = (TextView) view.findViewById(R.id.material_name);
        textViewUnit = (TextView) view.findViewById(R.id.unit);
        textViewPrice = (TextView) view.findViewById(R.id.price);
        textViewVendorName = (TextView) view.findViewById(R.id.vendor_Name);
        textViewCity = (TextView) view.findViewById(R.id.vend_city);
        textViewNum = (TextView) view.findViewById(R.id.num);
        textViewInfo = (TextView) view.findViewById(R.id.info);

        textViewType.setText(mType);
        textViewUnit.setText(mUnit);
        textViewPrice.setText("$"+mPrice+" / ");
        textViewVendorName.setText(vendor_name);
        textViewCity.setText(mCity);
        textViewNum.setText(mNum);
        textViewInfo.setText(mInfo);

        ImageView imageholder=(ImageView)view.findViewById(R.id.largeimg);
        Picasso.with(getActivity()).load(mImg).into(imageholder);

        //setUserName();
    }

    /*public void setUserName() {
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        //FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        //fbAuth.getCurrentUser().getDisplayName();
        DatabaseReference refer = db.getReference().child("Parent").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Receiving: ",dataSnapshot.toString());
                Users user = dataSnapshot.getValue(Users.class);
                if(user!=null)
                vendor_name = user.getName();
                if(vendor_name!=null)
                    textViewVendorName.setText(vendor_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }*/
}
