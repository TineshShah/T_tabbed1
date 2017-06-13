package com.example.tinesh.t_tabbed;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Tinesh on 5/29/2017.
 */

public class Tab1 extends Fragment {
    TextView mTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab1,container,false);

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
