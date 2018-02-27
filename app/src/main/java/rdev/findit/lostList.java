package rdev.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class lostList extends AppCompatActivity {

    private List<DataModel> elosPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_list);




        elosPost = new ArrayList<>();
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));
        elosPost.add(new DataModel (R.drawable.ic_launcher_background, "Description", "Contact", "Name"));



        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, elosPost);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);


    }



}
