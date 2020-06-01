package com.example.captureimage.Fragments;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.captureimage.Adapters.CustomListForExpenseLog;
import com.example.captureimage.Classes.ImageUrlSaved;
import com.example.captureimage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ExpenseLogFragment extends Fragment implements AdapterView.OnItemClickListener
{
    ListView list;
    CustomListForExpenseLog customList;
    int position = 0;
    DatabaseReference ref;
    private StorageReference mStorage;
    String selected;

//    String itemList[] = {"Cement",
//            "Iron Bars",
//            "Coarse Aggregate",
//            "Sand",
//            "Bricks",
//            "Clay Blocks",
//            "Marble",
//            "Paint"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_expense_log, container, false);
        mStorage = FirebaseStorage.getInstance().getReference();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Bitmap[] icons = new Bitmap[8];
        icons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.cement);
        icons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ironbar1);
        icons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.coarseaggregates);
        icons[3] = BitmapFactory.decodeResource(getResources(), R.drawable.venice_hauling_beach_sand);
        icons[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bricks);
        icons[5] = BitmapFactory.decodeResource(getResources(), R.drawable.clayblocks);
        icons[6] = BitmapFactory.decodeResource(getResources(), R.drawable.marble);
        icons[7] = BitmapFactory.decodeResource(getResources(), R.drawable.paint7);

        String[] names = {"Cement", "Iron Bars", "Coarse Aggregate", "Sand","Bricks","Clay Blocks", "Marble","Paint"};
        list = (ListView) view.findViewById(R.id.listView);
        customList = new CustomListForExpenseLog(getActivity(), names, icons);
        list.setAdapter(customList);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        selected = ((TextView) view.findViewById(R.id.name)).getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        switch (i) {
            case 0:
                ref = database.getReference().child("Receipts").child("Cement");
//                ref = database.getReference().child("Receipts").child(itemList[0]);
                break;
            case 1:
                position = 1;
                ref = database.getReference().child("Receipts").child("Iron Bars");
                break;
            case 2:
                position = 2;
                ref = database.getReference().child("Receipts").child("Coarse Aggregate");
                break;
            case 3:
                position = 3;
                ref = database.getReference().child("Receipts").child("Sand");
                break;
            case 4:
                position = 4;
                ref = database.getReference().child("Receipts").child("Bricks");
                break;
            case 5:
                position = 5;
                ref = database.getReference().child("Receipts").child("Clay Blocks");
                break;
            case 6:
                position = 6;
                ref = database.getReference().child("Receipts").child("Marble");
                break;
            case 7:
                position = 7;
                ref = database.getReference().child("Receipts").child("Paint");
                break;
            default:
                System.out.println("Error");
                break;
        }
        loadFragment();
    }

    public void loadFragment() {
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Adding data to the bundle class
                Bundle b = new Bundle();
                b.putInt("index", position);
                b.putString("clickedItem", selected);

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ImageUrlSaved path = snapshot.getValue(ImageUrlSaved.class);
                        try {
                            if (path != null)
                            {
                                //Fragment operation
                                Fragment receipts = new  RecietImagesPage();
                                FragmentManager fm = getFragmentManager();
                                receipts.setArguments(b);
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, receipts);
                                fragmentTransaction.commit();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "No vendors found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    //Fragment operation
                    Fragment receipts = new UploadReceiptImages();
                    FragmentManager fm = getFragmentManager();
                    receipts.setArguments(b);
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, receipts);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}