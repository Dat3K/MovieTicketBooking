package com.example.movieticketbooking.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email_input_field);
        passwordEditText = findViewById(R.id.password_input_field);

        setupLoginButton();
        setupRegisterTextView();
        setupForgotPasswordTextView();
    }

    private void setupLoginButton() {
        Button loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void setupRegisterTextView() {
        TextView registerTextView = findViewById(R.id.register_text);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegisterActivity();
            }
        });
    }

    private void setupForgotPasswordTextView() {
        TextView forgotPasswordTextView = findViewById(R.id.forgot_password_link);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToForgotPasswordActivity();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Check if email or password is empty
            Toast.makeText(LoginActivity.this, "The input cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            handleLoginSuccess();
                        } else {
                            handleLoginFailure(task);
                        }
                    }
                });
    }

    private void handleLoginSuccess() {
        FirebaseUser user = mAuth.getCurrentUser();
        // Sign in success, update UI
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    private void handleLoginFailure(Task<AuthResult> task) {
        // If sign in fails, display a message to the user.
        String errorMessage = task.getException().getMessage();
        Toast.makeText(LoginActivity.this, errorMessage,
                Toast.LENGTH_LONG).show();
    }

    private void switchToRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void switchToForgotPasswordActivity() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }
}
