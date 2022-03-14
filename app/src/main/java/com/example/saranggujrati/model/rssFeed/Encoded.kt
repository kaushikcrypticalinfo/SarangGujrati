package com.example.saranggujrati.model.rssFeed

import com.example.saranggujrati.utils.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = ENCODED, strict = false)
data class Encoded(
    @field:Element(name = IMG, required = false)
    var img: String = "",

)