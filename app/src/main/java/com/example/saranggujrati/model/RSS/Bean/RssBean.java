package com.example.saranggujrati.model.RSS.Bean;





import java.util.logging.Logger;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rss")
public class RssBean {
    private static Logger log = Logger.getLogger(RssBean.class.getName());
    @Attribute(name = "version")
    String version = "";

    @Attribute(name = "TextContent")
    String textContent = "";

    @Element(name = "channel")
    ChannelBean channelBean;

    public void setVersion(String version) {
        this.version = version;
    }


    public String getVersion() {
        return version;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }


    public String getTextContent() {
        return textContent;
    }

    public ChannelBean getChannelBean() {
        if (channelBean == null) channelBean = new ChannelBean();
        return channelBean;
    }

    public void setChannelBean(ChannelBean channelBean) {
        this.channelBean = channelBean;
    }

}