package screen;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import datalicious.com.news.R;

/**
 * Created by nayan on 27/6/16.
 */
public abstract class DataliciousActivity extends AppCompatActivity {
    protected Snackbar snackbar;

    public void showSnakebar(String text, int duration) {
        dismissSnakebar();
        snackbar = Snackbar.make(findViewById(R.id.base), text, duration);
        snackbar.show();
    }

    public void dismissSnakebar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
