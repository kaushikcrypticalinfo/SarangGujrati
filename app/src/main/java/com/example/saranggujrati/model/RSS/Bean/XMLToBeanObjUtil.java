package com.example.saranggujrati.model.RSS.Bean;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class XMLToBeanObjUtil {
private static RssBean convertXmlToBeanObject(Document doc ) throws Exception { 

		Element rssElm = doc.getDocumentElement();RssBean rssBean = new RssBean();


rssBean.setVersion(rssElm.getAttribute("version"));
rssBean.setTextContent(XMLUtil.getFirstLevelTextContent(rssElm));

		Element channelElm = XMLUtil.getChildElement(rssElm, "channel");
ChannelBean channelBean = new ChannelBean();
rssBean.setChannelBean(channelBean);


channelBean.setDescription(channelElm.getAttribute("description"));
channelBean.setGenerator(channelElm.getAttribute("generator"));
channelBean.setLanguage(channelElm.getAttribute("language"));
channelBean.setLastBuildDate(channelElm.getAttribute("lastBuildDate"));
channelBean.setTitle(channelElm.getAttribute("title"));
channelBean.setUpdateFrequency(channelElm.getAttribute("updateFrequency"));
channelBean.setUpdatePeriod(channelElm.getAttribute("updatePeriod"));
channelBean.setTextContent(XMLUtil.getFirstLevelTextContent(channelElm));

		Element itemElm = XMLUtil.getChildElement(channelElm, "item");
ItemBean itemBean = new ItemBean();
channelBean.setItemBean(itemBean);


itemBean.setCategory(itemElm.getAttribute("category"));
itemBean.setComments(itemElm.getAttribute("comments"));
itemBean.setCreator(itemElm.getAttribute("creator"));
itemBean.setDescription(itemElm.getAttribute("description"));
itemBean.setLink(itemElm.getAttribute("link"));
itemBean.setPubDate(itemElm.getAttribute("pubDate"));
itemBean.setTitle(itemElm.getAttribute("title"));
itemBean.setTextContent(XMLUtil.getFirstLevelTextContent(itemElm));

		Element guidElm = XMLUtil.getChildElement(itemElm, "guid");
GuidBean guidBean = new GuidBean();
itemBean.setGuidBean(guidBean);


guidBean.setIsPermaLink(guidElm.getAttribute("isPermaLink"));
guidBean.setTextContent(XMLUtil.getFirstLevelTextContent(guidElm));

		Element thumbnailElm = XMLUtil.getChildElement(itemElm, "thumbnail");
ThumbnailBean thumbnailBean = new ThumbnailBean();
itemBean.setThumbnailBean(thumbnailBean);


thumbnailBean.setUrl(thumbnailElm.getAttribute("url"));
thumbnailBean.setWidth(thumbnailElm.getAttribute("width"));
thumbnailBean.setTextContent(XMLUtil.getFirstLevelTextContent(thumbnailElm));

		Element linkElm = XMLUtil.getChildElement(channelElm, "link");
LinkBean linkBean = new LinkBean();
channelBean.setLinkBean(linkBean);


linkBean.setHref(linkElm.getAttribute("href"));
linkBean.setRel(linkElm.getAttribute("rel"));
linkBean.setType(linkElm.getAttribute("type"));
linkBean.setTextContent(XMLUtil.getFirstLevelTextContent(linkElm));


 return rssBean; 
 } 
 }