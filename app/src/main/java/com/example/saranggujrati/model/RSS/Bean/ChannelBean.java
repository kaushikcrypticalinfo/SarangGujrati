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


public class ChannelBean {
    private static Logger log = Logger.getLogger(ChannelBean.class.getName());
    String description = "";
    String generator = "";
    String language = "";
    String lastBuildDate = "";
    String title = "";
    String updateFrequency = "";
    String updatePeriod = "";
    String textContent = "";

    @SerializedName("item")
    ItemBean itemBean;

    @SerializedName("link")
    LinkBean linkBean;

    public void setDescription(String description) {
        this.description = description;
    }

    @Attribute(name = "description")
    public String getDescription() {
        return description;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    @Attribute(name = "generator")
    public String getGenerator() {
        return generator;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Attribute(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    @Attribute(name = "lastBuildDate")
    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Attribute(name = "title")
    public String getTitle() {
        return title;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    @Attribute(name = "updateFrequency")
    public String getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdatePeriod(String updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    @Attribute(name = "updatePeriod")
    public String getUpdatePeriod() {
        return updatePeriod;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @Attribute(name = "TextContent")
    public String getTextContent() {
        return textContent;
    }

    @Element(name = "item")
    public ItemBean getItemBean() {
        if (itemBean == null) itemBean = new ItemBean();
        return itemBean;
    }

    public void setItemBean(ItemBean itemBean) {
        this.itemBean = itemBean;
    }

    @Element(name = "link")
    public LinkBean getLinkBean() {
        if (linkBean == null) linkBean = new LinkBean();
        return linkBean;
    }

    public void setLinkBean(LinkBean linkBean) {
        this.linkBean = linkBean;
    }

}