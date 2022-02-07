package com.example.saranggujrati.model.RSS.Bean;

import org.simpleframework.xml.Attribute;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;








import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;




public class LinkBean {
    private static Logger log = Logger.getLogger(LinkBean.class.getName());
    @Attribute(name = "href")
    String href = "";

    @Attribute(name = "rel")
    String rel = "";

    @Attribute(name = "type")
    String type = "";

    @Attribute(name = "TextContent")
    String textContent = "";

    public void setHref(String href) {
        this.href = href;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

}