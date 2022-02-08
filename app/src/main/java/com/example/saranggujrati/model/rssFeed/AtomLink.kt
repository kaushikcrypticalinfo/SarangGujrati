package com.example.saranggujrati.model.rssFeed

import com.example.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = ATOM_LINK, strict = false)
data class AtomLink(
    @field:Attribute(name = HREF, required = false)
    var href: String = "",

    @field:Attribute(name = REL, required = false)
    var rel: String = "",

    @field:Attribute(name = TYPE, required = false)
    var type: String = "",

    @field:Text(required = false)
    var link: String = "",
)