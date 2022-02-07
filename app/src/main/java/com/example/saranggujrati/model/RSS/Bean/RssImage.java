package com.example.saranggujrati.model.RSS.Bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "image", strict = false)
public class RssImage 
{
  @Element
  private String url;
   
  @Element
  private String width;
   
  @Element
  private String height;
   
  @Override
  public String toString() {
    return "RssImage [url=" + url + ", width=" + width + ", height=" + height + "]";
  }
}