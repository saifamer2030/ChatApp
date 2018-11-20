package com.developersaifamer2030.friendsstatuspro.NotificationOnlin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.developersaifamer2030.friendsstatuspro.MainActivity;
import com.developersaifamer2030.friendsstatuspro.Product;
import com.developersaifamer2030.friendsstatuspro.R;
import com.developersaifamer2030.friendsstatuspro.listadapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


public class MyTokenProfile extends AppCompatActivity {

    //ProgressBar progressBar1;
    RecyclerView mylist;
    private RecyclerView.LayoutManager mLayoutManager;
////ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ


    CircleImageView homelessPic;
    ProgressBar progressBar;
    String downloadedurl;
    EditText Name, Sug, City,Num;
    Button Share;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private Uri img_uri;
    private static final int image = 101;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        mStorage = FirebaseStorage.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String s = ("MY Token :" + FirebaseInstanceId.getInstance().getToken());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.putname);

        Name = (EditText) findViewById(R.id.name);
        Sug = (EditText) findViewById(R.id.sug);
        City = (EditText) findViewById(R.id.city);
        Share = (Button) findViewById(R.id.share);
        Num = (EditText) findViewById(R.id.phone);
        progressBar = findViewById(R.id.myprogressbar);
        homelessPic = (CircleImageView) findViewById(R.id.profile_image);
     //   homelessPic.setBackgroundResource(R.drawable.ic_person_black_24dp);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText, cityeText, sugText , numText;
                nameText = Name.getText().toString();
                cityeText = City.getText().toString();
                sugText = Sug.getText().toString();
                numText = Num.getText().toString();

                if (nameText.isEmpty() || nameText.equals(" ")) {
                    Name.setError(getString(R.string.fill_here));
                    return;
                }

                if (cityeText.isEmpty() || cityeText.equals(" ")) {
                    City.setError(getString(R.string.fill_here));
                    return;
                }

                if (sugText.isEmpty() || sugText.equals(" ")) {
                    Sug.setError(getString(R.string.fill_here));
                    return;
                }
                if (numText.isEmpty() || numText.equals(" ")) {
                    Num.setError(getString(R.string.fill_here));
                    return;
                }
                Product product = new Product(nameText, cityeText, sugText,numText ,downloadedurl);
                database.getReference().child("Users").push().setValue(product);
                Name.setText("");
                City.setText("");
                Sug.setText("");
                Num.setText("");
                uploadImage();

            }

        });
        homelessPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });



        progressBar = (ProgressBar) findViewById(R.id.myprogressbar);
        mylist = (RecyclerView) findViewById(R.id.listhomelesinform);
        mylist.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mylist.setLayoutManager(mLayoutManager);
        mylist.setAdapter(new listadapter(this));
////ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ



    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                homelessPic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(MyTokenProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MyTokenProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
