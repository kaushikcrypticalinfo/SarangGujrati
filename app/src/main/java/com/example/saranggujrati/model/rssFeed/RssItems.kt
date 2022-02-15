package com.example.saranggujrati.model.rssFeed


import com.example.saranggujrati.utils.*
import com.kakyiretechnologies.bothOfUsRss.data.model.Enclosure
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable


@Root(name = ITEM, strict = false)
data class RssItems(
    @field:Element(name = TITLE)
    var title: String = "",

    @field:Element(name = DESCRIPTION, required = false)
    var description: String = "",

    @field:Element(name = LINK)
    var link: String = "",

    @field:Element(name = PUB_DATE, required = false)
    var pubDate: String = "",

    @field:Element(name = ENCLOSURE, required = false)
    var enclosure: Enclosure? = null,

    @field:Element(name = THUMBNAIL, required = false)
    var thumbnail: Thumbnail? = null,

    ) : Serializable
