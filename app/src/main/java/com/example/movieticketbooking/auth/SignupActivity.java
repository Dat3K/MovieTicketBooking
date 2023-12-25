package com.example.movieticketbooking.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.home.HomeActivity;
import com.example.movieticketbooking.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(view -> {
            String name = signupName.getText().toString();
            String email = signupEmail.getText().toString();
            String password = signupPassword.getText().toString();
            if (name.isEmpty() || email.isEmpty() ||password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    createNewUserDocument(name);
                    updateUserProfile(name);
                } else {
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUserProfile(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userDocRef = db.collection("users").document();

        User newUser = new User(
                user.getUid(),  // Use the automatically generated document ID
                name,
                user.getEmail()
        );
        userDocRef.set(newUser)
                .addOnSuccessListener(aVoid -> {
                    Log.d("UserCreate", "Success" + aVoid);
                })
                .addOnFailureListener(e -> {
                    Log.d("UserCreate", "Failed" + e);

                });
    }

    private void createNewUserDocument(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Failed to update user profile.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}