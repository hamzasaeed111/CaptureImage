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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.captureimage.Classes.ImageUrlSaved;
import com.example.captureimage.Adapters.CustomListForRecieptImages;
import com.example.captureimage.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class RecietImagesPage extends Fragment implements AdapterView.OnItemClickListener {
    CustomListForRecieptImages custom;
    GridView gridView;
    DatabaseReference ref;
    private StorageReference mStorage;
    private static ProgressDialog mProgressDialog = null;
    public static Bitmap bmp = null;
    private static Uri selectedImageUri = null;
    private static String urlpath = null;
    public static ImageUrlSaved savedUrl = null;
    int position = 0;
    String itemClicked;
    Button generatePopupBtn;

    ArrayList<String> images = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_receipt_images_page, container, false);

        // Receiving arguments from UploadReceiptImages Fragment
        Bundle b = getArguments();
        position = b.getInt("index");
        itemClicked = b.getString("clickedItem");

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        values();
        generatePopupBtn = view.findViewById(R.id.alertBoxForPic);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());
        gridView = (GridView) view.findViewById(R.id.listviewForRcieptImages);
        custom = new CustomListForRecieptImages(getActivity(),images);
        gridView.setAdapter(custom);
        gridView.setOnItemClickListener(this);
        generatePopupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogBox();
            }
        });
    }

    public void values()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        switch (position)
        {
            case 0:
                ref = database.getReference().child("Receipts").child("Cement");
                break;
            case 1:
                ref = database.getReference().child("Receipts").child("Iron Bars");
                break;
            case 2:
                ref = database.getReference().child("Receipts").child("Coarse Aggregate");
                break;
            case 3:
                ref = database.getReference().child("Receipts").child("Sand");
                break;
            case 4:
                ref = database.getReference().child("Receipts").child("Bricks");
                break;
            case 5:
                ref = database.getReference().child("Receipts").child("Clay Blocks");
                break;
            case 6:
                ref = database.getReference().child("Receipts").child("Marble");
                break;
            case 7:
                ref = database.getReference().child("Receipts").child("Paint");
                break;
            default:
                System.out.println("Error");
                break;
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //images.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ImageUrlSaved path = snapshot.getValue(ImageUrlSaved.class);
                    try
                    {
                        images.add(path.getImageURL());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), "No images found", Toast.LENGTH_SHORT).show();
                    }
                }
                custom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to retrieve data. Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dialogBox() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Capturing Image from Camera
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bmp = (Bitmap) data.getExtras().get("data");

            if (bmp != null) {
                //Compressing image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream); //Compress Bitmap
                byte[] image = stream.toByteArray(); //Get the Bytes

                //Progess Message shows image is attempting to upload
                mProgressDialog.setMessage("Saving  Image");
                mProgressDialog.show();

                //Upload image to VendorsProfilePictures Folder
                StorageReference filePath = mStorage.child("Billing_Receipts").child(String.valueOf(new Date().getTime()) + ".png");
                filePath.putBytes(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    //On Successfully Uploaded
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get Download Url of Picture
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
                        urlpath = url.toString();
                        upload();
                    }
                });
            }
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

            if (selectedImageUri != null) {

                //Progess Message shows image is attempting to upload
                mProgressDialog.setMessage("Saving Image");
                mProgressDialog.show();

                //Upload selected file to VendorsProfilePictures Folder
                StorageReference filePath = mStorage.child("Billing_Receipts").child(selectedImageUri.getLastPathSegment());
                filePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    //On Successfully Uploaded
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();
                        urlpath = url.toString();
                        upload();
                    }
                });
            }
        }
    }

    //Upload receipt images to firebase database
    private void upload()
    {
        savedUrl = new ImageUrlSaved();
        // Adding image url into class function object.
        savedUrl.setImageURL(urlpath);

        switch (position) {
            case 0:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Cement")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 1:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Iron Bars")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 2:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Coarse Aggregate")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 3:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Sand")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 4:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Bricks")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 5:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Clay Blocks")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 6:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Marble")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            case 7:
                FirebaseDatabase.getInstance().getReference("Receipts")
                        .child("Paint")
                        .child("Photo" + String.valueOf(new Date().getTime()))
                        .setValue(savedUrl);
                break;
            default:
                System.out.println("Error");
                break;
        }

        mProgressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //fragment operation
        Fragment detail = new ViewFullSizeReceiptImage();
        ImageUrlSaved receipt = new ImageUrlSaved(images.get(i));
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",receipt);
        bundle.putString("item",itemClicked);
        FragmentManager fm = getFragmentManager();
        detail.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detail);
        fragmentTransaction.commit();
    }
}