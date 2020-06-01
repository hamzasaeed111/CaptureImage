package com.example.captureimage.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captureimage.Classes.ImageUrlSaved;
import com.example.captureimage.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ViewFullSizeReceiptImage extends Fragment
{
    private ImageUrlSaved receipt = null;
    ImageView deleteBtn;
    Button OCR;
    private TextView scanResults;
    private TextRecognizer detector;
    TextView confirmationText;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    ArrayList<String> arr;
    String itemName,date,cost,mImg,selectedItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        receipt  = (ImageUrlSaved) getArguments().getSerializable("data");
        detector = new TextRecognizer.Builder(getContext()).build();
        // Receiving arguments from ReceiptImagesPage Fragment
        Bundle b = getArguments();
        selectedItem = b.getString("item");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_full_size_receipt_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        deleteBtn = view.findViewById(R.id.delete);
        scanResults = view.findViewById(R.id.detectedTextFromImage);
        confirmationText = view.findViewById(R.id.userInputDialog);
        OCR = view.findViewById(R.id.ocr);

        mImg = receipt.getImageURL();
        ImageView imageholder=(ImageView)view.findViewById(R.id.fullsizeimg);
        Picasso.with(getActivity()).load(mImg).into(imageholder);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogBoxForDeleteConfirmation();
            }
        });
        OCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                load_OCR_Fragment();
            }
        });
    }

    private void dialogBoxForDeleteConfirmation()
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialogBox, int id)
                    {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("Receipts").child(selectedItem).orderByChild("imageURL").equalTo(mImg);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), "The image cannot be deleted. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialogBox, int id)
                            {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void load_OCR_Fragment()
    {
        Picasso.with(getActivity()).load(mImg).into(new Target()
        {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from)
            {
                if (detector.isOperational() && bitmap != null)
                {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    arr = new ArrayList<String>();
                    String blocks = "";

                    for (int index = 0; index < textBlocks.size(); index++)
                    {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";

                        for (Text line : tBlock.getComponents())
                        {
                            //extract scanned text lines here and insert each line into array
                            arr.add(line.getValue());
                        }
                    }

                    algorithmForFilteringText();

                    if (textBlocks.size() == 0)
                        scanResults.setText("Scan Failed: Found nothing to scan");
                    else
                    {
                        scanResults.setText(scanResults.getText() + "Lines: " + "\n");
                        scanResults.setText(scanResults.getText() + "---------" + "\n");
                        scanResults.setText(scanResults.getText() + String.valueOf(arr) + "\n");
                        scanResults.setText(scanResults.getText() + date);

                        // Adding data to the bundle class
                        Bundle b = new Bundle();
                        b.putString("date", date);
                        b.putString("item_name", itemName);
                        b.putString("price", cost);

                        //Fragment operation
                        Fragment receipts = new DetectedTextFragment();
                        FragmentManager fm = getFragmentManager();
                        receipts.setArguments(b);
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, receipts);
                        fragmentTransaction.commit();
                    }
                }

                else
                {
                    scanResults.setText("Could not set up the detector!");
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable)
            {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable)
            {

            }
        });
    }

    private void algorithmForFilteringText()
    {
        for(int i = 0; i < arr.size(); i++)
        {
            if(arr.get(i).startsWith("*"))
                arr.remove(i);

            /*if(arr.get(i).contains("Cement"))
                itemName = arr.get(i);*/
        }

        arr.remove(0);
        arr.remove(arr.size() - 1);
        arr.remove(arr.size() - 1);

        switch(arr.get(1))
        {
            case "Cement":
                itemName = arr.get(1);
                break;
            case "Iron Bars":
                itemName = arr.get(1);
                break;
            case "Coarse Aggregate":
                itemName = arr.get(1);
                break;
            case "Sand":
                itemName = arr.get(1);
                break;
            case "Bricks":
                itemName = arr.get(1);
                break;
            case "Clay Blocks":
                itemName = arr.get(1);
                break;
            case "Marble":
                itemName = arr.get(1);
                break;
            case "Paint":
                itemName = arr.get(1);
                break;
            default:
                Toast.makeText(getContext(), "No item found", Toast.LENGTH_SHORT).show();
                break;
        }

        String d = arr.get(0);
        date = d.substring(6,16);

        cost = arr.get(arr.size() - 1);
    }
}


