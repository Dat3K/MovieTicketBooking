package com.example.movieticketbooking.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieticketbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.pass_rs_email);

        Button resetButton = findViewById(R.id.pass_rs_btn);
        setupSignInTextView();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResetButtonClick();
            }
        });
    }

    private void setupSignInTextView() {
        TextView registerTextView = findViewById(R.id.pass_rs_back);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLoginActivity();
            }
        });
    }

    private void switchToLoginActivity() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
    }

    private void handleResetButtonClick() {
        String email = emailEditText.getText().toString().trim();

        if (!email.isEmpty()) {
            resetPassword(email);
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Password reset email sent. Check your inbox.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Failed to send reset email. Please check your email address.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

