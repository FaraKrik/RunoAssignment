package com.example.NewProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.runoassignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class NoteEditor extends AppCompatActivity {
    String notes;
    EditText editTextTopic,editTextDescription;
    ProgressBar savingprogress;

    private DatabaseReference mDatabase;

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.editormenu, menu);
            return true;
        }catch (Throwable e){
            e.getStackTrace();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.savenote:
                if(notes.equals("")) {
                    ViewNotes.notes = ViewNotes.notes+"\n" + editTextTopic.getText().toString().trim()+"\n"+editTextDescription.getText().toString().trim();

                }else{
                    ViewNotes.notes = editTextDescription.getText().toString().trim();
                }
                savingprogress.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notes").setValue(base64encode(ViewNotes.notes)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if(task.isSuccessful()){

                            Intent intent = new Intent(getApplicationContext(), ViewNotes.class);
                            startActivity(intent);
                            savingprogress.setVisibility(View.INVISIBLE);
                            Toast.makeText(NoteEditor.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            savingprogress.setVisibility(View.INVISIBLE);
                            Toast.makeText(NoteEditor.this, "Notes Update Failed", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        savingprogress=(ProgressBar)findViewById(R.id.savingprogress);


        editTextTopic = (EditText)findViewById(R.id.editTextTopic);
        editTextDescription= (EditText)findViewById(R.id.editTextDescription);
        Intent intent = getIntent();
        notes = String.valueOf(intent.getStringExtra("notes"));
        if(!notes.equals("")){
            editTextTopic.setHint("Edit Below Only");
            editTextDescription.setText(notes);
        }



//        if(noteID != -1)
//        {
//            editText.setText(ViewNotes.notes.get(noteID));
//        }
//
//        else
//        {
//            ViewNotes.notes.add("");                // as initially, the note is empty
//            noteID = ViewNotes.notes.size() - 1;
//            ViewNotes.arrayAdapter.notifyDataSetChanged();
//        }

//        editText.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
////                ViewNotes.notes.set(noteID, String.valueOf(s));
////                ViewNotes.arrayAdapter.notifyDataSetChanged();
////
////                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.runoassignent", Context.MODE_PRIVATE);
////                HashSet<String> set = new HashSet<>(ViewNotes.notes);
////                sharedPreferences.edit().putStringSet("notes", set).apply();
//
//                ViewNotes.notes+=String.valueOf(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });
    }

    public static String base64encode(String input) {
        try {
            return new String(Base64.encode(input.getBytes("UTF-8"), Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Got unsupported encoding: " + e);
            return "encode-error";
        }
    }

}