package com.example.victor.appubicacion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    EditText usuario, email, pass, pass2;
    Button bt_registrar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usuario = findViewById(R.id.et_registro_usuario);
        email = findViewById(R.id.et_registro_email);
        pass = findViewById(R.id.et_registro_pass);
        pass2 = findViewById(R.id.et_registro_pass_confirmar);
        bt_registrar = findViewById(R.id.bt_registro_registrar);
        mAuth = FirebaseAuth.getInstance();

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario(email, pass, usuario, pass2);
            }
        });

    }

    private void crearUsuario(EditText email, EditText pass, EditText usuario, EditText pass2) {
        final String s_usuario = usuario.getText().toString().trim();
        final String s_email = email.getText().toString().trim();
        final String s_pass = pass.getText().toString().trim();
        final String s_pass2 = pass2.getText().toString().trim();
        if (s_email.equals("") || s_pass.equals("") || s_usuario.equals("")) {
            Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (!s_pass.equals(s_pass2)) {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                pass.setText("");
                pass2.setText("");
            } else {
                mAuth.createUserWithEmailAndPassword(s_email, s_pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.signInWithEmailAndPassword(s_email, s_pass);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                            DatabaseReference currentUserDb = mDatabase.child(mAuth.getCurrentUser().getUid());
                            currentUserDb.child("name").setValue(s_usuario);
                            currentUserDb.child("compartir_ubicacion").setValue(false);
                            Toast.makeText(getApplicationContext(), "Usuario registrado!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al registrar usuario!" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }
}
