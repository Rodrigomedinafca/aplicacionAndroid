package com.example.teci_.myapplication;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConsultaTiempo con = new ConsultaTiempo();
        con.execute();

    }

    private class ConsultaTiempo extends AsyncTask<Void,Void,String[]>{

       @Override
        protected String[] doInBackground(Void... String){
           URL url = null;
           String []datos = new String[2];

           try{
               url = new URL("http://192.168.1.81/proyecto/clima/3530597");
           }catch(MalformedURLException e){
               e.getStackTrace();
           }

           HttpURLConnection urlConexion = null;
           String dataJson = "";

           try{
               urlConexion = (HttpURLConnection) url.openConnection();
               urlConexion.setConnectTimeout(5000);
               BufferedReader in = new BufferedReader(new InputStreamReader(urlConexion.getInputStream()));
               StringBuffer buffer = new StringBuffer();

               String inputLine;

               while ((inputLine = in.readLine()) != null){
                   buffer.append(inputLine).append('\n');
               }

              dataJson = buffer.toString();

               JSONObject objetoJson = new JSONObject(dataJson);
               if(objetoJson.has("weather")){
                   JSONArray array =  objetoJson.getJSONArray("weather");
                   JSONObject icono = array.getJSONObject(0);
                   if(icono.has("icon")){
                       datos[0] = icono.getString("icon");
                   }
               }

               if(objetoJson.has("main")){
                   JSONObject temp = objetoJson.getJSONObject("main");
                   datos[1] = java.lang.String.valueOf(temp.getDouble("temp"));

               }

           }catch (IOException e) {
               e.getStackTrace();
           } catch (JSONException e) {
               e.printStackTrace();
           }finally {
               urlConexion.disconnect();
           }


           return datos;
       }

        @Override
        protected void onPostExecute(String s[]){

            ImageView imagen = (ImageView)findViewById(R.id.imagen_aplicaciion);

            int identi = getResources().getIdentifier("drawable/imagen_"+s[0],"drawable",getPackageName());
            imagen.setImageDrawable(getResources().getDrawable(identi));
            TextView texto = (TextView)findViewById(R.id.textTiempo);
            texto.setText(s[1] + "\u00b0");
        }
    }
}