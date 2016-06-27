package screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import datalicious.com.news.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread navThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                navigateToHomeScreen();
            }
        };
        navThread.start();
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }
}
