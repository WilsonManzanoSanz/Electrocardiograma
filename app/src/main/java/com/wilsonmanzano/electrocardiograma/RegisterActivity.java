package com.wilsonmanzano.electrocardiograma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText;
    public String IP = "nada";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.editEdad);
                if (!Objects.equals(editText.getText().toString(), "")) {
                    Intent intent = new Intent(RegisterActivity.this, GraphActivity.class);
                    intent.putExtra("EDAD", editText.getText());
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Llenar los datos por favor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            //Display Fragment
            IPDIALOG IPDIALOGFRAGMENT = new IPDIALOG();
            FragmentManager fragmentManager = getSupportFragmentManager();
            IPDIALOGFRAGMENT.show(fragmentManager, "IP DIALOG");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
