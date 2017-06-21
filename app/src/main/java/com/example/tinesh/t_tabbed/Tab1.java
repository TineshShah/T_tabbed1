package com.example.tinesh.t_tabbed;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.example.tinesh.t_tabbed.R.layout.tab1;

/**
 * Created by Tinesh on 5/29/2017.
 */

public class Tab1 extends Fragment  {
    TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(tab1,container,false);
        RelativeLayout fragment_linearlayout= (RelativeLayout)view.findViewById(R.id.tab1_layout);

        fragment_linearlayout.setOnLongClickListener(new View.OnLongClickListener() {//On long press

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(container.getContext(), "Long Pressed", Toast.LENGTH_SHORT).show();
                Log.e("Long press","Long press");
                return true;

            }});

        return view;
    }



   /* public void setTextViewText(String value){
        TextView textView = (TextView) getView().findViewById(R.id.textView1);
        textView.setText(value);
    }
    public TextView getTextView1(){
        return mTextView;
    }*/
}
