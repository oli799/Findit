package rdev.findit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class lostList extends AppCompatActivity {

    private lostActivity mLostActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_list);


        mLostActivity = new lostActivity();




    }
}
