package datalicious.com.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread navThread=new Thread(new Runnable() {
            @Override
            public void run() {
              //  navigateToHomeScreen();
            }
        });
     navThread.start();
    }

    private void navigateToHomeScreen(){
        Intent intent=new Intent(this, null);
        finish();
        startActivity(intent);
    }
}
