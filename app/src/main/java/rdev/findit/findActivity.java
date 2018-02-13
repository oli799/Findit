package rdev.findit;

import android.content.Context;
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
import java.util.List;

public class findActivity extends AppCompatActivity {

    private Spinner spinner_country;
    private Spinner spinner_city;
    private List<String> spinnerArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);



        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);



        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.countries,
                android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_country.setAdapter(adapter);


        //SPINNER_COUNTRY_ADAPTER_FELTÖLTÉSE


        //SPINNER_COUNTRY_ADAPTER_KIVÁLASZTÁS_ÉRZÉKELÉS_ÉS_ASYNC_MEGHÍVÁSA

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new JSONTask().execute("https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //SPINNER_COUNTRY_ADAPTER_KIVÁLASZTÁS_ÉRZÉKELÉS_ÉS_ASYNC_MEGHÍVÁSA





    }

    public class  JSONTask extends AsyncTask<String, String, List<String>> {

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
                JSONArray parentArray = parentObj.getJSONArray(spinner_country.getSelectedItem().toString());

                spinnerArray = new ArrayList<String>();

               if(parentArray != null){
                   for (int i = 0; i < parentArray.length(); i++){
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






        }

    }


}





