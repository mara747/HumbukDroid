package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s="H U m B U K parta";
        SpannableString ss=  new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 1, 0);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 2, 3, 0);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(10,224,14)), 4, 5, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 6, 7, 0);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(255,165,0)), 8, 9, 0);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 10, 17, 0);
        setTitle(ss);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //mProgressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.LoadingKomentare), true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hudba budoucnosti :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "KOMENTÁŘ";
                case 1:
                    return "ČLÁNKY";
                case 2:
                    return "KECAL ROOM";
                case 3:
                    return "FOTO";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private RetrieveFeedTask mRFT;
        private List<Message> mMsgList;
        private boolean mFirstStart = true;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
            mMsgList = new ArrayList<Message>();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int secNum = getArguments().getInt(ARG_SECTION_NUMBER);

            BaseAdapter ba;

            switch (secNum) {
                case 1:
                {
                    ProgressDialog pd = null;
                    if (mFirstStart) {
                        pd = ProgressDialog.show(inflater.getContext(), "", getResources().getString(R.string.Loading), true);
                        mFirstStart=false;
                    }
                    ba = new MessagesAdapter(inflater.getContext(), mMsgList);
                    mRFT = new RetrieveFeedTask(mMsgList, ba, getResources().getString(R.string.HumbukRssKomentare),pd);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    ((ListView) rootView.findViewById(R.id.listView)).setAdapter(ba);
                    break;
                }
                case 2:
                {
                    ba = new MessagesAdapter(inflater.getContext(), mMsgList);
                    mRFT = new RetrieveFeedTask(mMsgList, ba,getResources().getString(R.string.HumbukRssClanky),null);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    ((ListView) rootView.findViewById(R.id.listView)).setAdapter(ba);
                    break;
                }
                case 3:
                {
                    ba = new MessagesAdapter(inflater.getContext(), mMsgList);
                    mRFT = new RetrieveFeedTask(mMsgList, ba,getResources().getString(R.string.HumbukRssKecalroom),null);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    ((ListView) rootView.findViewById(R.id.listView)).setAdapter(ba);
                    break;
                }
                case 4: {
                    ba = new LazyAdapter(inflater.getContext(), mMsgList);
                    mRFT = new RetrieveRajceFeedTask(mMsgList,ba, getResources().getString(R.string.HumbukRssRajce),null);
                    mRFT.setImgFeed(true);
                    ((ListView) rootView.findViewById(R.id.listView)).setAdapter(ba);
                    break;
                }
            }

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mRFT.execute();
        }
    }
}
