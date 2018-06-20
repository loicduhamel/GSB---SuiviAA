package fr.obiedzynski.suivaa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.obiedzynski.suivaa.DataSync.visiteSync;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppliFrais.db";
    private static final int DATABASE_VERSION = 8;


    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db)
    {
        String sqltableMedecin="create table medecin (id INTEGER PRIMARY KEY , nom VARCHAR, prenom VARCHAR, id_cabinet INTEGER)";
        String sqltableCabinet="create table cabinet (id INTEGER PRIMARY KEY , adresse VARCHAR, code_postal VARCHAR, ville VARCHAR, longitude VARCHAR, latitude VARCHAR)";
        String sqltableUtilisateur="create table utilisateur (id INTEGER PRIMARY KEY , nom VARCHAR, prenom VARCHAR, login VARCHAR, password VARCHAR, id_role Integer, id_medecin Integer)";
        String sqltableVisite="create table visite (id INTEGER PRIMARY KEY AUTOINCREMENT,rdv INTEGER, hArrivee String , hDebut String, hDepart String, dVisite String, id_medecin INTEGER, id_user INTEGER)";

        db.execSQL(sqltableMedecin);
        db.execSQL(sqltableCabinet);
        db.execSQL(sqltableUtilisateur);
        db.execSQL(sqltableVisite);

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        this.onCreate(db);
    }

    public void insertMedecin (Integer id, String nom, String prenom, Integer id_cabinet)
    {
        String strSql = "insert or ignore into medecin (id,nom,prenom,id_cabinet) values ('" + id + "','" + nom + "','" + prenom + "','" + id_cabinet + "')";
        this.getWritableDatabase().execSQL(strSql);

    }
    public void insertUtilisateur (Integer id, String nom, String prenom, String login, String password, Integer id_role, Integer id_medecin)
    {
        String strSql = "insert or ignore into utilisateur (id,nom,prenom,login,password,id_role,id_medecin) values ('" + id + "','" + nom + "','" + prenom + "','" + login + "','"+password+"','"+id_role+"','"+id_medecin+"')";
        this.getWritableDatabase().execSQL(strSql);

    }

    public void insertCabinet (Integer id, String adresse, String code_postal, String ville, String longitude, String latitude)
    {
        String strSql = "insert or ignore into cabinet (id,adresse,code_postal,ville,longitude,latitude) values ('" + id + "','" + adresse + "','" + code_postal + "','" + ville + "','" + longitude + "','" + latitude + "')";
        this.getWritableDatabase().execSQL(strSql);

    }

    public void insertVisite (Integer rdv, String hArrivee, String hDebut, String hDepart, String dVisite, Integer id_medecin, Integer id_user)
    {
        String strSql = "insert into visite (rdv, hArrivee, hDebut, hDepart, dVisite, id_medecin, id_user) values ('" + rdv + "','" + hArrivee + "','" + hDebut + "','" + hDepart + "','" + dVisite + "','" + id_medecin + "','" + id_user + "')";
        this.getWritableDatabase().execSQL(strSql);
        Log.i("sync",strSql);
    }

    public List<Cabinet> getCabinet()
    {
        List<Cabinet> cabinets = new ArrayList<>();
        String strSql = "Select * from cabinet";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()){
            Cabinet cabinet = new Cabinet(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            cabinets.add(cabinet);
            cursor.moveToNext();
        }
        cursor.close();
        return cabinets;
    }

    public List<Medecin> getMedecin(int id)
    {
        List<Medecin> medecins = new ArrayList<>();
        String strSql = "Select * from medecin where id_cabinet = " + id;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()){
            Medecin medecin = new Medecin(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            medecins.add(medecin);
            cursor.moveToNext();
        }
        cursor.close();
        return medecins;
    }

    public List<Utilisateur> getUtilisateur()
    {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String strSql = "Select * from utilisateur";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()){
            Utilisateur utilisateur = new Utilisateur(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getInt(5), cursor.getInt(6));
            utilisateurs.add(utilisateur);
            cursor.moveToNext();
        }
        cursor.close();
        return utilisateurs;
    }

    public Medecin getUnMedecin(int id){
        String strSql = "Select * from medecin where id = "+id ;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        Medecin medecin = new Medecin(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));;
        cursor.close();
        return medecin;
    }

    public void getVisite()
    {
        String strSql = "Select rdv, hArrivee, hDebut, hDepart, dVisite, id_medecin, id_user from visite ";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql,null);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()){
            Visite visite = new Visite(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            Integer isRdv = visite.getRdv();
            String hArrivee = visite.gethArrivee();
            String hDebut = visite.gethDebut();
            String hDepart = visite.gethDepart();
            String dVisite = visite.getdVisite();
            Integer id_medecin = visite.getId_medecin();
            Integer id = visite.getId();
            new visiteSync().execute(isRdv.toString(), hArrivee, hDebut, hDepart, dVisite, id_medecin.toString(), id.toString());
            cursor.moveToNext();
        }
        this.delete();
        cursor.close();
    }

    public void delete()
    {
        this.getWritableDatabase().execSQL("Delete from visite");
    }

}