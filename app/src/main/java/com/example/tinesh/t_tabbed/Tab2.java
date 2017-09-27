package com.example.tinesh.t_tabbed;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Tinesh on 5/29/2017.
 */

public class Tab2 extends Fragment {
    CheckBox chklike,chkdislike,chkIdea;
    EditText txtlike,txtdislike,txtidea;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v3=inflater.inflate(R.layout.tab2,container,false);

        chklike = (CheckBox)v3.findViewById(R.id.cbLike);
        chkdislike = (CheckBox)v3.findViewById(R.id.cbDislike);
        chkIdea = (CheckBox)v3.findViewById(R.id.cbHaveidea);

        txtlike = (EditText)v3.findViewById(R.id.txtLike);
        txtdislike = (EditText)v3.findViewById(R.id.txtDontLike);
        txtidea = (EditText)v3.findViewById(R.id.txtHaveidea);

        addListenerOnChkIos();

        return v3;


    }

    private void addListenerOnChkIos() {

        chklike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    txtlike.setVisibility(VISIBLE);// perform logic
                    ((TabTin) getActivity()).showFloatingActionButton();
                }
                else {
                    txtlike.setVisibility(INVISIBLE);// perform logic
                }

            }
        });
        chkdislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    txtdislike.setVisibility(VISIBLE);// perform logic
                }
                else {
                    txtdislike.setVisibility(INVISIBLE);// perform logic
                }

            }
        });
        chkIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    txtidea.setVisibility(VISIBLE);// perform logic
                }
                else {
                    txtidea.setVisibility(INVISIBLE);// perform logic
                }

            }
        });
    }
}