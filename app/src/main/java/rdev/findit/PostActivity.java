package rdev.findit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {


    private TextView textTitle;
    private TextView textDesc;
    private TextView textContact;
    private ImageView imageImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        textTitle = (TextView) findViewById(R.id.postTitle);
        textDesc = (TextView) findViewById(R.id.postDesc);
        textContact = (TextView) findViewById(R.id.postContact);
        imageImage = (ImageView) findViewById(R.id.postImage);


        Intent intent = getIntent();

        String title = intent.getExtras().getString("PostTitle");
        String desc = intent.getExtras().getString("PostDesc");
        String conatct = intent.getExtras().getString("PostContact");
        String image = intent.getExtras().getString("PostImage");


        textTitle.setText(title);
        textDesc.setText(desc);
        textContact.setText("Contact: " + conatct);
        Picasso.with(getApplicationContext())
                .load(image)
                .fit()
                .centerCrop()
                .into(imageImage);


    }
}
