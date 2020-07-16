package com.example.captureimage.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captureimage.Classes.VendorDetails;
import com.example.captureimage.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FragmentAdDetailPage extends Fragment
{
    private FirebaseDatabase database;
    private DatabaseReference ref;
    TextView textViewType,textViewUnit,textViewPrice,textViewCity,textViewNum,textViewInfo,textViewVendorName, nameHeading, cityHeading, phoneHeading;
    private String mType,mUnit,mPrice,mImg,mCity,mNum,mInfo,vendor_name,key;
    private VendorDetails materials = null;
    private Button btnUpdate;
    private Button btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Child");
        View root = inflater.inflate(R.layout.fragment_detail_page, container, false);
        //mPrice = getArguments().getString("data");
        materials  = (VendorDetails) getArguments().getSerializable("data");
        key = getArguments().getString("key");
        Log.e("Data received. ", materials.toString());
        // Inflate the layout for this fragment
        return root;
    }
    public void deleteVendor(String key)
    {
        ref.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText (getContext (), "deleted", Toast.LENGTH_LONG).show ();
                    }
                });

    }
    public void updateVendor(String key, VendorDetails vendor)
    {
        ref.child(key).setValue(vendor)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText (getContext (), "Updated", Toast.LENGTH_LONG).show ();
                    }
                });

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
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


//        nameHeading = (TextView) view.findViewById(R.id.name_heading);
//        phoneHeading = (TextView) view.findViewById(R.id.num_heading);
//        cityHeading = (TextView) view.findViewById(R.id.city_heading);

        textViewType.setText(mType);
        textViewUnit.setText(mUnit);
        textViewPrice.setText("$"+mPrice+" / ");
        textViewVendorName.setText(vendor_name);
        textViewCity.setText(mCity);
        textViewNum.setText(mNum);
        textViewInfo.setText(mInfo);

        btnUpdate = (Button)view.findViewById(R.id.btnUpdate);
        btnDelete = (Button)view.findViewById(R.id.btnDelete);

        btnUpdate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (((Button)v).getText().toString()=="UPDATE")
                {
                    ((Button) v).setText ("OK");
                    textViewVendorName.getLayoutParams ().width=330;
                    textViewVendorName.getLayoutParams ().height=50;
                    textViewVendorName.setCursorVisible(true);
                    textViewVendorName.setFocusableInTouchMode(true);
                    textViewVendorName.setInputType(InputType.TYPE_CLASS_TEXT);
                    textViewVendorName.requestFocus();

                    textViewCity.getLayoutParams ().width=330;
                    textViewCity.getLayoutParams ().height=50;
                    textViewCity.setCursorVisible(true);
                    textViewCity.setFocusableInTouchMode(true);
                    textViewCity.setInputType(InputType.TYPE_CLASS_TEXT);
                    textViewCity.requestFocus();

                    textViewNum.getLayoutParams ().width=330;
                    textViewNum.getLayoutParams ().height=50;
                    textViewNum.setCursorVisible(true);
                    textViewNum.setFocusableInTouchMode(true);
                    textViewNum.setInputType(InputType.TYPE_CLASS_TEXT);
                    textViewNum.requestFocus();

                    btnDelete.setText ("CANCEL");




                }
                else if (((Button)v).getText().toString()=="OK")
                {
                    VendorDetails vd = new VendorDetails (mType, mUnit, mPrice, textViewCity.getText ().toString (), textViewVendorName.getText ().toString (), textViewNum.getText ().toString (), mInfo, mImg);
                    updateVendor (key,vd);

                }

            }
        });

        btnDelete.setOnClickListener (new View.OnClickListener ( ) {
                                          @Override
                                          public void onClick(View v) {
                                              if(((Button)v).getText().toString()=="DELETE")
                                              {
                                                  deleteVendor (key);
                                              }
                                              else if(((Button)v).getText().toString()=="CANCEL")
                                              {
                                                  btnUpdate.setText ("UPDATE");
                                                  getActivity().getSupportFragmentManager().beginTransaction().replace(FragmentAdDetailPage.this.getId(), new FragmentAdDetailPage ()).commit();
                                              }
                                              else
                                              {
                                                  btnUpdate.setText ("UPDATE");
                                                  getActivity().getSupportFragmentManager().beginTransaction().replace(FragmentAdDetailPage.this.getId(), new FragmentAdDetailPage ()).commit();

                                              }


                                          }
                                      });

                ImageView imageholder = (ImageView) view.findViewById (R.id.largeimg);
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
