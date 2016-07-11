package screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import datalicious.com.news.R;
import datalicious.com.news.utils.Constants;
import datalicious.com.news.utils.ImageUtil;

/**
 * Created by nayan on 11/7/16.
 */
public class TwitterDetailFragment extends DataliciousFragment {
    public static Fragment getInstance(String tweet) {
        Fragment fragment = new TwitterDetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TWEET, tweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_detail_layout, container, false);
        try {
            JSONObject tweetDetails = new JSONObject(getArguments().getString("tweet"));
            TextView name = (TextView) view.findViewById(R.id.name_location);
            JSONObject user = tweetDetails.optJSONObject("user");
            StringBuilder nameAndLocation = new StringBuilder();
            nameAndLocation.append(user.getString("name"));
            if (user.has("screen_name")) {
                nameAndLocation.append(" @").append(user.getString("screen_name"));
            }
            if (user.has("location")) {
                nameAndLocation.append(" ,").append(user.getString("location"));
            }
            name.setText(nameAndLocation.toString());
            TextView desc = (TextView) view.findViewById(R.id.desc);
            if (user.has("description")) {
                desc.setText(user.optString("description"));

            } else {
                desc.setVisibility(View.GONE);
            }
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(tweetDetails.optString("text"));
            Linkify.addLinks(text, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
            ImageView imageView= (ImageView) view.findViewById(R.id.img_view);
            ImageUtil.setImage(getContext(), user.optString("profile_image_url")
                    , imageView, view.findViewById(R.id.progress), 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
