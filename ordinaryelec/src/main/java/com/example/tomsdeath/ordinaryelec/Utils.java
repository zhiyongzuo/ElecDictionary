package com.example.tomsdeath.ordinaryelec;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


public final class Utils {

    public static boolean isNetWork(Context cxt) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) cxt
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static DictEntity JsonAnalysis(String strJson) {
        DictEntity dict = new DictEntity();
        JSONObject basic = null;
        JSONObject jso = null;
        JSONArray web = null;
        try {
            jso = new JSONObject(strJson);
            // 有道词典-基本词典
            try {
                basic = jso.getJSONObject("basic");
            } catch (Exception e) {
                System.err.println("没有有道词典-基本词典！");
            }

            try {
                // 有道词典-网络释义数据
                web = jso.getJSONArray("web");
            } catch (Exception e) {
                System.err.println("没有有道词典-网络释义数据！");
            }

            /************* 具体数据 ********************/
            try {
                // 错误代码
                int errorCode = jso.getInt("errorCode");
                dict.setErrorCode(errorCode);
            } catch (Exception e) {
                System.err.println("没有错误代码！");
            }
            try {
                // 有道翻译
                String translation = jso.getString("translation").substring(2,
                        jso.getString("translation").length() - 2);
                dict.setTranslation(translation);
            } catch (Exception e) {
                System.err.println("没有翻译！");
            }
            try {
                // 查询的词
                String query = jso.getString("query");
                dict.setQuery(query);
            } catch (Exception e) {
                System.err.println("查询的词为空！");
            }
            try {
                // 释义
                String explains = basic.getString("explains")
                        .substring(2, basic.getString("explains").length() - 2)
                        .replaceAll("\",\"", "\n");
                dict.setExplains(explains);
            } catch (Exception e) {
                System.err.println("没有释义！");
            }

            try {
                // 音标\拼音
                String phonetic = basic.getString("phonetic");
                dict.setPhonetic(phonetic);
            } catch (Exception e) {
                System.err.println("没有音标\\拼音！");
            }

            try {
                // 英式发音
                String uk_phonetic = basic.getString("uk-phonetic");
                dict.setUk_phonetic(uk_phonetic);
            } catch (Exception e) {
                System.err.println("没有英式发音！");
            }

            try {
                // 美式发音
                assert basic != null;
                String us_phonetic = basic.getString("us-phonetic");
                dict.setUs_phonetic(us_phonetic);
            } catch (Exception e) {
                System.err.println("没有美式发音！");
            }

            try {
                StringBuilder sb = new StringBuilder();
                assert web != null;
                for (int i = 0; i < web.length(); i++) {
                    JSONObject webv = (JSONObject) web.get(i);
                    String webkey = webv.getString("key");
                    String webvalue = webv.getString("value")
                            .substring(2, webv.getString("value").length() - 2)
                            .replaceAll("\",\"", ",");
                    String webInterpretation = webkey + "  " + webvalue + "\n";
                    sb.append(webInterpretation);
                }
                dict.setWebInterpretation(sb.toString());
            } catch (Exception e) {
                System.err.println("没有网络释义！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dict;
    }

    public static String getUrl(String q, Context cxt) {
        String[] keyfrom = cxt.getResources().getStringArray(R.array.key_form);
        int[] key = cxt.getResources().getIntArray(R.array.key);
        int j = (int) (Math.random() * keyfrom.length);
        final String doctype = "json";
        String loadURL = cxt.getString(R.string.url) + "keyfrom="
                + keyfrom[j] + "&key=" + key[j] + "&" + "type=data&doctype="
                + doctype + "&version=1.1&q=" + URLEncoder.encode(q);
        return loadURL;
    }
}
