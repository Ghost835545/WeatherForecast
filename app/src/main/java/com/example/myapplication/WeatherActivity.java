package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.connection.RemoteFetch;
import com.example.myapplication.entity.City;
import com.example.myapplication.entity.Coordinate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class WeatherActivity extends AppCompatActivity {
    private List<String> cites;
    private ArrayAdapter<String> listAdapter;
    private ListView listview;
    private int textlength = 0;
    private JSONArray jsonArray;
    private String[] array;
    private AlertDialog myalertDialog = null;
    private EditText txt_item = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        try {
            weatherActivity(this);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.change_city, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.city) {
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setTitle("Изменение города");
        final EditText editText = new EditText(WeatherActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        listview = new ListView(this);
        LinearLayout layout = new LinearLayout(WeatherActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        builder.setView(layout);
        final CustomAlertAdapter arrayAdapter = new CustomAlertAdapter(WeatherActivity.this, (ArrayList<String>) cites);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(arrayAdapter.getItem(position).toString());
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textlength = editText.getText().length();
                cites.clear();
                for (int i = 0; i < array.length; i++) {
                    try {
                        if (textlength <= array[i].length()) {
                            if (array[i].toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                                cites.add(array[i]);
                            }
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
                listview.setAdapter(new CustomAlertAdapter(WeatherActivity.this, (ArrayList<String>) cites));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {


            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String s = null;
                s = editText.getText().toString();
                changeCity(s);
                dialog.dismiss();
            }
        });
        myalertDialog = builder.show();
        //itemList.setVisibility(View.VISIBLE);
        //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cites);
        //itemList.setAdapter(listAdapter);
        //itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //@Override
            /*public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make Toast when click
                //Toast.makeText(getApplicationContext(), "Position " + , Toast.LENGTH_LONG).show();
                changeCity(listAdapter.getItem(position));
            }
        });*/
        //builder.setView(editText);
        //builder.setView(itemList);
        /*builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(editText.getText().toString());
            }
        });
        builder.show();*/
    }

    public void weatherActivity(Context context) throws IOException, JSONException {

        // Read content of company.json
        String jsonText = readText(context, R.raw.city);
        jsonArray = new JSONArray(jsonText);
        cites = new ArrayList<>();
        array = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            cites.add(i, jsonArray.getJSONObject(i).getString("name") + ", " + jsonArray.getJSONObject(i).get("country"));
            array[i] = jsonArray.getJSONObject(i).getString("name") + "," + jsonArray.getJSONObject(i).get("country");
        }
    }

    public static String readText(Context context, int resid) throws IOException {
        InputStream is = context.getResources().openRawResource(resid);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            //sb.append("/n");
        }
        return sb.toString();
    }

    public void changeCity(String city) {
        try {
            WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            wf.changeCity(city);
            new CityPreference(this).setCity(city);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}