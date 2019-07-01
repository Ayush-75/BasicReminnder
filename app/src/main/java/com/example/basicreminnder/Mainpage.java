package com.example.basicreminnder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Mainpage extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    TextView username,email;
    View hview;
    ImageView imgg;
    private FirebaseStorage firebaseStorage;
    private int notificationId=1;
    EditText task1;
    TimePicker tt;
    String Task,hour,Minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        //
        findViewById(R.id.setT).setOnClickListener(this);
        findViewById(R.id.cancelT).setOnClickListener(this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navh_view);
        hview=navigationView.getHeaderView(0);
        username=(TextView)hview.findViewById(R.id.myname);
        email=(TextView)hview.findViewById(R.id.myemail);
        imgg=(ImageView)hview.findViewById(R.id.imageView);
        firebaseStorage=FirebaseStorage.getInstance();
        //
        task1=(EditText) findViewById(R.id.setTask);
        tt=(TimePicker) findViewById(R.id.timePicker);

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imgg);

            }
        });



        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());
                email.setText(user.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Mainpage.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView=(NavigationView) findViewById(R.id.navh_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.taskhistory:
                        startActivity(new Intent(Mainpage.this,history.class));
                        break;
                    case R.id.yourprofile:
                        startActivity(new Intent(Mainpage.this,profile.class));
                        break;
                    case R.id.log:
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Mainpage.this,MainActivity.class));
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {

        //notification Id
        Intent intent=new Intent(Mainpage.this,AlarmReceiver.class);
        intent.putExtra("notificationId",notificationId);
        intent.putExtra("ToDo",task1.getText().toString());

        //getBroadcast(context,request,intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Mainpage.this, 0,
                intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);

        switch (v.getId()){
            case R.id.setT:
                int hour=tt.getCurrentHour();
                int minute=tt.getCurrentMinute();

                //Create time
                Calendar startTime=Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE,minute);
                startTime.set(Calendar.SECOND,0);
                long alarmStartTime=startTime.getTimeInMillis();

                //Set alarm
                //set(type,millisecond,intent)
                alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                sendd();
                break;

            case R.id.cancelT:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                break;

        }


    }
    public void sendd(){
        Task=task1.getText().toString();
        hour=tt.getCurrentHour().toString();
        Minute=tt.getCurrentMinute().toString();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference Tref=firebaseDatabase.getReference().child("Task Details").child(firebaseAuth.getUid());
        Timee timee=new Timee(Task,Minute,hour);
        Tref.setValue(timee);
    }
}
