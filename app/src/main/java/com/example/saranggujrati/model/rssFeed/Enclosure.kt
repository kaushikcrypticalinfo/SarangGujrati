package com.kakyiretechnologies.bothOfUsRss.data.model

import com.example.saranggujrati.utils.*

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root


@Root(name = ENCLOSURE, strict = false)
data class Enclosure(
    @field:Attribute(name = THUMBNAIL_URL)
    var thumbnailUrl: String? = null,
)
