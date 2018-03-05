package rdev.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //get all of the children
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    DataModel post = child.getValue(DataModel.class);

                    elosPost.add(new DataModel(R.drawable.ic_launcher_background,
                            post.getDesc().toString(),post.getContact().toString(),post.getName().toString()));
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,elosPost);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(recyclerViewAdapter);




    }



}
