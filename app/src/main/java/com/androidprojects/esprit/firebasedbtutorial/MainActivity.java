package com.androidprojects.esprit.firebasedbtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myDatabaseReference;
    private String personId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // for data persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        myDatabaseReference=FirebaseDatabase.getInstance().getReference("Person");
        personId= myDatabaseReference.push().getKey();


        /*((ListView)findViewById(R.id.peopleList)).
                setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.people_list_row, R.id.personNameTv, readData()));*/

        (findViewById(R.id.addBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPerson(((EditText)findViewById(R.id.fullNameEditText)).getText().toString(),
                        Integer.parseInt(((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString()));
            }
        });
        (findViewById(R.id.updateBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePerson(((EditText)findViewById(R.id.fullNameEditText)).getText().toString(),Integer.parseInt(((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString()));
            }
        });
        (findViewById(R.id.deleteBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePerson("first added");
            }
        });
        (findViewById(R.id.loadBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        (findViewById(R.id.findBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPerson(((EditText)findViewById(R.id.fullNameEditText)).getText().toString());
            }
        });



    }

    private void addPerson(String name,int phoneNumber){
        Person person = new Person(name,phoneNumber);
        myDatabaseReference.child(personId).setValue(person);
    }
    private void updatePerson(String name,int phoneNumber){
            myDatabaseReference.child(personId).child("fullName").setValue(name);
            myDatabaseReference.child(personId).child("phoneNumber").setValue(phoneNumber);
    }
    private void removePerson(String name){
        /*Query deleteQuery = myDatabaseReference.orderByChild("fullName").equalTo(name);
        deleteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
        myDatabaseReference.child(personId).removeValue();
    }
    private void readData(){
        final ArrayList<String> names = new ArrayList<>();
        final ArrayList<Integer> phoneNumbers = new ArrayList<>();
        myDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())){
                    Person value = iterator.next().getValue(Person.class);
                    names.add(value.getFullName());
                    phoneNumbers.add(value.getPhoneNumber());
                    ((CustomListAdapater)(((ListView)findViewById(R.id.peopleList)).getAdapter())).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((ListView)findViewById(R.id.peopleList)).
                setAdapter(new CustomListAdapater(names,phoneNumbers,this));
                //setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.people_list_row, R.id.personNameTv,names));
    }
    private void findPerson(String name){
        Query deleteQuery = myDatabaseReference.orderByChild("fullName").equalTo(name);
        deleteQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())){
                    Log.d("Item found: ",iterator.next().getValue().toString()+"---");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Item not found: ","this item is not in the list");
            }
        });
    }

}
