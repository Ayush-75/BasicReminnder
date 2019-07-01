package com.example.basicreminnder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class register extends AppCompatActivity {
    EditText RNAME, REMAIL, RPASSWORD;
    TextView RLOGIN;
    Button register;
    private FirebaseAuth firebaseAuth;
    private ImageView img;
    String name, email, password;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    private StorageReference storageReference;
    Uri imagepath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() !=null){
            imagepath = data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();
        firebaseStorage=FirebaseStorage.getInstance();

         storageReference=firebaseStorage.getReference();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);

            }
        });
        RLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,MainActivity.class));
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String user_email = REMAIL.getText().toString().trim();
                    String user_password = RPASSWORD.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //sendEmailverification();
                                sendData();
                                firebaseAuth.signOut();
                                Toast.makeText(register.this, "Registered,upload complete!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(register.this, MainActivity.class));
                            } else {
                                Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }

    private void setupUIViews() {
        RNAME = (EditText) findViewById(R.id.Rname);
        REMAIL = (EditText) findViewById(R.id.Remail);
        RPASSWORD = (EditText) findViewById(R.id.Rpassword);
        RLOGIN = (TextView) findViewById(R.id.Rlogin);
        register = (Button) findViewById(R.id.Rregister);
        img = (ImageView) findViewById(R.id.RImage);
    }
    private Boolean validate() {
        Boolean result = false;

        name = RNAME.getText().toString();
        password = RPASSWORD.getText().toString();
        email = REMAIL.getText().toString();


        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || imagepath ==null) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }
    private void sendData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference uRef=firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference imageRef=storageReference.child(firebaseAuth.getUid()).child("images").child("Profile Pic");
        UploadTask uploadTask=imageRef.putFile(imagepath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, "Upload Failed", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(register.this, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        });
        User user =new User(name,email);
        uRef.setValue(user);


    }
}
