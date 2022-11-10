package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    EditText mCreateNoteTitle, mCreateContentOfNote;
    FloatingActionButton mSaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    ProgressBar mProgressBarOfCreateNote;

    Button notifyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mSaveNote = findViewById(R.id.saveNoteFab);
        mCreateContentOfNote = findViewById(R.id.createContentOfNote);
        mCreateNoteTitle = findViewById(R.id.createNoteTitle);
        mProgressBarOfCreateNote =findViewById(R.id.progressBarOfCreateNote);

        //notification
        notifyBtn = findViewById(R.id.notify_btn);

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // notification code
                NotificationCompat.Builder builder = new NotificationCompat.Builder(CreateNote.this, "My Notification");
                builder.setContentTitle("NoteIt");
                builder.setContentText("New note is created");
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true); //auto cancels the notification after viewing

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CreateNote.this);
                managerCompat.notify(1,builder.build());
            }
        });

        //notification part ends

        Toolbar toolbar = findViewById(R.id.toolBarOfCreateNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // to implement back button

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // to store data on fireStore

        mSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mCreateNoteTitle.getText().toString();
                String content = mCreateContentOfNote.getText().toString();

                if(title.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Title Required", Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressBarOfCreateNote.setVisibility(View.VISIBLE);
                    // creates hierarchy of data
                    DocumentReference documentReference = fireStore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document();

                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);
                    
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNote.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to Create Note", Toast.LENGTH_SHORT).show();
                            mProgressBarOfCreateNote.setVisibility(View.INVISIBLE);
                        }
                    });



                }

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}