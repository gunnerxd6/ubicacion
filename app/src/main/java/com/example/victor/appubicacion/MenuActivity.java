package com.example.victor.appubicacion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    TextView tv_usuario, tv_ubicacion;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myref, myref2;
    Switch aSwitch;
    Button bt_agregar;
    RecyclerView recyclerView;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        final List<UserInformation> usuarios;
        recyclerView = findViewById(R.id.recycler_view);
        tv_usuario = findViewById(R.id.tv_nombre);
        tv_ubicacion = findViewById(R.id.tv_ubicacion);
        aSwitch = findViewById(R.id.switch1);
        bt_agregar = findViewById(R.id.bt_menu_agregar);
        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this,AmigosActivity.class);
                startActivity(i);
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        myref = firebaseDatabase.getReference();
        myref2 = firebaseDatabase.getReference().child("users");


        //Obtener localizacion
       locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Toast.makeText(getApplicationContext(),"Debe permitir el uso de GPS para compartir su ubicación",Toast.LENGTH_SHORT).show();
            tv_ubicacion.setText("Ubicación no disponible");
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }

        //Cargar recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usuarios = new ArrayList<>();

        final Adaptador adaptador = new Adaptador(usuarios, this);
        recyclerView.setAdapter(adaptador);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                compartiUbicacion(isChecked);
            }
        });
        myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.removeAll(usuarios);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {


                    UserInformation usuario = snapshot.getValue(UserInformation.class);
                    if (usuario.isCompartir_ubicacion() == true) {
                        usuarios.add(usuario);
                    }

                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Cargar usuario actual
        final String userId = mAuth.getCurrentUser().getUid();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_usuario.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getName());
                aSwitch.setChecked(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).isCompartir_ubicacion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            tv_ubicacion.setText("");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String ubicacion = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    ubicacion = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference currentUserDb = mDatabase.child(mAuth.getCurrentUser().getUid());
            currentUserDb.child("ubicacion").setValue(ubicacion);
            currentUserDb.child("latitud").setValue(latitude);
            currentUserDb.child("longitud").setValue(longitude);
            tv_ubicacion.setText(ubicacion);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    private void compartiUbicacion(Boolean activado) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference currentUserDb = mDatabase.child(mAuth.getCurrentUser().getUid());
        currentUserDb.child("compartir_ubicacion").setValue(activado);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        locationManager.removeUpdates(locationListener);
        locationManager = null;
        finish();
        Intent i = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(i);

    }
}
