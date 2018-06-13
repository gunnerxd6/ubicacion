package com.example.victor.appubicacion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AmigosActivity extends AppCompatActivity {
    EditText et_nombre_amigo;
    Button bt_agregar_amigo;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        et_nombre_amigo = findViewById(R.id.et_amigos_nombre);
        bt_agregar_amigo = findViewById(R.id.bt_amigos_agregar);
        mAuth = FirebaseAuth.getInstance();
        bt_agregar_amigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAmigo(et_nombre_amigo);
            }
        });
    }
    private void agregarAmigo(final EditText nombre) {
        final String s_usuario = nombre.getText().toString().trim();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabase.orderByChild("name").equalTo(s_usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot :
                        dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Log.e("KEY_USUARIO", key);
                    Toast.makeText(getApplicationContext(), "key: " + key, Toast.LENGTH_SHORT).show();
                    if (key == null) {
                        Toast.makeText(getApplicationContext(), "El usuario: " + s_usuario + " no esta registrado", Toast.LENGTH_LONG).show();
                    } else {
                        if (key.equals(mAuth.getCurrentUser().getUid().toString())) {
                            Toast.makeText(getApplicationContext(), "Debes agregar un usuario distinto al tuyo!", Toast.LENGTH_LONG).show();
                        } else {
                            DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("amigos");
                            DatabaseReference currentUserDb = mDatabase1.child(mAuth.getCurrentUser().getUid());
                            currentUserDb.child(key).setValue(true);
                            Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}