package com.example.saranggujrati.model.RSS.Bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class RssChannel {

    @ElementList(inline = true, required = false)
    public List<RssItem> item;


}