package com.example.android.guardianapi;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class Guardian {

    private String sectionName;

    private String webTitle;

    private String Url;

    private long webPublicationDate;

    public Guardian(String sectionName, String webTitle, String url, long webPublicationDate) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        Url = url;
        this.webPublicationDate = webPublicationDate;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getUrl() {
        return Url;
    }

    public long getWebPublicationDate() {
        return webPublicationDate;
    }


}
