package com.example.captureimage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.captureimage.Adapters.Customlist;
import com.example.captureimage.R;
import com.example.captureimage.Classes.VendorDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.math3.stat.inference.OneWayAnova;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    Customlist custom;
    GridView listView;
    private FirebaseDatabase database;
    private   DatabaseReference ref;

    ArrayList<String> type = new ArrayList<>();
    ArrayList<String> unit = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> nameofVendor = new ArrayList<>();
    ArrayList<String> city = new ArrayList<>();
    ArrayList<String> num = new ArrayList<>();
    ArrayList<String> info = new ArrayList<>();

    ArrayList<String> key = new ArrayList<> ();

    public interface DataStatus{
        void DataIsLoaded(List<VendorDetails> vendor, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_knowledge_base, container, false);
        return root;
    }

    public void values() {
      database = FirebaseDatabase.getInstance();
         ref = database.getReference().child("Child");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    VendorDetails vendor = snapshot.getValue(VendorDetails.class);
                    try
                    {
                        type.add(vendor.getMaterialType());
                        unit.add(vendor.getMaterialUnit());
                        price.add(vendor.getPrice());
                        images.add(vendor.getImageURL());
                        nameofVendor.add(vendor.getVendorName());
                        city.add(vendor.getCity());
                        num.add(vendor.getPhone());
                        info.add(vendor.getAdInfo());
                        key.add (snapshot.getKey());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), "No vendors found", Toast.LENGTH_SHORT).show();
                    }
                }
                custom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        values();
        listView = (GridView) view.findViewById(R.id.listview);
        custom = new Customlist(getActivity(), type,unit, price, images,nameofVendor,city,num,info);
        listView.setAdapter(custom);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //fragment operation
        Fragment detail = new FragmentAdDetailPage();
        Bundle bundle = new Bundle();
        VendorDetails material = new VendorDetails(type.get(i),unit.get(i),price.get(i),nameofVendor.get(i),city.get(i),num.get(i),info.get(i),images.get(i));
        bundle.putSerializable("data",material);
        bundle.putString ("key",key.get (i));
        FragmentManager fm = getFragmentManager();
        detail.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detail);
        fragmentTransaction.commit();
    }
    public void deleteVendor(String key)
    {
        ref.child("Child").setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText (getContext (), "deleted", Toast.LENGTH_LONG).show ();
                    }
                });

    }
}