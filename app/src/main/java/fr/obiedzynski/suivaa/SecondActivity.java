package fr.obiedzynski.suivaa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import fr.obiedzynski.suivaa.DataSync.cabinetSync;
import fr.obiedzynski.suivaa.DataSync.medecinSync;
import fr.obiedzynski.suivaa.DataSync.pingDB;

public class SecondActivity extends AppCompatActivity {

    private Button addvisitemedecin;
    private Button addvisitecabinet;

    private static final int REQUEST_CODE = 1000;
    private String idVisiteur;
    private String idVisiteur_medecin;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location userLocation;
    private Boolean issetLocation;

    private DataBaseManager dataBaseManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        new pingDB().execute(this);
        Intent intent = getIntent();
        if(intent != null)
        {
            idVisiteur = intent.getStringExtra("id");
            idVisiteur_medecin = intent.getStringExtra("id_medecin");
        }
        dataBaseManager = new DataBaseManager(this);
        addvisitemedecin = findViewById(R.id.medecinButton);
        addvisitecabinet = findViewById(R.id.cabinetButton);
        issetLocation = false;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallback();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        addvisitemedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, addvisite_medecin.class);
                intent.putExtra("id_medecin",idVisiteur_medecin);
                intent.putExtra("id",idVisiteur);
                startActivity(intent);


            }
        });

        addvisitecabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(issetLocation.equals(false))
                {
                    new AlertDialog.Builder(SecondActivity.this)
                            .setTitle("Impossible de vous localisez")
                            .setMessage("Veuillez activer la localisation afin de saisir ce type de visite ou attendre le message de confirmation")
                            .create()
                            .show();
                }
                else
                {
                    int id = compareLocation();
                    Intent intent = new Intent(SecondActivity.this, addvisite_cabinet.class);
                    intent.putExtra("id_medecin",idVisiteur_medecin);
                    intent.putExtra("id_user",idVisiteur);
                    intent.putExtra("id",String.valueOf(id));
                    startActivity(intent);
                }
            }
        });
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback()
        {
            @Override
            public void  onLocationResult(LocationResult locationResult){
                for(Location location:locationResult.getLocations()){
                    Toast.makeText(SecondActivity.this,"Position acquise",Toast.LENGTH_SHORT).show();
                    userLocation = location;
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    issetLocation = true;
                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
    }

    private int compareLocation(){
        List<Cabinet> cabinets = dataBaseManager.getCabinet();
        int cabinetPlusProche = 0;
        Float distance = null;
        for (Cabinet cabinet:cabinets)
        {
            Float compare = userLocation.distanceTo(cabinet.getLocation());
            if(distance == null || distance>compare){
                distance = compare;
                cabinetPlusProche = cabinet.getId();
            }
        }
        return cabinetPlusProche;
    }
}