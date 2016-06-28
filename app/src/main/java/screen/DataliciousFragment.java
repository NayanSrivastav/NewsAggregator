package screen;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import datalicious.com.news.R;

/**
 * Created by nayan on 27/6/16.
 */
public abstract class DataliciousFragment extends Fragment implements IRefreshListener {

    protected void showSnakebar(String text, int duration){
        ((DataliciousActivity)getActivity()).showSnakebar(text,duration);
    }
    protected void dismissSnakebar(){
        ((DataliciousActivity)getActivity()).dismissSnakebar();
    }

    @Override
    public void onRefresh() {

    }
    protected void stopRefreshing(){
        if(getParentFragment()!=null&&getParentFragment() instanceof IRefresher)
        {
            ((IRefresher)getParentFragment()).setRefresh(false);
        }
    }

    public void setRefreshEnabled(boolean set)
    {
        if(getParentFragment()!=null&&getParentFragment() instanceof IRefresher)
        {
            ((IRefresher)getParentFragment()).setRefreshEnabled(set);
        }
    }
}
