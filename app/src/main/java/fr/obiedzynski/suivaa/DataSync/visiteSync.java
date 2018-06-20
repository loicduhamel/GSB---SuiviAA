package fr.obiedzynski.suivaa.DataSync;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import fr.obiedzynski.suivaa.DataBaseManager;

public class visiteSync extends AsyncTask<String,Void,Void> {
    private String json_url;
    private String json_string;
    private DataBaseManager dataBaseManager;

    @Override
    protected Void doInBackground(String... params) {
        try {
            String rdv = params[0];
            String hArrivee = params[1];
            String hDebut = params[2];
            String hDepart = params[3];
            String dVisite = params[4];
            String id_medecin = params[5];
            String id_user = params[6];
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data_post = URLEncoder.encode("rdv", "UTF-8") + "=" + URLEncoder.encode(rdv, "UTF-8") + "&" +
                    URLEncoder.encode("hArrivee", "UTF-8") + "=" + URLEncoder.encode(hArrivee, "UTF-8") + "&" +
                    URLEncoder.encode("hDebut", "UTF-8") + "=" + URLEncoder.encode(hDebut, "UTF-8") + "&" +
                    URLEncoder.encode("hDepart", "UTF-8") + "=" + URLEncoder.encode(hDepart, "UTF-8") + "&" +
                    URLEncoder.encode("dVisite", "UTF-8") + "=" + URLEncoder.encode(dVisite, "UTF-8") + "&" +
                    URLEncoder.encode("id_medecin", "UTF-8") + "=" + URLEncoder.encode(id_medecin, "UTF-8") + "&" +
                    URLEncoder.encode("id_user", "UTF-8") + "=" + URLEncoder.encode(id_user, "UTF-8");
            bufferedWriter.write(data_post);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                result += line;
            }
            Log.i("sync",result);
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    protected void onPreExecute()
    {
        json_url = "http://172.16.9.24/WebService/visite.php";
    }

    @Override
    protected void onProgressUpdate(Void...values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid){
    super.onPostExecute(aVoid);
    }
    }


