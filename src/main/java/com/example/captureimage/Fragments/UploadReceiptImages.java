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
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.captureimage.Classes.ImageUrlSaved;
import com.example.captureimage.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import static android.app.Activity.RESULT_OK;

public class UploadReceiptImages extends Fragment {
    private StorageReference mStorage;
    private static ProgressDialog mProgressDialog = null;
    public static Bitmap bmp = null;
    private static Uri selectedImageUri = null;
    private static String urlpath = null;
    public static ImageUrlSaved savedUrl = null;
    int position = 0;
    Button generatePopupBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_receipt_image, container, false);
        generatePopupBtn = view.findViewById(R.id.alertBox);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());

        generatePopupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogBox();
            }
        });

        // Receiving arguments from ExpenseLogFragment
        Bundle b = getArguments();
        position = b.getInt("index");

        return view;
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
    private void upload() {
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
}
