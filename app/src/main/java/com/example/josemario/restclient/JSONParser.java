package com.example.josemario.restclient;

import android.os.Build;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Jose Mario on 05/10/2015.
 */
public class JSONParser {
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int DATARETRIEVAL_TIMEOUT = 1000 ;

    public JSONParser()
    {

    }

    /*
        Descripción: Este método realiza peticios HTTP (GET o POST) al servicio web
        Entradas:
            pServiceUrl = URL al servicio web
            pMethod = método por invocar (POST o GET)
     */
    public JSONObject makeHttpRequest(String pServiceUrl, String pMethod)
    {
       disableConnectionReuseIfNecessary();

       HttpURLConnection urlConnection = null;
       //Se crea la conexión
        try {
            URL urlToRequest = new URL(pServiceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            //Manejar los errores
            int statusCode = urlConnection.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                //Se debe manejar la desautorización
            } else
            {
                if (statusCode != HttpURLConnection.HTTP_OK){
                    //Resolver los errores de código 404, 500, etc
                }
            }
            //Se crea el JSON a partir del contenido
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            //La URL es inválida
            e.printStackTrace();
        } catch (IOException e) {
            // no se puede leer el cuerpo de la respuesta
            // no se puede crear un Input Stream
            e.printStackTrace();
        } catch (JSONException e) {
            // el cuerpo de las respuesta no una hilera Json válida
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return null;
    }

    //Se requiere para evitar problemas de versiones nuevas de Android
    private static void disableConnectionReuseIfNecessary()
    {
        if(Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO)
        {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream)
    {
        return new Scanner(inStream).useDelimiter("//A").next();
    }


}
