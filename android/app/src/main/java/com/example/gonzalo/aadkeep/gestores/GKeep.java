package com.example.gonzalo.aadkeep.gestores;

import android.content.Context;
import android.util.Log;

import com.example.gonzalo.aadkeep.controladoresbd.GestorKeep;
import com.example.gonzalo.aadkeep.pojo.Keep;
import com.example.gonzalo.aadkeep.pojo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo on 28/02/2016.
 */
public class GKeep {

    private GestorKeep gk;
    private String urlDestino = "http://192.168.56.2:8080/KeepV2/go";

    public GKeep(Context context) {
        this.gk = new GestorKeep(context);

    }

    public GKeep() {
    }

    public List<Keep> getUserKeeps(Usuario u) {
        List<Keep> notas = new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destino = urlDestino + "?tabla=keep&op=read&login=" + login + "&origen=android&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                res += linea;
            }
            in.close();

            JSONObject obj = new JSONObject(res);
            JSONArray array = (JSONArray) obj.get("r");

            for (int i = 0; i < array.length(); i++) {

                JSONObject o = (JSONObject) array.get(i);

                Keep keep = new Keep(o.getInt("idAndroid"), o.getString("contenido"), true);
                notas.add(keep);
            }
            return notas;
        } catch (MalformedURLException e) {
            Log.v("xxx2", e.toString());
        } catch (IOException e) {
            Log.v("xxx3", e.toString());
        } catch (JSONException e) {
            Log.v("xxx4", e.toString());
        }
        return null;
    }

    public long getNextAndroidId(List<Keep> l) {
        long next = -1;
        for (Keep k : l) {
            if (k.getId() > next) {
                next = k.getId();
            }
        }
        return next + 1;
    }

    public List<Keep> uploadKeeps(List<Keep> l, Usuario u) {
        gk.open();
        List<Keep> d = new ArrayList<>();
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        List<Keep> uKeep = getUserKeeps(u);
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            for (Keep k : l) {
                if (!k.isEstado()) {
                    if (uKeep.contains(k)) {

                        String destinor = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido().replace(" ", "+") + "&accion=";
                        url = new URL(destinor);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    String destino = urlDestino + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido().replace(" ", "+") + "&accion=";
                    url = new URL(destino);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String linea;
                    while ((linea = in.readLine()) != null) {
                        res += linea;
                    }
                    in.close();
                    k.setEstado(true);
                    gk.changeState(k);
                }

                d.add(k);

            }
            gk.close();
            return d;

        } catch (MalformedURLException e) {
            Log.v("xxx2", e.toString());
        } catch (IOException e) {
            Log.v("xxx3", e.toString());
        }

        gk.close();
        return null;

    }

    public void deleteKeep(Keep k, Usuario u) {
        URL url = null;
        BufferedReader in = null;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getEmail(), "UTF-8");
            String destinor = urlDestino + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + k.getId() + "&contenido=" + k.getContenido() + "&accion=";
            url = new URL(destinor);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            Log.v("xxxe", e.toString());
        } catch (IOException e) {
            Log.v("xxxe", e.toString());
        }

    }
}
