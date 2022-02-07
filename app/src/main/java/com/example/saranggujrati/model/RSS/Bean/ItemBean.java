package com.example.saranggujrati.model.RSS.Bean;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;








import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;











import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


public class ItemBean {
    private static Logger log = Logger.getLogger(ItemBean.class.getName());
    String category = "";
    String comments = "";
    String creator = "";
    String description = "";
    String link = "";
    String pubDate = "";
    String title = "";
    String textContent = "";
    @SerializedName("guid")
    GuidBean guidBean;

    @SerializedName("thumbnail")
    ThumbnailBean thumbnailBean;

    public void setCategory(String category) {
        this.category = category;
    }

    @Attribute(name = "category")
    public String getCategory() {
        return category;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Attribute(name = "comments")
    public String getComments() {
        return comments;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Attribute(name = "creator")
    public String getCreator() {
        return creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Attribute(name = "description")
    public String getDescription() {
        return description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Attribute(name = "link")
    public String getLink() {
        return link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Attribute(name = "pubDate")
    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Attribute(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @Attribute(name = "TextContent")
    public String getTextContent() {
        return textContent;
    }

    @Element(name = "guid")
    public GuidBean getGuidBean() {
        if (guidBean == null) guidBean = new GuidBean();
        return guidBean;
    }

    public void setGuidBean(GuidBean guidBean) {
        this.guidBean = guidBean;
    }

    @Element(name = "thumbnail")
    public ThumbnailBean getThumbnailBean() {
        if (thumbnailBean == null) thumbnailBean = new ThumbnailBean();
        return thumbnailBean;
    }

    public void setThumbnailBean(ThumbnailBean thumbnailBean) {
        this.thumbnailBean = thumbnailBean;
    }

}