package eu.vmladenov.amymoney.models

enum class SecurityType(val type: Int) {
    Stock(0),
    MutualFund(1),
    Bond(2),
    Currency(3),
    None(4);

    companion object {
        private val map = values().associateBy(SecurityType::type)

        fun fromAttribute(value: String): SecurityType {
            return if (value == "") None else get(Integer.parseInt(value))
        }

        operator fun get(value: Int): SecurityType {
            return map.getOrElse(value) { None }
        }
    }
}

enum class RoundingMethod(val method: Int) {

    /**
     * Don't do any rounding, simply truncate and
     * print a warning in case of a remainder.
     * Otherwise the same as RoundTrunc.
     */
    RoundNever(0),

    /**
     * Round to the largest integral value not
     * greater than @p this.
     * e.g. 0.5 -> 0.0 and -0.5 -> -1.0
     */
    RoundFloor(1),

    /**
     * Round to the smallest integral value not
     * less than @p this.
     * e.g. 0.5 -> 1.0 and -0.5 -> -0.0
     */
    RoundCeil(2),

    /**
     * No rounding, simply truncate any fraction
     */
    RoundTruncate(3),

    /**
     * Use RoundCeil for positive and RoundFloor
     * for negative values of @p this.
     * e.g. 0.5 -> 1.0 and -0.5 -> -1.0
     */
    RoundPromote(4),

    /**
     * Round up or down with the following
     * constraints:
     * 0.1 .. 0.5 -> 0.0 and 0.6 .. 0.9 -> 1.0
     */
    RoundHalfDown(5),

    /**
     * Round up or down with the following
     * constraints:
     * 0.1 .. 0.4 -> 0.0 and 0.5 .. 0.9 -> 1.0
     */
    RoundHalfUp(6),

    /**
     * Use RoundHalfDown for 0.1 .. 0.4 and
     * RoundHalfUp for 0.6 .. 0.9. Use RoundHalfUp
     * for 0.5 in case the resulting numerator
     * is odd, RoundHalfDown in case the resulting
     * numerator is even.
     * e.g. 0.5 -> 0.0 and 1.5 -> 2.0
     */
    RoundRound(7);

    companion object {
        private val map = values().associateBy(RoundingMethod::method)

        fun fromAttribute(value: String): RoundingMethod {
            return if (value == "") RoundRound else get(Integer.parseInt(value))
        }

        operator fun get(value: Int): RoundingMethod {
            return map.getOrElse(value) { RoundRound }
        }
    }
}

data class Security(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("name") val name: String,
    @XmlAttribute("symbol") val tradingSymbol: String,
    @XmlAttribute("type") val securityType: SecurityType,
    @XmlAttribute("rounding-method") val roundingMethod: RoundingMethod,
    @XmlAttribute("trading-market") val tradingMarket: String,
    @XmlAttribute("trading-currency") val tradingCurrency: String,
    @XmlAttribute("saf") val smallestAccountFraction: Int,
    @XmlAttribute("scf") val smallestCashFraction: Int,
    @XmlAttribute("pp") val pricePrecision: Int,

    val extra: Map<String, String>
)

data class Securities(val items: List<Security>): ArrayList<Security>(items)
