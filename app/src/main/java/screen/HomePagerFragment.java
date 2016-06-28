package screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import datalicious.com.news.R;

public class HomePagerFragment extends DataliciousFragment implements SwipeRefreshLayout.OnRefreshListener, IRefresher {
    private PagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pager, container, false);
        setUpView(view);
        return view;
    }

    @Override
    public void setRefreshEnabled(boolean set) {
        getView().findViewById(R.id.refresh_layout).setEnabled(set);
    }

    public void setUpView(View view) {
        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        adapter = new PagerAdapter(getChildFragmentManager(), tabLayout,this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(adapter);
        adapter.setTabIcons(tabLayout, 0);
        ((SwipeRefreshLayout) view.findViewById(R.id.refresh_layout)).setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        IRefreshListener iRefreshListener = adapter.getCurrentPage();
        if (iRefreshListener != null) {
            iRefreshListener.onRefresh();
        }
    }

    @Override
    public void setRefresh(boolean set) {
        ((SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout)).setRefreshing(set);
    }

    private static class Page {
        DataliciousFragment page;
        String title;

        public DataliciousFragment getPage() {
            return page;
        }

        public String getTitle() {
            return title;
        }

        public Page(DataliciousFragment page, String title) {
            this.page = page;
            this.title = title;
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        WeakReference<TabLayout> tabLayout;
        WeakReference<HomePagerFragment> homePagerFragment;
        List<Page> pages;
        int position;

        public PagerAdapter(FragmentManager fm, TabLayout tabLayout, HomePagerFragment homePagerFragment) {
            super(fm);
            this.tabLayout = new WeakReference<>(tabLayout);
            this.homePagerFragment=new WeakReference<>(homePagerFragment);
            pages = new ArrayList<>(4);
            pages.add(new Page(new BlogFeedFragment(), "Blog"));
            pages.add(new Page(new YouTubeFragment(), "YouTube"));
            pages.add(new Page(new YouTubeFragment(), "Twitter"));
            pages.add(new Page(new YouTubeFragment(), "Pinterest"));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages.get(position).getTitle();
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public DataliciousFragment getItem(int position) {
            return pages.get(position).getPage();
        }

        DataliciousFragment getCurrentPage() {
            return getItem(position);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 4;
        }

        private void setTabIcons(TabLayout tabLayout, int position) {
            if (position < tabLayout.getTabCount()) {
                int tabCount = 0;
                tabLayout.getTabAt(tabCount).setIcon(position == tabCount++ ? R.drawable.blog_red : R.drawable.blog);
                tabLayout.getTabAt(tabCount).setIcon(position == tabCount++ ? R.drawable.youtube_red : R.drawable.youtube);
                tabLayout.getTabAt(tabCount).setIcon(position == tabCount++ ? R.drawable.twitter_red : R.drawable.twitter);
                tabLayout.getTabAt(tabCount).setIcon(position == tabCount++ ? R.drawable.pinterest_red : R.drawable.pinterest);

            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //do nothing
        }


        @Override
        public void onPageSelected(int position) {
            setTabIcons(tabLayout.get(), position);
            this.position = position;
        }


        @Override
        public void onPageScrollStateChanged(int state) {
            if(homePagerFragment.get()!=null)
            homePagerFragment.get().setRefreshEnabled( state == ViewPager.SCROLL_STATE_IDLE );
        }
    }
}