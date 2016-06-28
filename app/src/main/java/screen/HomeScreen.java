package screen;

import android.os.Bundle;

import datalicious.com.news.R;

public class HomeScreen extends DataliciousActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomePagerFragment()).commit();
    }
}
