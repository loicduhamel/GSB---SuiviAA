package fr.obiedzynski.suivaa;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.List;

public class addvisite_cabinet extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Button visibutton;
    private Button arrbutton;
    private Button entrbutton;
    private Button depbutton;
    private Button validate;
    private Button selectmedecin;
    private String id;
    private String id_medecin;
    private CheckBox remember;
    private DialogFragment timePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private DataBaseManager dataBaseManager;
    private List<Medecin> medecins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvisite_cabinet);
        visibutton = findViewById(R.id.visibutton);
        arrbutton = findViewById(R.id.arrbutton);
        entrbutton = findViewById(R.id.entrbutton);
        depbutton = findViewById(R.id.depbutton);
        validate = findViewById(R.id.buttonValidate);
        selectmedecin = findViewById(R.id.selectmedecin);
        remember = findViewById(R.id.rememberMe);

        dataBaseManager = new DataBaseManager(this);
        Intent intent = getIntent();
        if(intent != null)
        {
            medecins = dataBaseManager.getMedecin(Integer.parseInt(intent.getStringExtra("id")));
            id_medecin = intent.getStringExtra("id_medecin");
            id = intent.getStringExtra("id_user");
        }

        selectmedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dial = new AlertDialog.Builder(addvisite_cabinet.this);
                dial.setTitle("Veuillez sélectionner un médecin");
                int i =0;
                final String[] medecinNom = new String[medecins.size()];
                final String[] medecinId = new String[medecins.size()];
                final String[] medecinPrenom = new String[medecins.size()];
                for (Medecin value : medecins) {
                    medecinNom[i] =  value.getNom();
                    medecinPrenom[i] =  value.getPrenom();
                    medecinId[i] =  String.valueOf(value.getId());
                    i++;
                }
                    dial.setSingleChoiceItems(medecinNom,-1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            String textview = "Médecin : " + medecinNom[i] + " " +  medecinPrenom[i];
                            selectmedecin.setText(textview);
                            selectmedecin.setTag(medecinId[i]);
                            dialog.dismiss();
                        }
                    });
                dial.setNeutralButton("Sortir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dial.show();
            }
        });

        visibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int annee = cal.get(Calendar.YEAR);
                int jour = cal.get(Calendar.DAY_OF_MONTH);
                int mois = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(addvisite_cabinet.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, annee, mois, jour);
                dialog.show();
            }
        });

        arrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(),"arr");
            }
        });

        entrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(),"entr");
            }
        });

        depbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(),"dep");
            }
        });

            validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((visibutton.getTag() == null) || (arrbutton.getTag() == null) ||(entrbutton.getTag() == null) || (depbutton.getTag() == null) || (selectmedecin.getTag() == null)){
                    Toast.makeText(addvisite_cabinet.this,"Veuillez remplir tous les champs",Toast.LENGTH_SHORT).show();
                }
                else{
                    String hArrivee = arrbutton.getTag().toString();
                    String hDebut = entrbutton.getTag().toString();
                    String hDepart = depbutton.getTag().toString();

                    String dVisite = visibutton.getTag().toString();

                    Integer isRdv;

                    if(remember.isChecked()){
                        isRdv =1;
                    }
                    else
                    {
                        isRdv = 0;
                    }
                    dataBaseManager.insertVisite(isRdv, hArrivee, hDebut, hDepart, dVisite, Integer.parseInt(selectmedecin.getTag().toString()), Integer.parseInt(id));


                    Intent intent = new Intent(addvisite_cabinet.this, SecondActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("id_medecin",id_medecin);
                    startActivity(intent);
                    Toast.makeText(addvisite_cabinet.this,"Visite enregistrée",Toast.LENGTH_SHORT).show();

                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                String dateTag = year + "-" + month + "-" + dayOfMonth;
                visibutton.setText( "Date de la visite : " + date);
                visibutton.setTag(dateTag);
            }
        };
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        if(timePicker.getTag().equals("arr")){
            arrbutton.setText("Heure d'arrivée : " + time);
            arrbutton.setTag(time);
        }
        if(timePicker.getTag().equals("entr")){
            entrbutton.setText("Heure de début d'entretien : " + time);
            entrbutton.setTag(time);
        }
        if(timePicker.getTag().equals("dep")){
            depbutton.setText("Heure de départ : " + time);
            depbutton.setTag(time);
        }
    }
}
