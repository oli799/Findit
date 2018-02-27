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


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });




        elosPost = new ArrayList<>();
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));
        elosPost.add(new DataModel("assddd", "Description", "Contact", "Name"));


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, elosPost);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));


    }



}
