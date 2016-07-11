package screen;

import android.os.Bundle;

import datalicious.com.news.R;
import datalicious.com.news.utils.Constants;

/**
 * Created by nayan on 11/7/16.
 */
public class TwitterDetailActivity extends DataliciousActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Twitter");
        setContentView(R.layout.activity_home_screen);
        getSupportFragmentManager().beginTransaction().replace(R.id.content,
                TwitterDetailFragment.getInstance(getIntent().getStringExtra(Constants.TWEET))).commit();
    }

}
