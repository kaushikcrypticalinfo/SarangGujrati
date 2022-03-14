package com.example.saranggujrati.model.rssFeed


import com.example.saranggujrati.utils.*
import org.simpleframework.xml.*
import java.io.Serializable

@NamespaceList(
    Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
    Namespace(reference = "http://purl.org/rss/1.0/modules/content/", prefix = "content "),
    Namespace(reference = "http://search.yahoo.com/mrss/", prefix = "media ")
)
@Root(name = CHANNEL, strict = false)
data class RssChannel(
    @field:ElementList(inline = true)
    var newsItems: List<RssItems>? = null,
) : Serializable
