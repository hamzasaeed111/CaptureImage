package com.example.captureimage.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.captureimage.R;
import com.example.captureimage.Classes.VendorDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import static android.app.Activity.RESULT_OK;

public class UploadAdFragment extends Fragment implements View.OnClickListener
{
    private StorageReference mStorage;
    DatabaseReference databaseReference;
    private static ProgressDialog mProgressDialog = null;
    private static String urlpath = null;
    private static String materialType = null;
    private static String materialUnit = null;
    public static Bitmap bmp = null;
    private static Uri selectedImageUri = null;
    public static VendorDetails details = null;

    Button submitBtn;
    ImageView I1;
    TextInputEditText PriceEditText, AdInfoEditText, CityEditText, PhoneEditText,VendorNameEditText;

    // Declaring String variables to store name & phone number get from EditText.
    String PriceHolder, CityHolder, AdInfoHolder, PhoneHolder,VendorNameHolder;
    Spinner materialSpinner,unitSpinner;
    private HintSpinner<String> materialHintSpinner,unitHintSpinner;
    private List<String> materials,units;
    String material_item = "";
    String unit_item = "";

    // Root Database Name for Firebase Database.
    String Database_Path = "Vendor_Ad_Details_Database";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_ad, container, false);
        // Hint Spinner using default provided layout
        return root;
    }

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());
        submitBtn = (Button) getActivity().findViewById(R.id.submitAd);
//        okBtn = (Button) getActivity().findViewById(R.id.submit);

        materialSpinner = (Spinner) getActivity().findViewById(R.id.spinnerForMaterial);
        unitSpinner = (Spinner) getActivity().findViewById(R.id.spinnerForUnit);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        PriceEditText = getActivity().findViewById(R.id.rateOfMaterial);
        AdInfoEditText = getActivity().findViewById(R.id.description);
        CityEditText = (TextInputEditText) getActivity().findViewById(R.id.city);
        VendorNameEditText = (TextInputEditText) getActivity().findViewById(R.id.vend_name);
        PhoneEditText = (TextInputEditText) getActivity().findViewById(R.id.contact);

        I1 = (ImageView) getActivity().findViewById(R.id.cam);
        I1.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
