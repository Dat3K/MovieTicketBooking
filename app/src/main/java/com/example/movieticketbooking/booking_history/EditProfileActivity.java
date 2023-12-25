package com.example.movieticketbooking.booking_history;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieticketbooking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail, editPassword;
    Button saveButton;
    ImageView backButton;
    CollectionReference reference;
    FirebaseFirestore db;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseUser user = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = db.getInstance().collection("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.newPassword);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backBtn);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValid()){
                    updateProfile();
                }
            }
        });

       backButton.setOnClickListener(v -> {
           Intent intent = new Intent(EditProfileActivity.this, BookingHistoryActivity.class);
           startActivity(intent);
           finish();
       });
    }

    private void updateProfile() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String newPass = editPassword.getText().toString();
        user.updatePassword(newPass);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates);
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                if (documentSnapshot.getString("id").equals(user.getUid())){
                    documentSnapshot.getReference().update("name", name);
                }
            }
        });
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditProfileActivity.this, BookingHistoryActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkValid() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.contains("@")){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void showData(){
        editEmail.setText(user.getEmail());
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                if(documentSnapshot.getString("id").equals(user.getUid())){
                    editName.setText(documentSnapshot.getString("name"));
                }
            }
        });
    }
}