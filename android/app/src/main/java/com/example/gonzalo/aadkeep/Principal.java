package com.example.gonzalo.aadkeep;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gonzalo.aadkeep.adaptador.Adaptador;
import com.example.gonzalo.aadkeep.gestores.GKeep;
import com.example.gonzalo.aadkeep.pojo.Keep;
import com.example.gonzalo.aadkeep.pojo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {

    private final int NEW = 1, OLD = 2;
    private Usuario user;
    private List<Keep> listaNotas;
    private GKeep gk;
    private Adaptador adaptador;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        user = getIntent().getParcelableExtra("user");

        init();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == NEW) {
            createNewKeep(data.getExtras().getString("content"), 0);
        }
        if (resultCode == RESULT_OK && requestCode == OLD) {
            updateKeep(data.getExtras().getString("content"), data.getExtras().getLong("id"), data.getExtras().getInt("previousposition"));
        }
    }

    public void init() {
        listaNotas = new ArrayList<>();
        gk = new GKeep(this);

        realizarSync();

        lv = (ListView) findViewById(R.id.listView);

        adaptador = new Adaptador(this, R.layout.item, listaNotas);
        lv.setAdapter(adaptador);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openKeepView(position);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            openNewKeepView();
            return true;
        }
        if (id == R.id.action_sinc){
            realizarSync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openKeepView(int position) {
        Intent i = new Intent(this, VerNotas.class);

        i.putExtra("user", user);
        i.putExtra("content", listaNotas.get(position).getContenido());
        i.putExtra("mode", "old");
        i.putExtra("id", listaNotas.get(position).getId());
        Log.v("antes", listaNotas.get(position).getId() + "");

        i.putExtra("previousposition", position);

        startActivityForResult(i, OLD);
    }

    public void openNewKeepView() {
        Intent i = new Intent(this, VerNotas.class);
        i.putExtra("user", user);
        i.putExtra("mode", "new");
        startActivityForResult(i, NEW);
    }

    public void createNewKeep(String content, long idd) {
        long id;
        if (idd != 0) {
            id = idd;
        } else {
            id = gk.getNextAndroidId(listaNotas);
        }
        Keep k = new Keep();

        k.setId(id);
        k.setContenido(content);
        k.setEstado(false);

        listaNotas.add(k);

        CrearKeep a = new CrearKeep();
        a.execute();

        realizarSync();
    }

    public void deleteKeep(final int previousPosition) {
        Keep k = new Keep();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                gk.deleteKeep(listaNotas.get(previousPosition), user);
            }
        };
        Thread t = new Thread(r);
        t.start();

        listaNotas.remove(listaNotas.get(previousPosition));
        adaptador.borrar(previousPosition);
    }

    public void updateKeep(String content, long id, final int previousPosition) {
        deleteKeep(previousPosition);
        createNewKeep(content, id);
    }

    private void realizarSync() {
        Sincronizar sinc = new Sincronizar();
        sinc.execute();
    }

    public class CrearKeep extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listaNotas = gk.uploadKeeps(listaNotas, user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adaptador.notifyDataSetChanged();
            adaptador = new Adaptador(Principal.this, R.layout.item, listaNotas);
            lv.setAdapter(adaptador);
        }
    }

    public class Sincronizar extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            listaNotas = gk.getUserKeeps(user);
            List<Keep> ambas = new ArrayList<>();
            ambas.addAll(listaNotas);

            for (Keep nota : listaNotas) {
                if (!nota.isEstado()) {
                    ambas.add(nota);
                }
            }
            listaNotas = ambas;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adaptador.notifyDataSetChanged();
            CrearKeep creark = new CrearKeep();
            creark.execute();
        }
    }
}
