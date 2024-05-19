package com.angelaavalos.pokedexls.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.angelaavalos.pokedexls.R;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText mailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailEditText = findViewById(R.id.mail);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            String email = mailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("fragment", "firstFragment");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Error en el login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        signupButton.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(Login.this, Sign_up.class);
            startActivity(signUpIntent);
        });
    }
}