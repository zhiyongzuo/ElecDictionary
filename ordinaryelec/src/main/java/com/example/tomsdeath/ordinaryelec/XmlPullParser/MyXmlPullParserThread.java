package com.example.tomsdeath.ordinaryelec.XmlPullParser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.example.tomsdeath.ordinaryelec.MainActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomsdeath on 2016/4/13.
 */
public class MyXmlPullParserThread{
    InputStream inputStream;
    URL url;
    List<Translation> list;
    Translation translation;
    String return_s;

    public static boolean isNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if(info!=null) {
            return true;
        }
        return false;
    }

    public List<Translation> parser(String s) {
        try {
            String strURL = "http://fanyi.youdao.com/openapi.do?keyfrom=" + "fgfdgdfg" + "&key="
                    + "1612287005" + "&type=data&doctype=xml&version=1.1&q=" + URLEncoder.encode(s);
            url = new URL(strURL);
            HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            //huc.setRequestProperty("accept", "*/*");
            //huc.setRequestProperty("connection", "Keep-Alive");
            //huc.setRequestProperty("user-agent", "Mozilla/4.0{compatible; MSIE 6.0; Windows NT 5.1; SV1}");
            huc.connect();//要有网络连接，否则卡在这步
            inputStream = huc.getInputStream();
            Log.v("MXPPT", inputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            list = new ArrayList<>();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String localName;
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        translation = new Translation();
                        break;
                    case XmlPullParser.START_TAG:
                        localName = xmlPullParser.getName();
                        if("paragraph".equals(localName)) {
                            if (xmlPullParser.getText()==null) { //what would happen if it encounters in fact a cdsect
                                xmlPullParser.next();
                                translation.putParagraph(xmlPullParser.getText());
                                Log.e("SA", "test" + translation.getParagraph());
                               return_s = xmlPullParser.getText();
                            }
                        } else if("errorCode".equals(localName)) {
                            translation.putErrorCode(Integer.valueOf(xmlPullParser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        localName = xmlPullParser.getName();
                        if("translation".equals(localName)) {
                            list.add(translation);
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch(IOException io) {
            io.printStackTrace();
        }
        Log.e("SA", "test22" + return_s);
        return list;
    }

    public class Translation {
        String paragraph;
        String query;
        int errorCode;

        void putParagraph(String paragraph) {
            this.paragraph = paragraph;
        }
        void putQuery(String query) {
            this.query = query;
        }
        void putErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getParagraph() {
            return paragraph;
        }
        String getQuery() {
            return query;
        }
        int getErrorCode() {
            return errorCode;
        }
    }
}
