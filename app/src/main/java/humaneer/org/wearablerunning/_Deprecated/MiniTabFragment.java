package humaneer.org.wearablerunning._Deprecated;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.Fragment.DataFragment;
import humaneer.org.wearablerunning.Fragment.FriendFragment;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.SlidingTabLayout;

/**
 * Created by Minki on 2017-03-08.
 */

public class MiniTabFragment extends Fragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mini_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        mViewPager.setCurrentItem(1);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FriendFragment();
                case 1:
                    return new MainFragment();
                case 2:
                    return new DataFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            // First : Friend Page
            // Second : Main Page.
            // Third : Data Page.
            return 3;
        }
    }
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return object == view;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Friend";
//                case 1:
//                    return "Main";
//                case 2:
//                    return "Data";
//            }
//            return null;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            View view = null;
//            switch (position) {
//                case 0:
//                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_friend, container, false);
////                    fragment = FriendFragment.newInstance();
//                    break;
//                case 1:
//                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_main, container, false);
//
////                    fragment = MainActivity.MainFragment.newInstance();
//                    Button button = (Button) view.findViewById(R.id.button2);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Log.d("### Start Clicked ###", "S111111111d" );
//                            Toast.makeText(getActivity(), "Main Start Test.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    break;
//                case 2:
//                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_data, container, false);
////                    fragment = DataFragment.newInstance();
//                    break;
//            }
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View)object);
//        }
//    }

}
