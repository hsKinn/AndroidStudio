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
import com.ktds.hskim.kbolotte.players.PlayerFragment1;
import com.ktds.hskim.kbolotte.players.PlayerFragment2;
import com.ktds.hskim.kbolotte.players.PlayerFragment3;
import com.ktds.hskim.kbolotte.players.PlayerFragment4;
import com.ktds.hskim.kbolotte.players.PlayerFragment5;
import com.ktds.hskim.kbolotte.players.PlayerFragment6;


public class PlayersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PlayersFragment() {
        // Required empty public constructor
    }

    public static PlayersFragment newInstance(String param1, String param2) {
        PlayersFragment fragment = new PlayersFragment();
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

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    private Fragment playerFragment1;
    private Fragment playerFragment2;
    private Fragment playerFragment3;
    private Fragment playerFragment4;
    private Fragment playerFragment5;
    private Fragment playerFragment6;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_players, container, false);

        playerFragment1 = new PlayerFragment1();
        playerFragment2 = new PlayerFragment2();
        playerFragment3 = new PlayerFragment3();
        playerFragment4 = new PlayerFragment4();
        playerFragment5 = new PlayerFragment5();
        playerFragment6 = new PlayerFragment6();

        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        pager.setOffscreenPageLimit(6);
        pager.setCurrentItem(0);

        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        getActivity().setTitle("Players Fragment");

        return view;
    }

    private String[] pageTitle = {"Cheer Leader", "Coach", "Infielder", "OutFielder", "Catcher", "Pitcher"};

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
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
                return playerFragment1;
            }
            else if ( position == 1 ) {
                return playerFragment2;
            }
            else if ( position == 2)  {
                return playerFragment3;
            }
            else if ( position == 3) {
                return playerFragment4;
            }
            else if ( position == 4 ) {
                return playerFragment5;
            }
            else {
                return playerFragment6;
            }
        }

        /**
         * View Pager의 몇 개의 Fragment가 있는지 설정
         *
         * @return
         */
        @Override
        public int getCount() {
            return 6;
        }
    }

}
