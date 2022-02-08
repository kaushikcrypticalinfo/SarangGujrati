package com.example.saranggujrati.model.rssFeed


import com.example.saranggujrati.utils.*
import org.simpleframework.xml.*
import retrofit2.http.Field
import java.io.Serializable

@NamespaceList(Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"))
@Root(name = CHANNEL, strict = false)
data class RssChannel(

    @field:ElementList(inline = true)
    var newsItems: List<RssItems>? = null,

    @field:ElementList(entry = "link", inline = true, required = false)
    var links: List<AtomLink>? = null

) : Serializable
