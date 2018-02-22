package rdev.findit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.Iterator;
import java.util.List;


public class findActivity extends AppCompatActivity {

    private final String URL = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";

    //VÁLTOZÓK

    private Spinner spinner_country;
    private Spinner spinner_city;
    private Button button_send;
    private EditText edittext_desc;
    private EditText edittrex_contact;
    private List<String> spinnerArray;
    private List<String> spinnerCounryArray;
    private DatabaseReference mDatabaseReference;


    //VÁLTOZÓK


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);


        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        button_send = (Button) findViewById(R.id.button_Send);
        edittext_desc = (EditText) findViewById(R.id.editText_Description);
        edittrex_contact = (EditText) findViewById(R.id.editText_Contact);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("posts");


        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE

        if (InternetStatus.getInstance(getApplicationContext()).isOnline()) {

            new JSONTaskCountry().execute(URL);

        } else {

            Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connected), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(R.string.toast_connect_to_internet), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE


        //SPINNER_COUNTRY_ADAPTER_KIVÁLASZTÁS_ÉRZÉKELÉS_ÉS_ASYNC_MEGHÍVÁSA

        if (InternetStatus.getInstance(getApplicationContext()).isOnline()) {

            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    new JSONTaskCity().execute(URL);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {

            Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connected), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(R.string.toast_connect_to_internet), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }


        //SPINNER_COUNTRY_ADAPTER_KIVÁLASZTÁS_ÉRZÉKELÉS_ÉS_ASYNC_MEGHÍVÁSA


        //GOMBNYOMÁSRA AZ ADAT FELTÉTELE FIREBASE-BA


        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (InternetStatus.getInstance(getApplicationContext()).isOnline()) {

                    dataUpload();

                } else {

                    Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connected), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_connect_to_internet), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);


                }


            }
        });

        //GOMBNYOMÁSRA AZ ADAT FELTÉTELE FIREBASE-BA


    }

    public class JSONTaskCountry extends AsyncTask<String, String, List<String>> {

        private ProgressDialog dialog = new ProgressDialog(findActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("please wait..");
            this.dialog.show();

        }

        @Override
        protected List<String> doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {

                    buffer.append(line);

                }


                String finalJshon = buffer.toString();
                JSONObject parentObj = new JSONObject(finalJshon);

                //GET_JSON_OBJECT_NAMES

                spinnerCounryArray = new ArrayList<String>();


                Iterator<String> iter = parentObj.keys();
                while (iter.hasNext()) {
                    spinnerCounryArray.add(iter.next());

                }

                //GET_JSON_OBJECT_NAMES


                return spinnerCounryArray;


            } catch (MalformedURLException e) {
                Log.e("Jshon", "Url is not OK!");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Jshon", "Connection is not OK!");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("Jshon", "Array name is not OK!");
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    Log.e("Jshon", "Disconnect is not OK!");
                    e.printStackTrace();
                }

            }

            return null;

        }

        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            Context context = findActivity.this;


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context, android.R.layout.simple_spinner_item, result);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_country.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }


    public class JSONTaskCity extends AsyncTask<String, String, List<String>> {

        private ProgressDialog dialog = new ProgressDialog(findActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("please wait..");
            this.dialog.show();

        }

        @Override
        protected List<String> doInBackground(String... urls) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {

                    buffer.append(line);

                }


                String finalJshon = buffer.toString();
                JSONObject parentObj = new JSONObject(finalJshon);

                //GET_JSON_OBJECT_NAMES

                spinnerCounryArray = new ArrayList<String>();


                Iterator<String> iter = parentObj.keys();
                while (iter.hasNext()) {
                    spinnerCounryArray.add(iter.next());

                }

                //GET_JSON_OBJECT_NAMES


                JSONArray parentArray = parentObj.getJSONArray(spinner_country.getSelectedItem().toString());


                spinnerArray = new ArrayList<String>();

                if (parentArray != null) {
                    for (int i = 0; i < parentArray.length(); i++) {
                        spinnerArray.add(parentArray.getString(i));
                    }
                }

                return spinnerArray;


            } catch (MalformedURLException e) {
                Log.e("Jshon", "Url is not OK!");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Jshon", "Connection is not OK!");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("Jshon", "Array name is not OK!");
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    Log.e("Jshon", "Disconnect is not OK!");
                    e.printStackTrace();
                }

            }

            return null;

        }


        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            Context context = findActivity.this;


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context, android.R.layout.simple_spinner_item, result);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_city.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }


        }

    }


    public void dataUpload() {

        String country = spinner_country.getSelectedItem().toString();
        String city = spinner_city.getSelectedItem().toString();
        String desc = edittext_desc.getText().toString();
        String contact = edittrex_contact.getText().toString();


        if (TextUtils.isEmpty(desc)) {

            Toast.makeText(this, getString(R.string.toast_desc_text), Toast.LENGTH_SHORT).show();


        } else if (TextUtils.isEmpty(contact)) {

            Toast.makeText(this, getString(R.string.toast_contact_text), Toast.LENGTH_SHORT).show();

        } else {

            String id = mDatabaseReference.push().getKey();

            DataModel data = new DataModel(id, country, city, desc, contact);

            mDatabaseReference.child(id).setValue(data);

            Toast.makeText(this, getString(R.string.toast_upploaded_text), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }


    }


    public static class InternetStatus {

        //INERNET_ELLENORZES

        static Context context;

        private static InternetStatus instance = new InternetStatus();
        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        boolean connected = false;

        public static InternetStatus getInstance(Context ctx) {
            context = ctx.getApplicationContext();
            return instance;
        }

        @SuppressLint("ServiceCast")
        public boolean isOnline() {
            try {

                connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

                return connected;

            } catch (Exception e) {

                Log.d("connect", e.toString());

            }
            return connected;
        }


        //INERNET_ELLENORZES
    }


}





