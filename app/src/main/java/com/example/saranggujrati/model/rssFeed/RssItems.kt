package com.example.saranggujrati.model.rssFeed


import com.example.saranggujrati.utils.*
import org.simpleframework.xml.*
import java.io.Serializable


@Root(name = ITEM, strict = false)
data class RssItems(
    @field:Element(name = TITLE)
    var title: String = "",

    @field:Element(name = DESCRIPTION, required = false)
    var description: String = "",

    @field:ElementList(entry = LINK, inline = true, required = false)
    var link: List<AtomLink>? = null,

    @field:Element(name = PUB_DATE, required = false)
    var pubDate: String = "",

    @field:Element(name = ENCLOSURE, required = false)
    var enclosure: Enclosure? = null,

    @field:Element(name = THUMBNAIL, required = false)
    var thumbnail: Thumbnail? = null,

    @field:Element(name = MEDIA_CONTENT, required = false)
    var mediaContent: MediaContent? = null,

    @field:Element(name = ENCODED, required = false)
    var encoded: Encoded? = null,

    ) : Serializable
