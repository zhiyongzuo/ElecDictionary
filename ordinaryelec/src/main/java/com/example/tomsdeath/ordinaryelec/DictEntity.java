package com.example.tomsdeath.ordinaryelec;


public class DictEntity {
    // 错误代码
    private int errorCode;
    // 有道翻译
    private String translation;
    // 查询的词
    private String query;
    // 释义
    private String explains;
    // 音标\拼音
    private String phonetic;
    // 英式发音
    private String uk_phonetic;
    // 美式发音
    private String us_phonetic;
    // 网络释义
    private String webInterpretation;

    public DictEntity() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public String getWebInterpretation() {
        return webInterpretation;
    }

    public void setWebInterpretation(String webInterpretation) {
        this.webInterpretation = webInterpretation;
    }


}