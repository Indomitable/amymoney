package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.infrastructure.Fraction
import java.util.*
import kotlin.collections.ArrayList

data class Price(
    @XmlAttribute("source") val source: String,
    @XmlAttribute("price") val price: Fraction,
    @XmlAttribute("date") val date: Date
)

data class PricePair (
    @XmlAttribute("from") val from: String,
    @XmlAttribute("to") val to: String,
    val prices: List<Price>
)

class Prices: BaseList<PricePair>()
