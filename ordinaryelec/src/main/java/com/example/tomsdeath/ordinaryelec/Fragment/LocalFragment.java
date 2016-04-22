package com.example.tomsdeath.ordinaryelec.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomsdeath.ordinaryelec.R;

public class LocalFragment extends Fragment {
    String result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, null);
        TextView textView = (TextView)view.findViewById(R.id.result);
        if (result != null) {
            textView.setText(result);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey("key")) {
            result = getArguments().getString("key");
        }
    }

    public LocalFragment() {
    }
}
