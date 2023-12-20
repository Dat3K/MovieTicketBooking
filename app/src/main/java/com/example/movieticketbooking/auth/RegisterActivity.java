package com.example.movieticketbooking.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText,cf_passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.reg_email_input);
        nameEditText = findViewById(R.id.reg_name_input);
        passwordEditText = findViewById(R.id.reg_password_input);
        cf_passwordEditText = findViewById(R.id.cf_password_input_field);

        setupRegisterButton();
        setupSignInTextView();
    }

    private void setupRegisterButton() {
        Button registerButton = findViewById(R.id.reg_reg_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void setupSignInTextView() {
        TextView registerTextView = findViewById(R.id.reg_sign_text);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLoginActivity();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String cf_password = cf_passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "The input cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(cf_password)) {
            Toast.makeText(RegisterActivity.this, "Confirm password is not the same.", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            createNewUserDocument(name);
                            updateUserProfile(name);
                        } else {
                            handleRegistrationFailure(task);
                        }
                    }
                });
    }
    private void createNewUserDocument(String name){
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
                    Log.d("UserCreate","Success" +aVoid );
                })
                .addOnFailureListener(e -> {
                    Log.d("UserCreate","Failed" + e);

                });
    }
    private void updateUserProfile(String name) {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    switchToMainActivity();
                } else {
                    handleProfileUpdateFailure();
                }
            }
        });
    }

    private void switchToMainActivity() {
        startActivity(new Intent(RegisterActivity.this, HomePageActivity.class));
        finish();
    }

    private void handleRegistrationFailure(Task<AuthResult> task) {
        String errorMessage = task.getException().getMessage();
        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void handleProfileUpdateFailure() {
        Toast.makeText(RegisterActivity.this, "Failed to update user profile.", Toast.LENGTH_SHORT).show();
    }

    private void switchToLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}