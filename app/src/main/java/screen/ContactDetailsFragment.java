package screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import datalicious.com.news.R;

/**
 * Created by nayan on 11/7/16.
 */
public class ContactDetailsFragment extends DataliciousFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_details_fragment, container, false);
        PhoneClickListener phoneClickListener = new PhoneClickListener();
        view.findViewById(R.id.aus_phone).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.aus_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.ger_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.ind_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.nz_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.sk_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.sng_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.uk_phone_1).setOnClickListener(phoneClickListener);
        return view;
    }

    private static class PhoneClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+((TextView) v).getText().toString().replaceAll("\\s+","")));
            if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            v.getContext().startActivity(phoneIntent);
        }
    }

    private static class MailClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:"+((TextView) v).getText().toString().replaceAll("\\s+","")));
            if (phoneIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                v.getContext().startActivity(phoneIntent);
            }
        }
    }
}
