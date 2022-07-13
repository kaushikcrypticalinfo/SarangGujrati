package com.saranggujrati.model.rssFeed

import com.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = MEDIA_CONTENT, strict = false)
data class MediaContent(
    @field:Attribute(name = URL, required = false)
    var url: String = "",

)