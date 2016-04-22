package com.example.tomsdeath.ordinaryelec.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tomsdeath on 2016/3/30.
 */
public class Foo01 {
    private int errorCode;
    private String query;
    private String[] translation;
    private Map<String, Object> basic;
    private List<Map<String, Object>> web;

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public String getQuery() {
        return query;
    }
    public void setTranslation(String[] translation) {
        this.translation = translation;
    }
    public String[] getTranslation() {
        return translation;
    }
    public void setBasic(Map<String, Object> basic) {
        this.basic = basic;
    }
    public Map<String, Object> getBasic() {
        return basic;
    }
    public void setWeb(List<Map<String, Object>> web) {
        this.web = web;
    }
    public List<Map<String, Object>> getWeb() {
        return web;
    }

    public String getPhonetic() {
        if(basic==null) {
            return null;
        }
        String phonetic = (String)basic.get("phonetic");
        return phonetic;
    }
    public String getUkPhonetic() {
        if(basic==null) {
            return null;
        }
        String ukPhonetic = (String)basic.get("uk-phonetic");
        return ukPhonetic;
    }
    public String getUsPhonetic() {
        if(basic==null) {
            return null;
        }
        String usPhonetic = (String)basic.get("us-phonetic");
        return usPhonetic;
    }
    public String getExplains() {
        String strExplains = null;
        if(basic==null) {
            return strExplains;
        }
        List<String> explains = (List<String>)basic.get("explains");
        for(String string : explains) {
            strExplains += string + "\n";
        }
        return strExplains;
    }
    public String getWebResult() {
        String webResult = null;
        if(web==null) {
            return webResult;
        }
        for(Map<String, Object> map : web) {
            String webKey = (String)map.get("key");
            webResult += webKey + ":";
            List<String> webValue = (List<String>)map.get("value");
            for(String str : webValue) {
                webResult += str + ";";
            }
            webResult += "\n";
        }
        return webResult;
    }
    public String getTranslationResult() {
        if(translation.length >0) {
            String strTranslation = null;
            for(String string : translation) {
                strTranslation += string;
            }
            return strTranslation;
        }
        return null;
    }

    @Override
    public String toString() {
        String string = null;
        if (errorCode != 0) {
            string = "错误代码：" + errorCode + "\n";
        } else {
            String translation = getTranslationResult();
            if (translation != null) {
                translation = translation.substring(0, translation.length() - 1);
                if(!translation.equals(query)) {
                    string = (query + ":" + getTranslationResult() + "\n");
                }
            }
            if (getPhonetic() != null) {
                if (string == null) {
                    string = "";
                }
                string += (getPhonetic() + "\n");
            }
            if (getExplains() != null) {
                if (string == null) {
                    string = "";
                }
                string += (getExplains());
            }
            if (getWebResult() != null) {
                if (string == null) {
                    string = "";
                }
                string+="网络释义：\n";
                string += (getWebResult());
            }
        }
        if(string == null)
        {
            string = "你选的内容："+query+"\n这是个毛线！！！\n翻译不了！";
        }
        return string;
    }
}
