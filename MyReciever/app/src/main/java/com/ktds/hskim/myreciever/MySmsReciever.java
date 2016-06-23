package com.ktds.hskim.myreciever;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MySmsReciever extends BroadcastReceiver {
    public MySmsReciever() {
    }

    /**
     * Android 문자메시지가 도착할 경우 실행
     * @param context
     * @param intent
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ImageView iv = new ImageView(context);
        Drawable d = context.getDrawable(R.mipmap.ic_launcher);

        iv.setImageDrawable(d);

        TextView tv = new TextView(context);
        tv.setText("문자 메시지가 도착했습니다");
        tv.setTextColor(Color.BLACK);

        ll.addView(iv);
        ll.addView(tv);
        Toast toast = Toast.makeText(context, "문자 도착", Toast.LENGTH_SHORT);
        toast.setView(ll);
        toast.show();
    }
}
