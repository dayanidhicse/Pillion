package com.example.dayanidhi.pillion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.Toast;

public class Rating extends AppCompatActivity {
    RatingBar rb1,rb2,rb3;
    public String trip_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        rb1=(RatingBar) findViewById(R.id.ratingBar);
        rb2=(RatingBar) findViewById(R.id.ratingBar2);
        rb3=(RatingBar) findViewById(R.id.ratingBar3);
        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(), "" + rating, Toast.LENGTH_SHORT).show();
                // txtRatingValue.setText(String.valueOf(rating));

            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(), "" + rating, Toast.LENGTH_SHORT).show();
                // txtRatingValue.setText(String.valueOf(rating));

            }
        });
        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(), "" + rating, Toast.LENGTH_SHORT).show();
                // txtRatingValue.setText(String.valueOf(rating));

            }
        });
            trip_id=getIntent().getExtras().getString("trip_id");

    }
}
