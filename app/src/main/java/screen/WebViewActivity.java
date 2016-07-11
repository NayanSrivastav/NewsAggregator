package screen;

import android.os.Bundle;

import datalicious.com.news.R;
import datalicious.com.news.utils.Constants;

/**
 * Created by nayan on 11/7/16.
 */
public class WebViewActivity extends DataliciousActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content, WebViewFragment.getInstance(getIntent().getStringExtra(Constants.URL))).commit();
    }
}
