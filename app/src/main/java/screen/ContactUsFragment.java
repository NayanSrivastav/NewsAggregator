package screen;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import datalicious.com.news.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends DataliciousFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((HomePagerFragment) getParentFragment()).setRefreshEnabled(false);
        View view = inflater.inflate(R.layout.fragment_conntact_us, container, false);
        view.findViewById(R.id.call).setOnClickListener(this);
        view.findViewById(R.id.navigate).setOnClickListener(this);
        view.findViewById(R.id.email).setOnClickListener(this);
        view.findViewById(R.id.contact).setOnClickListener(this);
        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                call();
                break;
            case R.id.navigate:
                navigate();
                break;
            case R.id.email:
                openInquiry();
                break;
            case R.id.contact:
                openContactDetails();
                break;
        }
    }

    private void openContactDetails() {
        Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
        getActivity().startActivity(
                intent
        );
    }

    private void openInquiry() {
        Intent intent = new Intent(getActivity(), InquiryActivity.class);
        getActivity().startActivity(intent);
    }

    //http://maps.google.com/maps?q=90+Arthur+Street%2C+North+Sydney%2C+New+South+Wales%2C+Australia&ll=-33.8408211,151.20962970000005
    private void navigate() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:-33.8408211,151.20962970000005?q=-33.8408211,151.20962970000005(Datalicious Sydney)"));
        startActivity(intent);
    }

    private void call() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:+61 2 8999 5815"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(getChildFragmentManager().findFragmentById(R.id.map_fragment));
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
