package com.example.b07_project_group5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class uploadimageactivity extends AppCompatActivity {
    private ImageView profilePic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button chooseImageBtn;
    private Button uploadImageBtn;
    public Uri imageUri;

    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productimageupload);
        profilePic = findViewById(R.id.firebaseimageuploader);
        chooseImageBtn = findViewById(R.id.selectimagebtn);
        uploadImageBtn = findViewById(R.id.uploadimagebtn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
            }
        });
    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    imageUri = result.getData().getData();
                    profilePic.setImageURI(imageUri);
                }
            });

    private void uploadPicture() {
        if (imageUri != null) {
            // Create a reference to the image file you want to upload in the "ProductImages/" directory
            StorageReference imageRef = storageReference.child("ProductImages/" + System.currentTimeMillis() + ".jpg");

            // Upload the file to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image upload successfully
                        // You can get the download URL of the uploaded image using taskSnapshot.getDownloadUrl()
                        // For example, to get the download URL:
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Toast.makeText(uploadimageactivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                            profilePic.setImageDrawable(null);
                            imageUri = null;
                        });
                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(uploadimageactivity.this, "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Handle the case when no image is selected (optional)
            // You may want to show a message to the user or perform other actions as needed.
            Toast.makeText(uploadimageactivity.this, "Please select an image to upload.", Toast.LENGTH_SHORT).show();
        }
    }




    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

}
