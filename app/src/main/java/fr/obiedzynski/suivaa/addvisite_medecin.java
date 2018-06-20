package fr.obiedzynski.suivaa;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.obiedzynski.suivaa.DataSync.cabinetSync;
import fr.obiedzynski.suivaa.DataSync.visiteSync;

public class addvisite_medecin extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private TextView leMedecin;
    private DataBaseManager dataBaseManager;
    private String id_medecin;
    private String id;
    private Button visibutton;
    private Button arrbutton;
    private Button entrbutton;
    private Button depbutton;
    private Button validate;
    private CheckBox remember;
    private DialogFragment timePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvisite_medecin);
        visibutton = findViewById(R.id.visibutton);
        arrbutton = findViewById(R.id.arrbutton);
        entrbutton = findViewById(R.id.entrbutton);
        depbutton = findViewById(R.id.depbutton);
        validate = findViewById(R.id.buttonValidate);
        leMedecin = findViewById(R.id.medecin);
        remember = findViewById(R.id.rememberMe);

        Intent intent = getIntent();
        if(intent != null)
        {
            id_medecin = intent.getStringExtra("id_medecin");
            id = intent.getStringExtra("id");
        }
        dataBaseManager=new DataBaseManager(this);
        Medecin medecin = dataBaseManager.getUnMedecin(Integer.parseInt(id_medecin));
        leMedecin.setText(medecin.getNom());
        visibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int annee = cal.get(Calendar.YEAR);
                int jour = cal.get(Calendar.DAY_OF_MONTH);
                int mois = cal.get(Calendar.MONTH);

                DatePickerDialog dialog = new DatePickerDialog(addvisite_medecin.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, annee, mois, jour);
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
                if((visibutton.getTag() == null) || (arrbutton.getTag() == null) ||(entrbutton.getTag() == null) || (depbutton.getTag() == null)){
                    Toast.makeText(addvisite_medecin.this,"Veuillez remplir tous les champs",Toast.LENGTH_SHORT).show();
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
                    dataBaseManager.insertVisite(isRdv, hArrivee, hDebut, hDepart, dVisite, Integer.parseInt(id_medecin), Integer.parseInt(id));

                    Intent intent = new Intent(addvisite_medecin.this, SecondActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("id_medecin",id_medecin);
                    startActivity(intent);
                    Toast.makeText(addvisite_medecin.this,"Visite enregistrée",Toast.LENGTH_SHORT).show();

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
