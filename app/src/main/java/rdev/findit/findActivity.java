package rdev.findit;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.R.layout.simple_spinner_dropdown_item;

public class findActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);




        Spinner spinner_county = (Spinner) findViewById(R.id.spinner_country);


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
        spinner_county.setAdapter(countryAdapter);







    }
}
