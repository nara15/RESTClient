package com.example.josemario.restclient;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaPersonasActivity extends Activity {

    private ListView mainListView;
    private ArrayList<String> listaPersonas;
    private ArrayAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_personas);

        //Hallando la listView
        mainListView = (ListView) findViewById(R.id.listaPersonas);

        //Crear un adaptador para agregar elementos
        listaPersonas = new ArrayList<>();
        listAdapter = new ArrayAdapter(this, R.layout.simplerow, listaPersonas);
        //Agregar el adaptador a la ListView
        mainListView.setAdapter(listAdapter);

        new CargaDePersonas().execute();
    }

    class CargaDePersonas extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog dialog = new ProgressDialog(ListaPersonasActivity.this);

        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog.setMessage("Cargando personas");
            dialog.show();
        }

        protected Void doInBackground(Void... voids)
        {
            JSONParser jsonParser = new JSONParser();
            JSONObject serviceResult = jsonParser.makeHttpRequest("http://tripgripcr.comeze.com/php_webservice/get_personas.php", "GET");

            try {
                JSONArray personas = serviceResult.getJSONArray("personas");
                for (int i = 0; i < personas.length(); i ++)
                {
                    JSONObject persona = personas.getJSONObject(i);
                    listaPersonas.add(persona.getString("nombre"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused)
        {
            listAdapter.notifyDataSetChanged(); //Mostrar las personas en el ListView
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_personas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
