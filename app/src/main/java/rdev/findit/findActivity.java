package rdev.findit;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.R.layout.simple_spinner_dropdown_item;

public class findActivity extends AppCompatActivity {

    private Spinner spinner_counry;
    private Spinner spinner_city;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);



        spinner_counry = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);



        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();


            String line = "";
            while ((line = reader.readLine()) != null){

                buffer.append(line);

            }



        } catch (MalformedURLException e) {
            Log.e("Jshon","Url is not OK!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Jshon","Connection is not OK!");
            e.printStackTrace();
        }
        finally {

            if(connection != null){
                connection.disconnect();
            }

            try {
                if(reader != null){
                    reader.close();
                }

            } catch (IOException e) {
                Log.e("Jshon","Disconnect is not OK!");
                e.printStackTrace();
            }

        }












       /* Spinner spinner_county = (Spinner) findViewById(R.id.spinner_country);


        Locale[] locates = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locates) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        spinner_county.setAdapter(countryAdapter);*/













    }


}
