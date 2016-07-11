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

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import datalicious.com.news.R;

/**
 * Created by nayan on 11/7/16.
 */
public class ContactDetailsFragment extends DataliciousFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_details_fragment, container, false);
        initPhoneViews(view);
        initMailViews(view);
        initLocViews(view);
        return view;
    }

    private void initLocViews(View view) {
        LocClickListener locClickListener = new LocClickListener();
        view.findViewById(R.id.aus_address).setOnClickListener(locClickListener);
        view.findViewById(R.id.nz_address_1).setOnClickListener(locClickListener);
        view.findViewById(R.id.ger_address_1).setOnClickListener(locClickListener);
        view.findViewById(R.id.ind_address_1).setOnClickListener(locClickListener);
        view.findViewById(R.id.sk_address_1).setOnClickListener(locClickListener);
        view.findViewById(R.id.sng_address_1).setOnClickListener(locClickListener);
        view.findViewById(R.id.aus_address_1).setOnClickListener(locClickListener);
    }

    private void initMailViews(View view) {
        MailClickListener mailClickListener = new MailClickListener();
        view.findViewById(R.id.aus_email).setOnClickListener(mailClickListener);
        view.findViewById(R.id.aus_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.ger_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.ind_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.nz_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.sk_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.sng_email_1).setOnClickListener(mailClickListener);
        view.findViewById(R.id.uk_email_1).setOnClickListener(mailClickListener);
    }

    private void initPhoneViews(View view) {
        PhoneClickListener phoneClickListener = new PhoneClickListener();
        view.findViewById(R.id.aus_phone).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.aus_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.ger_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.ind_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.nz_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.sk_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.sng_phone_1).setOnClickListener(phoneClickListener);
        view.findViewById(R.id.uk_phone_1).setOnClickListener(phoneClickListener);
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
            phoneIntent.setData(Uri.parse("tel:" + ((TextView) v).getText().toString().replaceAll("\\s+", "")));
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
            Uri uri = Uri.parse("mailto:" + ((TextView) v).getText().toString());
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            v.getContext().startActivity(mailIntent);
        }
    }

    private static class LocClickListener implements View.OnClickListener {
        static final Map<Integer, LatLng> latLngLookUp = new HashMap<>(7);

        static {
            latLngLookUp.put(R.id.aus_address, new LatLng(-33.8408211, 151.20962970000005));
            latLngLookUp.put(R.id.aus_address_1, new LatLng(-37.8160774, 144.95849339999995));
            latLngLookUp.put(R.id.ger_address_1, new LatLng(52.39227899999999, 9.760010500000021));
            latLngLookUp.put(R.id.nz_address_1, new LatLng(-36.8445848, 174.76390130000004));
            latLngLookUp.put(R.id.ind_address_1, new LatLng(12.9135222, 77.63239539999995));
            latLngLookUp.put(R.id.sk_address_1, new LatLng(37.4909252, 127.02918399999999));
            latLngLookUp.put(R.id.sng_address_1, new LatLng(1.2799663, 103.84673339999995));
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            LatLng latLng = latLngLookUp.get(v.getId());
            if (latLng != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:-" + latLng.latitude + "," + latLng.longitude + "?" +
                                "q=" + latLng.latitude + "," + latLng.longitude));
                v.getContext().startActivity(intent);
            }
        }
    }
}
