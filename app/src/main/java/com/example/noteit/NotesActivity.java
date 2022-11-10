package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesActivity extends AppCompatActivity {

    FloatingActionButton mCreateNotesFab;
    private FirebaseAuth firebaseAuth;

    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;
    FirestoreRecyclerAdapter<FireBaseModel,NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mCreateNotesFab = findViewById(R.id.createNoteFab);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");

//        mfloatingActionButton = findViewById(R.id.createNoteFab);
//        mfloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Notes.this, CreateNote.class));
//            }
//        });





        mCreateNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this,CreateNote.class));
            }
        });

        getFirebaseData();

//        // path must match with uploading path
////        Query query = firestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").orderBy("title",Query.Direction.ASCENDING);
//
////        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
//
//
//        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(q,firebasemodel.class).build();
//
//        Log.i("testingView", "onCreate: "+allusernotes.getSnapshots());
//
//        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
//                ImageView popUpButton=holder.itemView.findViewById(R.id.menupopbutton);
//                int colorCode=getRandomColor();
//                holder.mnote.setBackgroundColor(Objects.requireNonNull(holder.itemView).getResources().getColor(colorCode,null));
//                holder.notetitle.setText(model.getTitle());
//                holder.notecontent.setText(model.getContent());
//                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(v.getContext(),noteDetails.class);
//                        intent.putExtra("tittle",model.getTitle());
//                        intent.putExtra("content",model.getContent());
//                        intent.putExtra("noteId",docId);
//                        v.getContext().startActivity(intent);
//                    }
//                });
//
//                popUpButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
//                        popupMenu.setGravity(Gravity.END);
//                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                Intent intent=new Intent(v.getContext(),editNoteActivity.class);
//                                intent.putExtra("tittle",model.getTitle());
//                                intent.putExtra("content",model.getContent());
//                                intent.putExtra("noteId",docId);
//                                v.getContext().startActivity(intent);
//                                return false;
//                            }
//                        });
//                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                // Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
//                                DocumentReference documentReference=firestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document(docId);
//                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(v.getContext(),"Failed to Delete",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                return false;
//                            }
//                        });
//                        popupMenu.show();
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
//                return new NoteViewHolder(view);
//            }
//        };
//
//        mrecyclerview= findViewById(R.id.recyclerview);
//        mrecyclerview.setHasFixedSize(true);
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
//        mrecyclerview.setAdapter(noteAdapter);



        Query query =  fireStore.collection("Notes")
                .document(firebaseUser.getUid())
                .collection("MyNotes")
                .orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FireBaseModel> allUserNotes = new FirestoreRecyclerOptions.Builder<FireBaseModel>().setQuery(query, FireBaseModel.class).build();

        Log.i("testingView", "onCreate: "+allUserNotes.getSnapshots());
        noteAdapter = new FirestoreRecyclerAdapter<FireBaseModel, NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull FireBaseModel model) {
                ImageView popupmenu = holder.itemView.findViewById(R.id.menuPopButton);
                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());
                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), noteDetails.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);
                    }
                });

                popupmenu.setOnClickListener(new View.OnClickListener() {
                    //@RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit text").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent = new Intent(v.getContext(),editNoteActivity.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteId",docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReference = fireStore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(NotesActivity.this, "Notes is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NotesActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, staggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);





    }

    private void getFirebaseData() {
        fireStore.collection("Notes")
                .document(firebaseUser.getUid())
                .collection("MyNotes")
                .orderBy("title",Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<FireBaseModel> fireBaseModelList = new ArrayList<>();
                        List<DocumentSnapshot> models = value.getDocuments();
                        Log.i("testingView", "onEvent: "+models);
                        for(int i=0;i<models.size();i++){
                            fireBaseModelList.add(models.get(i).toObject(FireBaseModel.class));
                        }
                        Log.i("testingView", "onEvent: "+ fireBaseModelList);
                    }
                });
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {


        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout mNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            mNote = itemView.findViewById(R.id.note);



        }
    }


    // to connect the menu xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(NotesActivity.this,MainActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }
    private int getRandomColor(){
        List<Integer> colorCode=new ArrayList<>();
        List<Integer> colorcode = null;
        colorcode.add(R.color.gray);
        colorcode.add(R.color.LightGreen);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);
        colorcode.add(R.color.green);
        Random random=new Random();
        int number = random.nextInt(colorCode.size());
        return colorCode.get(number);
    }
}