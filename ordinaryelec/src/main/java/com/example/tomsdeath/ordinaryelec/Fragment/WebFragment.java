package com.example.tomsdeath.ordinaryelec.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tomsdeath.ordinaryelec.DictEntity;
import com.example.tomsdeath.ordinaryelec.R;
import com.example.tomsdeath.ordinaryelec.Utils;
import com.example.tomsdeath.ordinaryelec.json.Foo01;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WebFragment extends Fragment {
    @Bind(R.id.ll_phonetic)
    LinearLayout ll_phonetic;
    @Bind(R.id.ll_us_uk_phonetic)
    LinearLayout ll_us_uk_phonetic;
    @Bind(R.id.ll_explains)
    LinearLayout ll_explains;
    @Bind(R.id.ll_translation)
    LinearLayout ll_translation;
    @Bind(R.id.ll_web)
    LinearLayout ll_web;
    @Bind(R.id.tv_q)
    TextView tv_query;
    @Bind(R.id.tv_phonetic)
    TextView tv_phonetic;
    @Bind(R.id.tv_us_phonetic)
    TextView tv_us_phonetic;
    @Bind(R.id.tv_uk_phonetic)
    TextView tv_uk_phonetic;
    @Bind(R.id.tv_explains)
    TextView tv_explains;
    @Bind(R.id.tv_translation)
    TextView tv_translation;
    @Bind(R.id.tv_web)
    TextView tv_web;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web, null);
        ButterKnife.bind(this, view);
        String s = getActivity().getSharedPreferences("dict", Context.MODE_PRIVATE).getString("dict", null);
        Foo01 foo01 = new Gson().fromJson(s, Foo01.class);
        translate(foo01);
        return view;
    }

    private void showLL() {
        ll_explains.setVisibility(View.VISIBLE);
        ll_us_uk_phonetic.setVisibility(View.VISIBLE);
        ll_phonetic.setVisibility(View.VISIBLE);
        ll_translation.setVisibility(View.VISIBLE);
        ll_web.setVisibility(View.VISIBLE);
    }

    private void translate(Foo01 f) {
        showLL();
        // 查询的词
        tv_query.setText(f.getQuery());
        // 美式发音
        String us_phonetic = f.getUsPhonetic();
        // 英式发音
        String uk_phonetic = f.getUkPhonetic();
        if (us_phonetic == null && uk_phonetic == null) {
            ll_us_uk_phonetic.setVisibility(View.GONE);
            // 拼音
            String phonetic = f.getPhonetic();
            if (phonetic == null) {
                ll_phonetic.setVisibility(View.GONE);
            } else {
                ll_phonetic.setVisibility(View.VISIBLE);
                tv_phonetic.setText(phonetic);
            }
        } else {
            ll_us_uk_phonetic.setVisibility(View.VISIBLE);
            ll_phonetic.setVisibility(View.GONE);
            tv_uk_phonetic.setText("[ " + uk_phonetic + " ]");
            tv_us_phonetic.setText("[ " + us_phonetic + " ]");
        }
        // 有道翻译
        String translation = f.getTranslationResult();
        if (translation == null) {
            ll_translation.setVisibility(View.GONE);
        } else {
            ll_translation.setVisibility(View.VISIBLE);
            tv_translation.setText(translation);
        }
        // 释义
        String explains = f.getExplains();
        if (explains == null) {
            ll_explains.setVisibility(View.GONE);
        } else {
            ll_explains.setVisibility(View.VISIBLE);
            tv_explains.setText(explains);
        }
        // 网络释义
        String webInterpretation = f.getWebResult();
        if (webInterpretation == null) {
            ll_web.setVisibility(View.GONE);
        } else {
            ll_web.setVisibility(View.VISIBLE);
            tv_web.setText(webInterpretation);
        }
    }
}
