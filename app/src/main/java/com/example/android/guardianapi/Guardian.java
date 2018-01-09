package com.example.android.guardianapi;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class Guardian {

    private String sectionName;

    private String webTitle;

    private String url;
    private String time;
    private String title;

    private String webPublicationDate;

    public Guardian(String sectionName, String webTitle, String url, String webPublicationDate, String Time) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        this.url = url;
        this.webPublicationDate = webPublicationDate;
        this.time = Time;
    }

    public Guardian(String sectionName, String webTitle, String url, String title, String webPublicationDate, String time) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        this.url = url;
        this.time = time;
        this.title = title;
        this.webPublicationDate = webPublicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTime() {
        return time;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getUrl() {
        return url;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }


}
