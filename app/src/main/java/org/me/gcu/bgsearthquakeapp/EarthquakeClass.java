package org.me.gcu.bgsearthquakeapp;

public class EarthquakeClass {
    private String title;
    private String link;
    private String pubDate;

    public EarthquakeClass()
    {
        title = "";
        link = "";
        pubDate = "";
    }

    public EarthquakeClass(String atitle, String alink, String apubDate)
    {
        title = atitle;
        link = alink;
        pubDate = apubDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String alink)
    {
        link = alink;
    }

    public String getPubDate()
    {
        return pubDate;
    }

    public void setPubDate(String apubDate)
    {
        pubDate = apubDate;
    }

    public String toString()
    {
        String temp;

        temp = title + " " + link + " " + pubDate;

        return temp;
    }
}
