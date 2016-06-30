package com.ktds.junho.mymultifragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class SecondFragment extends Fragment {
    private static final String NUMBER_1 = "param1";
    private static final String NUMBER_2 = "param2";
    private static final String NUMBER_3 = "param3";

    private int number1;
    private int number2;
    private int number3;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(int number1, int number2, int number3) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt(NUMBER_1, number1);
        args.putInt(NUMBER_2, number2);
        args.putInt(NUMBER_3, number3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number1 = getArguments().getInt(NUMBER_1, 0);
            number2 = getArguments().getInt(NUMBER_2, 0);
            number3 = getArguments().getInt(NUMBER_3, 0);
        }
    }

    private Button nextFragment;
    private TextView tvNumber1;
    private TextView tvNumber2;
    private TextView tvNumber3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,container,false);

        tvNumber1 = (TextView) view.findViewById(R.id.tvNumber1);
        tvNumber2 = (TextView) view.findViewById(R.id.tvNumber2);
        tvNumber3 = (TextView) view.findViewById(R.id.tvNumber3);

        tvNumber1.setText(number1 + "");
        tvNumber2.setText(number2 + "");
        tvNumber3.setText(number3 + "");

        nextFragment = (Button) view.findViewById(R.id.nextFragment);
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplacable) getActivity()).replaceFragment(ThirdFragmen.newInstance(
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1,
                        new Random().nextInt(10) + 1));
            }
        });
        return view;
    }

}
