package com.example.collegeadminapp.MenuOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeadminapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addImage;
    private final int REQ=28;
    private Bitmap bitmap;
    private ImageView noticeImageView;
    private TextInputLayout noticeTitle;
    private Button uploadNoticeButton;

String downloadURL="";

private ProgressDialog pd;

    private DatabaseReference reference;
    private StorageReference storageReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);


        addImage = findViewById(R.id.addImageCV);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        uploadNoticeButton = findViewById(R.id.uploadNoticeBtn);


        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        pd=new ProgressDialog(this);


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });




        uploadNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value=noticeTitle.getEditText().getText().toString();

                if(value.isEmpty())
                {
                    //noticeTitle.setErrorEnabled(true);
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                }
                else if (bitmap==null){
                    Toast.makeText(UploadNotice.this, "Please select Image", Toast.LENGTH_SHORT).show();

                    uploadData();
                }
                else{
                    uploadImage();

                }


            }
        });







    }

    private void uploadImage() {

        pd.setMessage("Uploading...");
        pd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();

        final StorageReference filePath;
        filePath = storageReference.child("Notice").child(finalImage+"jpg");
        final UploadTask  uploadTask= filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadURL = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }



                else {
                    pd.dismiss();
                    Toast.makeText(UploadNotice.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadData() {


        reference =reference.child("Notice");

        final String uniqueKey  = reference.push().getKey();

        String title = noticeTitle.getEditText().getText().toString();

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForDate.getTime());


        NoticeData noticeData = new NoticeData(title,downloadURL,date,time,uniqueKey);

        reference.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();

                Toast.makeText(UploadNotice.this, "Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });





    }




    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            noticeImageView.setImageBitmap(bitmap);
        }



    }
}