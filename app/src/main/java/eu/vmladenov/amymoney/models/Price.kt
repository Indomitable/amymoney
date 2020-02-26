package eu.vmladenov.amymoney.models

import java.util.*
import kotlin.collections.ArrayList

data class Price(
    @XmlAttribute("source") val source: String,
    @XmlAttribute("price") val price: String,
    @XmlAttribute("date") val date: Date
)

data class PricePair (
    @XmlAttribute("from") val from: String,
    @XmlAttribute("to") val to: String,
    val prices: List<Price>
)

data class Prices (val pricePairs: List<PricePair>): ArrayList<PricePair>(pricePairs)
