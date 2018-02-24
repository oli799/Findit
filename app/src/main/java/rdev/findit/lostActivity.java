package rdev.findit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.net.URL;

public class lostActivity extends AppCompatActivity {

    private final String URL = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";


    private Spinner spinnerLostCountry;
    private Spinner spinnerLostCity;
    private Button buttonLostOk;
    private List<String> spinnerCounryArray;
    private List<String> spinnerArray;

    public String City;
    public String Country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);


        spinnerLostCountry = (Spinner) findViewById(R.id.spinner_lost_country);
        spinnerLostCity = (Spinner) findViewById(R.id.spinner_lost_city);
        buttonLostOk = (Button) findViewById(R.id.button_lost_ok);

        findActivity.InternetStatus status = new findActivity.InternetStatus();


        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE

        if (status.getInstance(getApplicationContext()).isOnline()) {

            new JSONTaskCountry().execute(URL);


        } else {

            Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connected), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(R.string.toast_connect_to_internet), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE

        //SPINNER_COUNTRY_ADAPTER_KIVÁLASZTÁS_ÉRZÉKELÉS_ÉS_ASYNC_MEGHÍVÁSA

        if (status.isOnline()) {
            spinnerLostCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        buttonLostOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Country = spinnerLostCountry.getSelectedItem().toString();
                City = spinnerLostCity.getSelectedItem().toString();

                Intent intent = new Intent(getApplicationContext(), lostList.class);
                startActivity(intent);

            }
        });


    }

    private class JSONTaskCountry extends AsyncTask<String, String, List<String>> {

        private ProgressDialog dialog = new ProgressDialog(lostActivity.this);


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

            Context context = lostActivity.this;


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context, android.R.layout.simple_spinner_item, result);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLostCountry.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }

    private class JSONTaskCity extends AsyncTask<String, String, List<String>> {

        private ProgressDialog dialog = new ProgressDialog(lostActivity.this);

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


                JSONArray parentArray = parentObj.getJSONArray(spinnerLostCountry.getSelectedItem().toString());


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

            Context context = lostActivity.this;


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context, android.R.layout.simple_spinner_item, result);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLostCity.setAdapter(adapter);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }


        }

    }


}
