package com.example.saranggujrati.model.rssFeed

import com.example.saranggujrati.utils.*
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable


@Root(name = RSS, strict = false)
data class RssFeed(
    @field:Element(name = CHANNEL, required = false)
    var newsChannel: RssChannel? = null
) : Serializable
