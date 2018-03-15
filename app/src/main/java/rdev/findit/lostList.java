package rdev.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class lostList extends AppCompatActivity {

    private List<DataModel> elosPost;
    private TextView noPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_list);

        elosPost = new ArrayList<>();
        noPost = (TextView) findViewById(R.id.textView_noPost);




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //get all of the children
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    DataModel post = child.getValue(DataModel.class);


                    if (lostActivity.City.toString().equals(post.getCity())
                            && lostActivity.Country.toString().equals(post.getCountry())) {


                        elosPost.add(new DataModel(post.getId(),
                                post.getDesc().toString(), post.getContact().toString(), post.getName().toString()));

                    }

                    if(elosPost.isEmpty()){
                        noPost.setVisibility(TextView.VISIBLE);
                    }
                    else {
                        noPost.setVisibility(TextView.GONE);
                    }




                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, elosPost);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new AlphaInAnimationAdapter(recyclerViewAdapter));


    }


}
