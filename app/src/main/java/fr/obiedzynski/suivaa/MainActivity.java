package fr.obiedzynski.suivaa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.obiedzynski.suivaa.DataSync.cabinetSync;
import fr.obiedzynski.suivaa.DataSync.medecinSync;
import fr.obiedzynski.suivaa.DataSync.pingDB;

public class MainActivity extends AppCompatActivity {
    private DataBaseManager dataBaseManager;
    private EditText login, password;
    private Button validate;
    private CheckBox rememberMe;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new pingDB().execute(this);
        dataBaseManager=new DataBaseManager(this);
        login = findViewById(R.id.loginText);
        password = findViewById(R.id.passwordText);
        validate = findViewById(R.id.buttonValidate);
        rememberMe = findViewById(R.id.rememberMe);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connexion();
            }
        });
        sharedPrefs = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        edit = sharedPrefs.edit();
        Boolean isDataSaved = sharedPrefs.getBoolean("isLoginSaved", true);
        if (isDataSaved == true) {
            login.setText(sharedPrefs.getString("login", null));
            password.setText(sharedPrefs.getString("password", null));
            rememberMe.setChecked(true);

        }

    }

    private void connexion() {
        boolean isValide =false;
        List<Utilisateur> utilisateurs= new ArrayList<>();
        utilisateurs = dataBaseManager.getUtilisateur();
        for (Utilisateur utilisateur:utilisateurs) {
            if (((utilisateur.getLogin()).equals(login.getText().toString()) && (password.getText().toString().equals(utilisateur.getPassword())))) {
                if (rememberMe.isChecked()) {
                    saveDataLogin();
                } else {
                    if (sharedPrefs.contains("isLoginSaved")) {
                        sharedPrefs.edit().clear().commit();
                    }
                }
                isValide=true;
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("id",String.valueOf(utilisateur.getId()));
                intent.putExtra("id_medecin",String.valueOf(utilisateur.getId_medecin()));
                startActivity(intent);
                Toast.makeText(this, "Connexion réussi", Toast.LENGTH_LONG).show();
            }

        }
        if (isValide == false) {
            sharedPrefs.edit().clear().commit();
            new AlertDialog.Builder(this)
                    .setTitle("Erreur de connexion")
                    .setMessage("Le mot de passe ou l'identidiant saisi est incorrect")
                    .create()
                    .show();
        }
    }

    public void saveDataLogin() {
        sharedPrefs = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        edit = sharedPrefs.edit();
        edit.putString("login", login.getText().toString());
        edit.putString("password", password.getText().toString());
        edit.putBoolean("isLoginSaved", true);
        edit.apply();
    }
}