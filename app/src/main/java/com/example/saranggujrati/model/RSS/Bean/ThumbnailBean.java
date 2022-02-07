package com.example.saranggujrati.model.RSS.Bean;

import org.simpleframework.xml.Attribute;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;







import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;













public class ThumbnailBean {
private static Logger log = Logger.getLogger(ThumbnailBean.class.getName());
    String url="";
    String width="";
    String textContent="";
  public void setUrl(String url) { 
		this.url=url;
	} 
    @Attribute(name = "url")
    public String getUrl() { 
		return url;
	} 
  public void setWidth(String width) { 
		this.width=width;
	} 
    @Attribute(name = "width")
    public String getWidth() { 
		return width;
	} 
  public void setTextContent(String textContent) { 
		this.textContent=textContent;
	} 
    @Attribute(name = "TextContent")
    public String getTextContent() { 
		return textContent;
	} 

}