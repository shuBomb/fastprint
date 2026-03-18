package com.app.fastprint.ui.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fastprint.R;
import com.app.fastprint.ui.thankyou.ThankyouActivity;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {

    TextView tvTittle;
    TextView tvThankyou;
    TextView tvOrderPlace;
    TextView tvExperience;
    RatingBar productRating;
    EditText edtReview;
    TextView tvSubmit;
    TextView tvSkipp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        tvTittle = findViewById(R.id.tvTittle);
        tvThankyou = findViewById(R.id.tvThankyou);
        tvOrderPlace = findViewById(R.id.tvOrderPlace);
        tvExperience = findViewById(R.id.tvExperience);
        productRating = findViewById(R.id.productRating);
        edtReview = findViewById(R.id.edtReview);
        tvSubmit = findViewById(R.id.tvSubmit);
        tvSkipp = findViewById(R.id.tvSkipp);

        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvSubmit.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvSkipp.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thankYouIntent();
            }
        });

        tvSkipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thankYouIntent();
            }
        });

    }



    private void thankYouIntent() {
        Intent intent = new Intent(FeedbackActivity.this, ThankyouActivity.class);
        startActivity(intent);
    }
}