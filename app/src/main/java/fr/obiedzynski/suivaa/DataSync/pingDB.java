package fr.obiedzynski.suivaa.DataSync;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.obiedzynski.suivaa.Cabinet;
import fr.obiedzynski.suivaa.DataBaseManager;
import fr.obiedzynski.suivaa.Medecin;
import fr.obiedzynski.suivaa.Visite;

public class pingDB extends AsyncTask<Context,Void,Boolean>
{
    private Context context;
    private DataBaseManager dataBaseManager;
    @Override
    protected Boolean doInBackground(Context... cont) {
        Boolean result = false;
        context = cont[0];
        dataBaseManager = new DataBaseManager(context);
        try {
            URL urlObj = new URL("http://172.16.9.24");
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            con.connect();

            int code = con.getResponseCode();
            Log.i("ping",String.valueOf(code));

            if (code == 200) {
                result = true;
            }
        } catch (Exception e) {
            Log.i("ping",String.valueOf(e));
            result = false;
        }
        return result;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void...values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean bool){
        if(bool == true){
            new cabinetSync().execute(context);
            new medecinSync().execute(context);
            new utilisateurSync().execute(context);
            dataBaseManager.getVisite();
            Toast.makeText(context,"Synchronisation effectué",Toast.LENGTH_SHORT).show();
        }
    }
}
