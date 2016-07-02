package screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import datalicious.com.news.R;

/**
 * Created by nayan on 2/7/16.
 */
public abstract class ListFragment extends DataliciousFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layoout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rec_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(getAdapter());
        setUpListScroll(recyclerView);
        return view;
    }

    protected void setUpListScroll(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                setRefreshEnabled(((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpListScroll( (RecyclerView) getView().findViewById(R.id.rec_view));
    }

    abstract protected RecyclerView.Adapter getAdapter();
}
