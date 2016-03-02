package com.example.gonzalo.aadkeep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.gonzalo.aadkeep.pojo.Usuario;

/**
 * Created by Gonzalo on 02/03/2016.
 */
public class VerNotas extends AppCompatActivity {

    private Usuario user;
    private String content = "", mode = "";
    private long id;
    private int previousPosition;
    private EditText etKeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addkeep);

        previousPosition = getIntent().getExtras().getInt("previousposition");
        user = getIntent().getParcelableExtra("user");
        id = getIntent().getExtras().getLong("id");
        content = getIntent().getExtras().getString("content");
        mode = getIntent().getExtras().getString("mode");

        etKeep = (EditText) findViewById(R.id.etAdd);
        etKeep.setText(content);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vernotas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        content = etKeep.getText().toString();
        resultActivity();
    }


    public void resultActivity() {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("content", content);

        if (mode.equals("old")) {
            resultIntent.putExtra("id", id);
            resultIntent.putExtra("previousposition", previousPosition);
        }

        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }
}
