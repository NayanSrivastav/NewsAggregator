package screen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import datalicious.com.news.R;
import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;
import datalicious.com.news.utils.Constants;
import datalicious.com.news.utils.ImageUtil;

public class YouTubeFragment extends ListFragment implements ApiCallback {
    private static final String FETCH_UPLOAD_ID_TAG = "fetch_upload_id_tag";
    private static final String FETCH_UPLOAD_DETAILS_TAG = "fetch_upload_details_tag";
    private static final String ID_URL = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=datalicious&key=";
    private static final String DETAIL_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults" +
            "=50&playlistId=";
    private FeedAdapter adapter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adapter = new FeedAdapter();
        return adapter;
    }

    protected void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getJSONResponse(getContext(), ID_URL + getString(R.string.youtube_key), this, ApiRequest.MethodTypes.GET, FETCH_UPLOAD_ID_TAG);
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {

    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        switch (tag) {
            case FETCH_UPLOAD_ID_TAG:
                JSONArray items = response.optJSONArray("items");
                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.optJSONObject(i);
                        if (item != null) {
                            JSONObject contentDetails = item.optJSONObject("contentDetails");
                            if (contentDetails != null && contentDetails.has("relatedPlaylists")) {
                                JSONObject relatedPlayLists = contentDetails.optJSONObject("relatedPlaylists");
                                String uploadId = relatedPlayLists
                                        .optString("uploads");
                                fetchDetails(uploadId);
                            }
                        }
                    }
                }
                break;
            case FETCH_UPLOAD_DETAILS_TAG:
                if (response.has("items")) {
                    adapter.addItem(response.optJSONArray("items"));
                }
                stopRefreshing();
                break;
        }
    }

    private void fetchDetails(String uploadId) {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getJSONResponse(getContext(), DETAIL_URL + uploadId + "&key=" + getString(R.string.youtube_key), this, ApiRequest.MethodTypes.GET, FETCH_UPLOAD_DETAILS_TAG);
        } else {
            showSnakebar(getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        stopRefreshing();
        showSnakebar(ConnectionUtils.isConnected(getContext()) ? "Something went wrong! please retry" : getString(R.string.no_connection), Snackbar.LENGTH_LONG);
    }

    public static class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>implements View.OnClickListener {
        JSONArray response;
        public FeedAdapter() {
            this.response = new JSONArray();
        }

        public void addItem(JSONArray response) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    this.response.put(response.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_list_item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            JSONObject item = response.optJSONObject(position);
            JSONObject snippet = item.optJSONObject("snippet");
            holder.tvDate.setText("");
            holder.tvDetails.setText("");
            holder.imgView.setImageDrawable(null);
            if (snippet != null) {
                holder.itemView.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                holder.imgView.setVisibility(View.GONE);
                final JSONObject thumbnails = snippet.optJSONObject("thumbnails");
                ImageUtil.setImage(holder.imgView.getContext(), getImageUrl(thumbnails, holder.imgView.getContext())
                        , holder.imgView,holder.itemView.findViewById(R.id.progress),0);
                holder.tvDate.setText(snippet.optString("publishedAt"));
                holder.tvDetails.setText(snippet.optString("title"));
                holder.itemView.setOnClickListener(this);
                holder.itemView.setTag(item.optJSONObject("contentDetails").optString("videoId"));
            }
        }

        private String getImageUrl(JSONObject thumbnail, Context context) {
            String url = "";
            if (thumbnail != null) {
                switch (context.getResources().getDisplayMetrics().densityDpi) {
                    case DisplayMetrics.DENSITY_MEDIUM:
                        if (thumbnail.has("medium")) {
                            url = thumbnail.optJSONObject("medium").optString(Constants.URL);
                        }
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        if (thumbnail.has("high")) {
                            url = thumbnail.optJSONObject("high").optString(Constants.URL);

                        }
                        break;
                    case DisplayMetrics.DENSITY_XHIGH:
                        if (thumbnail.has("standard")) {
                            url = thumbnail.optJSONObject("standard").optString(Constants.URL);
                        }
                        break;
                }
                url = url.isEmpty() ? (thumbnail.has("default") ? thumbnail.optJSONObject("default").optString(Constants.URL) : "") : url;
            }
            return url;
        }

        @Override
        public int getItemCount() {
            return response.length();
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getContext(),WebViewActivity.class);
            intent.putExtra("url","https://www.youtube.com/watch?v="+v.getTag() );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);

//            Intent videoClient = new Intent(Intent.ACTION_VIEW);
//            videoClient.setData(Uri.parse("https://www.youtube.com/watch?v="+v.getTag()));
//            v.getContext().startActivity(videoClient);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgView;
            TextView tvDate, tvDetails;

            public ViewHolder(View itemView) {
                super(itemView);
                tvDate = (TextView) itemView.findViewById(R.id.date);
                tvDetails = (TextView) itemView.findViewById(R.id.details);
                imgView = (ImageView) itemView.findViewById(R.id.img_view);
            }
        }
    }
}
