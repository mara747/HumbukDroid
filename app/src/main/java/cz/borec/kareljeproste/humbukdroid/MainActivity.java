package cz.borec.kareljeproste.humbukdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    public static final long REFRESH_SCREEN_TIME = 1000 * 60;

    public static SpannableString getTitleHumbukString(){
        String s="H U m B U K parta";
        SpannableString ss=  new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 1, 0);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 2, 3, 0);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(10,224,14)), 4, 5, 0);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(100,149,237)), 6, 7, 0);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(255,165,0)), 8, 9, 0);
        ss.setSpan(new ForegroundColorSpan(Color.YELLOW), 10, s.length(), 0);
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, s.length(), 0);
        return ss;
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getTitleHumbukString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle extras = getIntent().getExtras();
        int msgID;
        if (extras != null) {
            msgID = extras.getInt("msgID");
            mViewPager.setCurrentItem(msgID);
            getIntent().removeExtra("msgID");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        RetrieveRajceFeedTaskTimer myTask = new RetrieveRajceFeedTaskTimer();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, REFRESH_SCREEN_TIME, REFRESH_SCREEN_TIME);

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, RetrieveFeedsService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), RetrieveFeedsService.REPEAT_TIME, pintent);
    }

    private static String makeFragmentName(int viewPagerId, int index) {
        return "android:switcher:" + viewPagerId + ":" + index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("HumbukDroidPrefsName", 0);
        Boolean notifiMuted = settings.getBoolean("NotificationMute", false);
        menu.findItem(R.id.mute).setChecked(notifiMuted);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            for (int i = 0; i<mSectionsPagerAdapter.getCount();i++)
                {
                    String fragTag = makeFragmentName(mViewPager.getId(),i);
                    PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (fragment!=null)
                        fragment.retrieveFeedsTask(true);
                }
        }
        else if (id==R.id.mute)
        {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("HumbukDroidPrefsName", 0);
            SharedPreferences.Editor editor = settings.edit();
            if (item.isChecked() == true) {
                editor.putBoolean("NotificationMute",false);
                item.setChecked(false);
            } else {
                editor.putBoolean("NotificationMute",true);
                item.setChecked(true);
            }
            editor.commit();
        }
        else if (id==R.id.about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class RetrieveRajceFeedTaskTimer extends TimerTask {
        public void run() {
            for (int i = 0; i<mSectionsPagerAdapter.getCount();i++)
            {
                String fragTag = makeFragmentName(mViewPager.getId(),i);
                PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
                if (fragment!=null)
                {
                    fragment.retrieveFeedsTask(false);
                }
            }
        }
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
        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final int ARG_SECTION_KOMENTARE = 1;
        public static final int ARG_SECTION_CLANKY = 2;
        public static final int ARG_SECTION_KECALROOM = 3;
        public static final int ARG_SECTION_FOTO = 4;

        private BaseAdapter mBa;
        private RetrieveFeedTask mRFT;
        private List<Message> mMsgList;
        private boolean refreshOnResume=false;

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
        public void onResume()
        {
            if (refreshOnResume)
            {
                retrieveFeedsTask(true);
                refreshOnResume=false;
            }
            super.onResume();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            int secNum = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (secNum) {
                case ARG_SECTION_KOMENTARE:
                {
                    mBa = new MessagesAdapter(getContext(), mMsgList);
                    break;
                }
                case ARG_SECTION_CLANKY:
                {
                    mBa = new MessagesAdapter(getContext(), mMsgList);
                    break;
                }
                case ARG_SECTION_KECALROOM:
                {
                    mBa = new MessagesAdapter(getContext(), mMsgList);
                    break;
                }
                case ARG_SECTION_FOTO: {
                    mBa = new LazyAdapter(getContext(), mMsgList);
                    break;
                }
            }
            retrieveFeedsTask(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ((ListView) rootView.findViewById(R.id.listView)).setAdapter(mBa);
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fabComment);
            final int secNum = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (secNum) {
                case ARG_SECTION_KOMENTARE: {
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            refreshOnResume=true;
                            Intent intent = new Intent(getContext(), SendCommentActivity.class);
                            intent.putExtra(ARG_SECTION_NUMBER, secNum);
                            startActivity(intent);
                        }
                    });
                    break;
                }
                case ARG_SECTION_CLANKY: {
                    break;
                }
                case ARG_SECTION_KECALROOM: {
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            refreshOnResume=true;
                            Intent intent=new Intent(getContext(), SendCommentActivity.class);
                            intent.putExtra(ARG_SECTION_NUMBER, secNum);
                            startActivity(intent);
                        }
                    });
                    break;
                }
                case ARG_SECTION_FOTO: {
                    break;
                }
            }
            super.onActivityCreated(savedInstanceState);
        }

        public void retrieveFeedsTask(boolean aShowProgress){
            ProgressDialog pd = null;
            if (aShowProgress)
                pd = ProgressDialog.show(getContext(), "", getResources().getString(R.string.Loading), true);
            int secNum = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (secNum) {
                case ARG_SECTION_KOMENTARE:
                {
                    mRFT = new RetrieveFeedTask(mMsgList, mBa, getResources().getString(R.string.HumbukRssKomentare),pd,getContext().getApplicationContext(),RetrieveFeedsService.MSG_ID_KOMENTARE);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    break;
                }
                case ARG_SECTION_CLANKY:
                {
                    mRFT = new RetrieveFeedTask(mMsgList, mBa,getResources().getString(R.string.HumbukRssClanky),pd,getContext().getApplicationContext(),RetrieveFeedsService.MSG_ID_CLANKY);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    break;
                }
                case ARG_SECTION_KECALROOM:
                {
                    mRFT = new RetrieveFeedTask(mMsgList, mBa,getResources().getString(R.string.HumbukRssKecalroom),pd,getContext().getApplicationContext(),RetrieveFeedsService.MSG_ID_KECALROOM);
                    mRFT.setEncoding(Xml.Encoding.ISO_8859_1);
                    break;
                }
                case ARG_SECTION_FOTO: {
                    mRFT = new RetrieveRajceFeedTask(mMsgList, mBa, getResources().getString(R.string.HumbukRssRajce),pd, getContext().getApplicationContext(),RetrieveFeedsService.MSG_ID_FOTOGALERIE);
                    mRFT.setImgFeed(true);
                    break;
                }
            }
            mRFT.execute();
        }
    }
}
