package rdev.findit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.UUID;


public class findActivity extends AppCompatActivity {

    public final String URL = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";
    private static final int GALERY_INTENT = 5;


    //VÁLTOZÓK

    private Spinner spinner_country;
    private Spinner spinner_city;
    private Button button_send;
    private Button button_photo_upload;
    private EditText editText_desc;
    private EditText editText_contact;
    private EditText editText_name;
    private List<String> spinnerArray;
    private List<String> spinnerCounryArray;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private Uri filePath;
    private String[] url = new String[1];
    private Boolean isClicked = false;


    //VÁLTOZÓK


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);


        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        button_send = (Button) findViewById(R.id.button_Send);
        button_photo_upload = (Button) findViewById(R.id.button_select_image);
        editText_desc = (EditText) findViewById(R.id.editText_Description);
        editText_contact = (EditText) findViewById(R.id.editText_Contact);
        editText_name = (EditText) findViewById(R.id.editText_Name);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("posts");
        mStorageReference = FirebaseStorage.getInstance().getReference();


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


        //GOMBNYOMÁSRA AZ ADAT KIVÁLASZTÁSA


        button_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isClicked = true;
                chooseImage();

            }
        });


        //GOMBNYOMÁSRA AZ ADAT KIVÁLASZTÁSA


        //GOMBNYOMÁSRA AZ ADAT FELTÉTELE FIREBASE-BA


        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String desc = editText_desc.getText().toString();
                String contact = editText_contact.getText().toString();
                String name = editText_name.getText().toString();


                if (InternetStatus.getInstance(getApplicationContext()).isOnline()) {

                    if (TextUtils.isEmpty(desc)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_desc_text), Toast.LENGTH_SHORT).show();


                    } else if (TextUtils.isEmpty(contact)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_contact_text), Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(name)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_name_text), Toast.LENGTH_SHORT).show();

                    } else if (!isClicked) {

                        Toast.makeText(getApplicationContext(),getString(R.string.toast_image_text),Toast.LENGTH_SHORT).show();

                    } else {
                        uploadImageAndData();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connected), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_connect_to_internet), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);


                }


            }
        });


    }


    private class JSONTaskCountry extends AsyncTask<String, String, List<String>> {

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

    private class JSONTaskCity extends AsyncTask<String, String, List<String>> {

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

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALERY_INTENT);

    }

    private void uploadImageAndData() {


        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("please wait..");
            progressDialog.show();

            StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    url[0] = taskSnapshot.getDownloadUrl().toString();

                    progressDialog.dismiss();


                    String country = spinner_country.getSelectedItem().toString();
                    String city = spinner_city.getSelectedItem().toString();
                    String desc = editText_desc.getText().toString();
                    String contact = editText_contact.getText().toString();
                    String name = editText_name.getText().toString();


                    if (TextUtils.isEmpty(desc)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_desc_text), Toast.LENGTH_SHORT).show();


                    } else if (TextUtils.isEmpty(contact)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_contact_text), Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(name)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_name_text), Toast.LENGTH_SHORT).show();

                    } else {


                        Log.d("image", url[0]);

                        String id = mDatabaseReference.push().getKey();


                        DataModel data = new DataModel(url[0], country, city, desc, contact, name);


                        mDatabaseReference.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(), getString(R.string.toast_upploaded_text), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();
                    Log.d("Image", "Problem with image Upload!" + e.getMessage());

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    progressDialog.setMessage("please wait  " + (int) progress + "%");

                }
            });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {


            try {
                filePath = data.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}





