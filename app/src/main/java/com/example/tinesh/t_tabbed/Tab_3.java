package com.example.tinesh.t_tabbed;

import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;
import static com.example.tinesh.t_tabbed.R.layout.tab3;
import static com.example.tinesh.t_tabbed.R.layout.tab_3;

/**
 * Created by Tinesh on 5/29/2017.
 */

public class Tab_3 extends Fragment  {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(tab3,container,false);

//        RelativeLayout fragment_linearlayout= (RelativeLayout)view.findViewById(R.id.tab1_layout);
//
//        fragment_linearlayout.setOnLongClickListener(new View.OnLongClickListener() {//On long press
//
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(container.getContext(), "Long Pressed", Toast.LENGTH_SHORT).show();
//                Log.e("Long press","Long press");
//                return true;
//
//            }});

        return view;
    }

}
