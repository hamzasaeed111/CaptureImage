package com.example.captureimage.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class DetectedTextFragment extends Fragment
{
    String item,receipt_date,price;
    TextView mName,mCost,mDate;
    String homeFragmentTag = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_detected_text, container, false);

        // Receiving arguments from ViewFullSizeReceiptImage Fragment
        Bundle b = getArguments();
        item = b.getString("item_name");
        price = b.getString("price");
        receipt_date = b.getString("date");

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        mName = view.findViewById(R.id.material);
        mCost = view.findViewById(R.id.priceValue);
        mDate = view.findViewById(R.id.dateValue);

        mName.setText(item);
        mCost.setText(price);
        mDate.setText(receipt_date);
    }

/*    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0)
            {
                homeFragmentTag = backStateName; //     String homeFragmentTag = null; (Declare this at top of code, (after public class abc before onCreate method))
            }
            ft.replace(R.id.nav_host_fragment, fragment); //framelayout jo is java activity ki xml me declared hai. Most probably tmne bhi FrameLayout use kia hga, koi aur kia hua hai to btana.
            ft.addToBackStack(backStateName); // THIS LINE. YE LINE BACK PRESS KRNE SE PICHLE FRAGMENT ME JATI HAI.
            ft.commit();
        }
    }

    public void popFragmentsFromBackStack(Fragment fragment) {
        if (fragment.getClass().getName() == DetectedTextFragment.getClass().getName()) { //AGAR TO patientDashboardFragment wale fragment me hai already, to back naa jae, else back chala jae. to apni logic is hisab se change kr lena
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        popFragmentsFromBackStack(currentFragment);
    }*/
}