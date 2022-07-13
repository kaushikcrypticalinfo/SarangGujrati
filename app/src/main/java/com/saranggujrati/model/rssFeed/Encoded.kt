package com.saranggujrati.model.rssFeed

import com.saranggujrati.utils.*
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = ENCODED, strict = false)
data class Encoded(
    @field:Element(name = IMG, required = false)
    var img: String = "",

)