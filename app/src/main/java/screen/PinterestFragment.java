package screen;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import datalicious.com.news.R;
import datalicious.com.news.utils.ApiCallback;
import datalicious.com.news.utils.ApiRequest;
import datalicious.com.news.utils.ConnectionUtils;
import datalicious.com.news.utils.Constants;
import datalicious.com.news.utils.ImageUtil;

/**
 * Created by nayan on 9/7/16.
 */
public class PinterestFragment extends ListFragment implements ApiCallback {

    private static final String URL = "http://pinterestapi.co.uk/datalicious/boards";
    private static final String PINTEREST_TAG = "pinterest_tag";
    private Adapter adapter;

    @Override
    protected void setUpFeed() {
        if (ConnectionUtils.isConnected(getContext())) {
            dismissSnakebar();
            ApiRequest.getJSONResponse(getContext(), URL, this, ApiRequest.MethodTypes.GET, PINTEREST_TAG);
        } else {
            showSnakebar(getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE);
            stopRefreshing();
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (adapter == null) {
            adapter = new Adapter();
        }
        return adapter;
    }

    @Override
    public void onResult(String response, int httpCode, String tag) {
        // do nothing
    }

    @Override
    public void onResult(JSONObject response, int httpCode, String tag) {
        if (httpCode == HttpURLConnection.HTTP_OK) {
            try {
                ((Adapter) getAdapter()).setData(response.getJSONArray("body"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        stopRefreshing();
    }

    @Override
    public void onError(String request, int httpCode, String tag) {
        stopRefreshing();
        showSnakebar(ConnectionUtils.isConnected(getContext()) ? "Something went wrong! please retry" : getString(R.string.no_connection), Snackbar.LENGTH_LONG);

    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements View.OnClickListener {

        private List<JSONObject> data;

        Adapter() {
            data = new ArrayList<>();
        }

        private void addItems(JSONArray data) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    this.data.add(data.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            notifyDataSetChanged();
        }

        private void setData(JSONArray data) {
            this.data.clear();
            addItems(data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.common_list_item_view, parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            JSONObject data = this.data.get(position);
            holder.imageView.setImageDrawable(null);
            String[] dataSplit = data.optString("href").split("/");
            holder.data.setText(dataSplit[dataSplit.length - 1].toUpperCase());
            ImageUtil.setImage(holder.imageView.getContext(), data.optString("src")
                    , holder.imageView, holder.itemView.findViewById(R.id.progress), 0);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(data.opt("href"));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (v.getContext() != null) {
                Intent intent=new Intent(v.getContext(),WebViewActivity.class);
                intent.putExtra(Constants.URL,"https://pinterest.com/" + v.getTag());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pinterest.com/" + v.getTag()));
//                v.getContext().startActivity(intent);
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView data;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                data = (TextView) itemView.findViewById(R.id.details);
                imageView = (ImageView) itemView.findViewById(R.id.img_view);
                itemView.findViewById(R.id.date).setVisibility(View.GONE);
            }

        }
    }
}
