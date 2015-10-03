package me.dawars.hackathon2015;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;

public class CardActivity extends Activity {

    public static final String TITLE = "title";
    public static final String DESC = "desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();

        String title = intent.getExtras().getString(TITLE);
        String desc = intent.getExtras().getString(DESC);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create(title,
                desc,
                0);
        fragmentTransaction.add(R.id.frame_layout, cardFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startActivity(new Intent(this, MainActivity.class));
    }
}
