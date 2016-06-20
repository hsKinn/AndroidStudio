package com.ktds.hskim.mymemoapp.vo;

/**
 * Created by 206-006 on 2016-06-20.
 */
public class Memo {

    private int _id;
    private String subject;
    private String content;
    private String date;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
