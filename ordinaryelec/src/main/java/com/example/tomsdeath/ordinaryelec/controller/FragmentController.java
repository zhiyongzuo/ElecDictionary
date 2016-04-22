package com.example.tomsdeath.ordinaryelec.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.webkit.WebViewFragment;

import com.example.tomsdeath.ordinaryelec.Fragment.LocalFragment;
import com.example.tomsdeath.ordinaryelec.Fragment.WebFragment;

import java.util.ArrayList;

/**
 * Created by tomsdeath on 2016/3/23.
 */
public class FragmentController {
    //ArrayList<Fragment> fragments;
    Activity activity;
    int containViewId;

    public FragmentController(Activity activity, int containViewId) {
        this.activity = activity;
        this.containViewId = containViewId;
        //fragments.add(0, new WebViewFragment());
        //fragments.add(1, new LocalFragment());
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(containViewId, fragment).commit();

    }
}
