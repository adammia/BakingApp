package miadesign.hu.cakerecipes.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.ui.fragment.StepDetailFragment;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            if (getIntent().getExtras() != null) {
                String recipeName = getIntent().getExtras().getString("recipeName");
                getSupportActionBar().setTitle(recipeName);
            }
        }

        if (savedInstanceState == null) {
            // Create and add or replace step fragment
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            stepDetailFragment.setArguments(getIntent().getExtras());

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentById(R.id.step_detail_container) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
