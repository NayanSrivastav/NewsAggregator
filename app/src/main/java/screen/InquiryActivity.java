package screen;

import android.os.Bundle;

import datalicious.com.news.R;

/**
 * Created by nayan on 11/7/16.
 */
public class InquiryActivity extends DataliciousActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new InquiryFragment()).commit();
    }
}
