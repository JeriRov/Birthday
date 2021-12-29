package com.example.birthday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CompactInfo extends Fragment{

    String loop[];
    TextView nameView,dataView,moreView;

    public CompactInfo(String[] loop){
        this.loop = loop;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.info_compact, container, false);
        nameView = (TextView)root.findViewById(R.id.nameView);
        dataView = (TextView)root.findViewById(R.id.dataView);
        moreView = (TextView)root.findViewById(R.id.moreView);
        nameView.setText(loop[0]);
        dataView.setText(loop[1]);
        moreView.setText(loop[2]);
        return root;
    }

    public String getName(){
        return loop[0];
    }

    public String getData(){
        return loop[1].substring(0, loop[1].lastIndexOf("."));
    }
}
