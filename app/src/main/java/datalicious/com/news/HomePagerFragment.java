package datalicious.com.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomePagerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pager, container, false);
        setUpView(view);
        return view;
    }


    public void setUpView(View view) {
        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), tabLayout);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(adapter);
        adapter.setTabIcons(tabLayout,0);
    }

    private static class Page {
        Fragment page;
        String title;

        public Page(Fragment page, String title) {
            this.page = page;
            this.title = title;
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        WeakReference<TabLayout> tabLayout;
        List<Page> pages;

        public PagerAdapter(FragmentManager fm, TabLayout tabLayout) {
            super(fm);
            this.tabLayout = new WeakReference<>(tabLayout);
            pages = new ArrayList<>(4);
            pages.add(new Page(new YouTubeFragment(), "Blog"));
            pages.add(new Page(new YouTubeFragment(), "YouTube"));
            pages.add(new Page(new YouTubeFragment(), "Twitter"));
            pages.add(new Page(new YouTubeFragment(), "Pinterest"));
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return pages.get(position).page;
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
        }


        @Override
        public void onPageScrollStateChanged(int state) {
            // do nothing
        }
    }
}