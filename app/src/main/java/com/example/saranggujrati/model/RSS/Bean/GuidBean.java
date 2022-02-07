package com.example.saranggujrati.model.RSS.Bean;

import org.simpleframework.xml.Attribute;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;







import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;













public class GuidBean {
private static Logger log = Logger.getLogger(GuidBean.class.getName());
    String isPermaLink="";
    String textContent="";
  public void setIsPermaLink(String isPermaLink) { 
		this.isPermaLink=isPermaLink;
	} 
    @Attribute(name = "isPermaLink")
    public String getIsPermaLink() { 
		return isPermaLink;
	} 
  public void setTextContent(String textContent) { 
		this.textContent=textContent;
	} 
    @Attribute(name = "TextContent")
    public String getTextContent() { 
		return textContent;
	} 

}