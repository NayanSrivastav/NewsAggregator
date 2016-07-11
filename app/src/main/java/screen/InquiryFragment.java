package screen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import datalicious.com.news.R;
import datalicious.com.news.utils.ConnectionUtils;

/**
 * Created by nayan on 11/7/16.
 */
public class InquiryFragment extends DataliciousFragment implements View.OnClickListener {

    EditText name, email, phone, enquiry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inquiry_layout, container, false);
        view.findViewById(R.id.send).setOnClickListener(this);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        phone = (EditText) view.findViewById(R.id.phone);
        enquiry = (EditText) view.findViewById(R.id.enquiry);
        return view;
    }

    private void sendMail() {
        if (ConnectionUtils.isConnected(getContext())) {
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("please wait");
//            progressDialog.show();
            StringBuilder body = new StringBuilder(enquiry.getText().toString());
            body.append("\n");
            body.append("From: ").append(name.getText().toString()).append("\n").append("Phone: ").append(phone.getText().toString()).append("\n");
            body.append("email: ").append(email.getText().toString());
            BackgroundMail.newBuilder(getActivity()).withUsername("aggregator.news.app@gmail.com")
                    .withPassword("datalicious1").withMailto("nayan20081991@gmail.com").
                    withSubject("mail from news-aggregator app").withBody(body.toString()).withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                @Override
                public void onSuccess() {
                    InquiryFragment.this.onSuccess();
                }
            }).withOnFailCallback(new BackgroundMail.OnFailCallback() {
                @Override
                public void onFail() {
                    InquiryFragment.this.onFail();
                }
            }).withProcessVisibility(true).send();
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_LONG);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(name.getText().toString())) {
            if (!TextUtils.isEmpty(phone.getText().toString())) {
                if (!TextUtils.isEmpty(email.getText().toString())
                        ) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        if (!TextUtils.isEmpty(enquiry.getText().toString())) {
                            sendMail();
                        } else {
                            enquiry.setError("Enter enquiry");
                            enquiry.requestFocus();
                        }
                    } else {
                        email.setError("Enter valid email-id");
                        email.requestFocus();
                    }
                } else {
                    email.setError("Enter email");
                    email.requestFocus();
                }
            } else {
                phone.setError("Enter phone");
                phone.requestFocus();
            }
        } else {
            name.setError("Enter name");
            name.requestFocus();
        }
    }

    public void onSuccess() {
        if (getContext() != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Query received");
            builder.setMessage("Thanks and we will get back to you shorty.");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            builder.create().show();
        }
    }

    public void onFail() {
        if (getContext() != null) {

            showSnakebar("Something went wrong! please retry", Snackbar.LENGTH_LONG);
        }
    }
}
