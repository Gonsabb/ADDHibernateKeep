package com.example.gonzalo.aadkeep.controladoresbd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gonzalo on 28/02/2016.
 */
public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "keepyourmind.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqldb;
        sqldb = "create table " + Contrato.TablaKeep.TABLA +
                " (" + Contrato.TablaKeep._ID +
                " integer primary key autoincrement, " +
                Contrato.TablaKeep.CONTENIDO + " text, " +
                Contrato.TablaKeep.ESTADO + " integer)";

        db.execSQL(sqldb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqldb = "drop table if exists "
                + Contrato.TablaKeep.TABLA;
        db.execSQL(sqldb);
        onCreate(db);

    }
}
