package com.example.collegeadminapp.MenuOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeadminapp.MainActivity;
import com.example.collegeadminapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadEbook extends AppCompatActivity {
    private CardView addPDF;
   // private ImageView PDFImageView;
    private TextInputLayout pdfTitle;
    private Button uploadPDFButton;

    Uri imageuri = null;
    private String Title;
    Intent x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ebook);

        addPDF = findViewById(R.id.addPDFCV);
        pdfTitle = findViewById(R.id.pdfTitle);
        uploadPDFButton = findViewById(R.id.uploadPDFBtn);




        addPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(UploadEbook.this, "clicked", Toast.LENGTH_SHORT).show();
                 Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("application/pdf");

x=galleryIntent;
            }
        });



        uploadPDFButton = findViewById(R.id.uploadPDFBtn);

        uploadPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Title=pdfTitle.getEditText().getText().toString();


                startActivityForResult(x, 1);


            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            //dialog = new ProgressDialog(this);
            //dialog.setMessage("Uploading");

            // this will show message uploading
            // while pdf is uploading
            // dialog.show();
            imageuri = data.getData();
           // final String timestamp = "" + System.currentTimeMillis();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

          //  final String messagePushID = timestamp;

            Toast.makeText(UploadEbook.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

            // Here we are uploading the pdf in firebase storage with the name of current time

            final StorageReference filepath = storageReference.child("EBooks").child(Title + "." + "pdf");
            Toast.makeText(UploadEbook.this, filepath.getName(), Toast.LENGTH_SHORT).show();
            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {


                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        //  dialog.dismiss();
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();
                        Toast.makeText(UploadEbook.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        //dialog.dismiss();
                        Toast.makeText(UploadEbook.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }



}//last