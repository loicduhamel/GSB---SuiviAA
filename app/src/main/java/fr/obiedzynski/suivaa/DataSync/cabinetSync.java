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

public class cabinetSync extends AsyncTask<Context,Void,String>
{
    private String json_url;
    private String json_string;
    private Context context;
    private DataBaseManager dataBaseManager;
    private List<Cabinet> cabinets;

    @Override
    protected String doInBackground(Context... contextapp) {
        try {
            context = contextapp[0];
            json_string = "";
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((json_string = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(json_string + "\n");
            }
            Log.i("doInBackground: ",stringBuilder.toString());
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();

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
        json_url = "http://172.16.9.24/WebService/cabinet.php";
    }

    @Override
    protected void onProgressUpdate(Void...values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result){
       json_string = result;
       parseJSON();
       syncCabinet();
    }

    public void parseJSON(){
        if (json_string != null){
            try {
                cabinets = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                String id,adresse,code_postal,ville,longitude,latitude;

                for (int i = 0; i<jsonArray.length();i++)
                {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    id = jo.getString("id");
                    adresse = jo.getString("adresse");
                    code_postal = jo.getString("code_postal");
                    ville = jo.getString("ville");
                    longitude = jo.getString("longitude");
                    latitude = jo.getString("latitude");

                    String[] lon = longitude.split(",");
                    String lonStr = lon[0] + "." + lon[1];

                    String[] lat = latitude.split(",");
                    String latStr = lat[0] + "." + lat[1];

                    Cabinet cabinet = new Cabinet(Integer.parseInt(id),adresse,code_postal,ville,lonStr,latStr);
                    cabinets.add(cabinet);
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncCabinet(){
        dataBaseManager = new DataBaseManager(context);
        String adresse,code_postal,ville,longitude,latitude;
        Integer id;
        for (Cabinet cabinet:cabinets)
        {
            id = cabinet.getId();
            adresse = cabinet.getAdresse();
            code_postal = cabinet.getCode_postal();
            ville = cabinet.getVille();
            longitude = cabinet.getLongitude();
            latitude = cabinet.getLatitude();
            dataBaseManager.insertCabinet(id,adresse,code_postal,ville,longitude,latitude);
        }
        dataBaseManager.close();
    }
}
