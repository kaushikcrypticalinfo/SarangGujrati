package com.example.saranggujrati.model.RSS.Bean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class BeanToXMLUtil {
public static Document convertBeanToXML (RssBean rssBean) throws Exception {
Document inputDoc = XMLUtil.createDocument("rss");  Element rssElm=inputDoc.getDocumentElement(); 
rssElm.setTextContent(rssBean.getTextContent());


rssElm.setAttribute("version",rssBean.getVersion());

Element channelElm=XMLUtil.createChildElement(rssElm,"channel");
ChannelBean channelBean=rssBean.getChannelBean();
channelElm.setTextContent(channelBean.getTextContent());


channelElm.setAttribute("description",channelBean.getDescription());
channelElm.setAttribute("generator",channelBean.getGenerator());
channelElm.setAttribute("language",channelBean.getLanguage());
channelElm.setAttribute("lastBuildDate",channelBean.getLastBuildDate());
channelElm.setAttribute("title",channelBean.getTitle());
channelElm.setAttribute("updateFrequency",channelBean.getUpdateFrequency());
channelElm.setAttribute("updatePeriod",channelBean.getUpdatePeriod());

Element itemElm=XMLUtil.createChildElement(channelElm,"item");
ItemBean itemBean=channelBean.getItemBean();
itemElm.setTextContent(itemBean.getTextContent());


itemElm.setAttribute("category",itemBean.getCategory());
itemElm.setAttribute("comments",itemBean.getComments());
itemElm.setAttribute("creator",itemBean.getCreator());
itemElm.setAttribute("description",itemBean.getDescription());
itemElm.setAttribute("link",itemBean.getLink());
itemElm.setAttribute("pubDate",itemBean.getPubDate());
itemElm.setAttribute("title",itemBean.getTitle());

Element guidElm=XMLUtil.createChildElement(itemElm,"guid");
GuidBean guidBean=itemBean.getGuidBean();
guidElm.setTextContent(guidBean.getTextContent());


guidElm.setAttribute("isPermaLink",guidBean.getIsPermaLink());

Element thumbnailElm=XMLUtil.createChildElement(itemElm,"thumbnail");
ThumbnailBean thumbnailBean=itemBean.getThumbnailBean();
thumbnailElm.setTextContent(thumbnailBean.getTextContent());


thumbnailElm.setAttribute("url",thumbnailBean.getUrl());
thumbnailElm.setAttribute("width",thumbnailBean.getWidth());

Element linkElm=XMLUtil.createChildElement(channelElm,"link");
LinkBean linkBean=channelBean.getLinkBean();
linkElm.setTextContent(linkBean.getTextContent());


linkElm.setAttribute("href",linkBean.getHref());
linkElm.setAttribute("rel",linkBean.getRel());
linkElm.setAttribute("type",linkBean.getType());

 
 return inputDoc; 
 } 
 }