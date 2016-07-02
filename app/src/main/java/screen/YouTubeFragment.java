package screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLoadingVideos();
    }

    private void startLoadingVideos() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getJSONResponse(getContext(), ID_URL + getString(R.string.youtube_key), this, ApiRequest.MethodTypes.GET, FETCH_UPLOAD_ID_TAG);
        } else {
            showSnakebar("No connection available", Snackbar.LENGTH_INDEFINITE);
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
                break;
        }
    }

    private void fetchDetails(String uploadId) {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getJSONResponse(getContext(), DETAIL_URL + uploadId + "&key=" + getString(R.string.youtube_key), this, ApiRequest.MethodTypes.GET, FETCH_UPLOAD_DETAILS_TAG);
        } else {
            showSnakebar(getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE);
        }
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        showSnakebar(ConnectionUtils.isConnected(getContext()) ? "Something went wrong! please retry" : getString(R.string.no_connection), Snackbar.LENGTH_LONG);
    }

    public static class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
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
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_you_tube, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            JSONObject item = response.optJSONObject(position);
            JSONObject snippet = item.optJSONObject("snippet");
            holder.tvDate.setText("");
            holder.tvDetails.setText("");
            holder.imgView.setImageDrawable(null);
            if (snippet != null) {
                final JSONObject thumbnails = snippet.optJSONObject("thumbnails");
                ImageUtil.setImage(holder.imgView.getContext(), getImageUrl(thumbnails, holder.imgView.getContext()), holder.imgView);
                holder.tvDate.setText(snippet.optString("publishedAt"));
                holder.tvDetails.setText(snippet.optString("title"));
            }
        }

        private String getImageUrl(JSONObject thumbnail, Context context) {
            String url = "";
            if (thumbnail != null) {
                switch (context.getResources().getDisplayMetrics().densityDpi) {
                    case DisplayMetrics.DENSITY_MEDIUM:
                        if (thumbnail.has("medium")) {
                            url = thumbnail.optJSONObject("medium").optString("url");
                        }
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        if (thumbnail.has("high")) {
                            url = thumbnail.optJSONObject("high").optString("url");

                        }
                        break;
                    case DisplayMetrics.DENSITY_XHIGH:
                        if (thumbnail.has("standard")) {
                            url = thumbnail.optJSONObject("standard").optString("url");
                        }
                        break;
                }
                url = url.isEmpty() ? (thumbnail.has("default") ? thumbnail.optJSONObject("default").optString("url") : "") : url;
            }
            return url;
        }

        @Override
        public int getItemCount() {
            return response.length();
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
