package com.ktds.hskim.kbolotte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ktds.hskim.kbolotte.catchphrase.CatchphraseFragment1;
import com.ktds.hskim.kbolotte.catchphrase.CatchphraseFragment2;
import com.ktds.hskim.kbolotte.catchphrase.CatchphraseFragment3;
import com.ktds.hskim.kbolotte.catchphrase.CatchphraseFragment4;

public class CatchphraseFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CatchphraseFragment() {
        // Required empty public constructor
    }

    public static CatchphraseFragment newInstance(String param1, String param2) {
        CatchphraseFragment fragment = new CatchphraseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ViewPager pager2;
    private PagerSlidingTabStrip tabs2;

    private Fragment catchphraseFragment1;
    private Fragment catchphraseFragment2;
    private Fragment catchphraseFragment3;
    private Fragment catchphraseFragment4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catchphrase, container, false);

        catchphraseFragment1 = new CatchphraseFragment1();
        catchphraseFragment2 = new CatchphraseFragment2();
        catchphraseFragment3 = new CatchphraseFragment3();
        catchphraseFragment4 = new CatchphraseFragment4();

        pager2 = (ViewPager) view.findViewById(R.id.pager2);
        pager2.setAdapter(new PagerAdapter(getChildFragmentManager()));
        pager2.setOffscreenPageLimit(4);
        pager2.setCurrentItem(0);

        tabs2 = (PagerSlidingTabStrip) view.findViewById(R.id.tabs2);
        tabs2.setViewPager(pager2);

        getActivity().setTitle("Catchphrase Fragment");

        return view;
    }

    private String[] pageTitle2 = {"2016", "2015", "2014", "2013"};

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle2[position];
        }

        /**
         * View Pager의 Fragment들은 각각 Index를 가진다
         * Android OS로 부터 요청된 Pager의 Index를 보내주면
         * 해당되는 Fragment를 리턴
         *
         * * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {

            if ( position == 0 ) {
                return catchphraseFragment1;
            }
            else if ( position == 1 ) {
                return catchphraseFragment2;
            }
            else if ( position == 2)  {
                return catchphraseFragment3;
            }
            else  {
                return catchphraseFragment4;
            }
        }

        /**
         * View Pager의 몇 개의 Fragment가 있는지 설정
         *
         * @return
         */
        @Override
        public int getCount() {
            return 4;
        }
    }
}
