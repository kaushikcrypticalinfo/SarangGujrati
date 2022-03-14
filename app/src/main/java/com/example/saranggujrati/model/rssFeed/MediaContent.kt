package com.example.saranggujrati.model.rssFeed

import com.example.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = MEDIA_CONTENT, strict = false)
data class MediaContent(
    @field:Attribute(name = URL, required = false)
    var url: String = "",

)