package com.saranggujrati.model.rssFeed

import com.saranggujrati.utils.*

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root


@Root(name = ENCLOSURE, strict = false)
data class Enclosure(
    @field:Attribute(name = URL)
    var thumbnailUrl: String? = null,
)