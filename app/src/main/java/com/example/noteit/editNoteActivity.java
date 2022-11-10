package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editNoteActivity extends AppCompatActivity {
   Intent data;
   EditText mEditTittleOfNote, mEditContentOfNote;
   FloatingActionButton mSavedItNote;
   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mEditTittleOfNote =findViewById(R.id.editTitleOfNote);
        mEditContentOfNote =findViewById(R.id.editContentOfNote);
        mSavedItNote =findViewById(R.id.savedItNote);
        data=getIntent();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolBarOfEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSavedItNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"save button click",Toast.LENGTH_SHORT);
                String newTittle= mEditTittleOfNote.getText().toString();
                String newContent = mEditContentOfNote.getText().toString();
                if(newTittle.isEmpty()||newContent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Something is empty",Toast.LENGTH_SHORT);
                    return;
                }
                else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("notes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note= new HashMap<>();
                    note.put("tittle",newTittle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note is Updated",Toast.LENGTH_SHORT);
                            startActivity(new Intent(editNoteActivity.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });

    String noteTittle=data.getStringExtra("tittle");
    String noteContent =data.getStringExtra("content");
    mEditContentOfNote.setText(noteContent);
    mEditTittleOfNote.setText("notetittle");
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}