//        okBtn.setOnClickListener(this);

        initDefaultHintSpinner();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == I1.getId()) {
            dialogBox();

        } else if (view.getId() == submitBtn.getId()) {
            PriceHolder = PriceEditText.getText().toString();
            AdInfoHolder = AdInfoEditText.getText().toString();
            CityHolder = CityEditText.getText().toString();
            VendorNameHolder = VendorNameEditText.getText().toString();
            PhoneHolder = PhoneEditText.getText().toString();

            if (material_item.isEmpty())
            {
                Snackbar snack;
                snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please select any material name from list", Snackbar.LENGTH_LONG);
                snack.show();
            }
            else if (unit_item.isEmpty())
            {
                Snackbar snack;
                snack = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please select material unit from list", Snackbar.LENGTH_LONG);
                snack.show();
            }
            else if (PriceHolder.length() == 0)
                PriceEditText.setError("Please enter material rate");
            else if (CityHolder.length() == 0)
                CityEditText.setError("Please enter your city");
            else if (VendorNameHolder.length() == 0)
                VendorNameEditText.setError("Please enter your name");
            else if (PhoneHolder.length() == 0)
                PhoneEditText.setError("Please enter your contact number");
            else if (AdInfoHolder.length() == 0)
                AdInfoEditText.setError("Please enter Ad description");
            else if (selectedImageUri == null && bmp == null)
            {
                Snackbar snackbar1;
                snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please insert picture of your material for sale", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }

            else {
                if(bmp != null) {
                    //Compressing image
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress Bitmap
                    byte[] data = stream.toByteArray(); //Get the Bytes

                    //Progess Message shows image is attempting to upload
                    mProgressDialog.setMessage("Uploading Ad");
                    mProgressDialog.show();

                    //Upload image to VendorsProfilePictures Folder
                    StorageReference filePath = mStorage.child("VendorsProfilePictures").child(String.valueOf(new Date().getTime()) + ".png");
                    filePath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //On Successfully Uploaded
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            //Get Download Url of Picture
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            urlpath = url.toString();
                            upload();
                        }
                    });
                }

                if(selectedImageUri != null) {

                    //Progess Message shows image is attempting to upload
                    mProgressDialog.setMessage("Uploading Ad");
                    mProgressDialog.show();

                    //Upload selected file to VendorsProfilePictures Folder
                    StorageReference filePath = mStorage.child("VendorsProfilePictures").child(selectedImageUri.getLastPathSegment());
                    filePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        //On Successfully Uploaded
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete());
                            Uri url = uri.getResult();
                            urlpath = url.toString();
                            upload();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturing Image from Camera
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bmp = (Bitmap) data.getExtras().get("data");
            I1.setImageBitmap(bmp);
        }

        //Selecting Image from Gallery
        if (requestCode == 2 && resultCode == RESULT_OK) {

            selectedImageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            I1.setImageBitmap(bitmap);
        }
    }

    //Upload vendor details to firebase database
    private void upload()
    {
        details = new VendorDetails();
        // Adding vendor ad details into class function object.
        details.setMaterialType(materialType);
        details.setMaterialUnit(materialUnit);
        details.setPrice(PriceHolder);
        details.setCity(CityHolder);
        details.setVendorName(VendorNameHolder);
        details.setPhone(PhoneHolder);
        details.setAdInfo(AdInfoHolder);
        details.setImageURL(urlpath);

        FirebaseDatabase.getInstance().getReference("Child")
                .child(materialType + " "+ String.valueOf(new Date().getTime()))
                .setValue(details);

        mProgressDialog.dismiss();
        PriceEditText.getText().clear();
        CityEditText.getText().clear();
        VendorNameEditText.getText().clear();
        PhoneEditText.getText().clear();
        AdInfoEditText.getText().clear();
        materialSpinner.setSelection(Adapter.NO_SELECTION);
        unitSpinner.setSelection(Adapter.NO_SELECTION);
    }

    private void initDefaultHintSpinner() {
        materials = new ArrayList<>();
        materials.add("Cement");
        materials.add("Iron Bars");
        materials.add("Coarse Aggregate");
        materials.add("Sand");
        materials.add("Bricks");
        materials.add("Clay Blocks");
        materials.add("Marble");
        materials.add("Tiles");
        materials.add("Paint");

        materialHintSpinner = new HintSpinner<>(
                materialSpinner,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(getActivity(), getString(R.string.default_spinner_hint), materials),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected
                        // event)
                        showMaterialSelectedItem(itemAtPosition);
                    }
                });

        units = new ArrayList<>();
        units.add("cm");
        units.add("km");
        units.add("kg");
        units.add("g");
        units.add("m");
        units.add("Sq. m");
        units.add("Sq. ft.");
        units.add("mg");
        units.add("inch");
        units.add("foot");
        units.add("ton");

        unitHintSpinner = new HintSpinner<>(
                unitSpinner,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(getActivity(), getString(R.string.spinnerunittitle), units),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected
                        // event)
                        showUnitSelectedItem(itemAtPosition);
                    }
                });

        unitHintSpinner.init();
        materialHintSpinner.init();
    }

    private void showMaterialSelectedItem(String itemAtPosition)
    {
        materialType = itemAtPosition;
        material_item = itemAtPosition;
        Toast.makeText(getActivity(), "Selected Material Name = " + material_item, Toast.LENGTH_SHORT).show();
        //defaultSpinner.setPadding(150,-2,0,-2);
    }

    private void showUnitSelectedItem(String itemAtPosition)
    {

        materialUnit = itemAtPosition;
        unit_item = itemAtPosition;
        Toast.makeText(getActivity(), "Selected unit = " + unit_item, Toast.LENGTH_SHORT).show();
    }

    // Alert dialog for choosing picture source
    public void dialogBox()
    {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    //captureImage
                    Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(picIntent, 1);
                } else if (items[item].equals("Choose from Library")) {
                    //choose from gallery
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}