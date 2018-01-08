package com.example.android.guardianapi;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class Guardian {

    private String sectionName;

    private String webTitle;

    private String Url;
    private String Time;
    private String Title;

    private String webPublicationDate;

    public Guardian(String sectionName, String webTitle, String url, String webPublicationDate, String Time) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        Url = url;
        this.webPublicationDate = webPublicationDate;
        this.Time = Time;
    }

    public Guardian(String sectionName, String webTitle, String url, String title, String webPublicationDate, String time) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        Url = url;
        Time = time;
        Title = title;
        this.webPublicationDate = webPublicationDate;
    }

    public String getTitle() {
        return Title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTime() {
        return Time;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getUrl() {
        return Url;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }


}
