package eu.vmladenov.amymoney.infrastructure

import java.lang.Exception
import java.math.BigDecimal
import java.text.ParseException

/**
 * Immutable class that works with fractions.
 */
class Fraction(val numerator: Long, val denominator: Long) {

    // if denominator grows more than the threshold simplify
    val denominatorThreshold = Integer.MAX_VALUE

    init {
        if (denominator == 0L) {
            throw Exception("Invalid Fraction")
        }
    }

    fun simplify(): Fraction {
        val gcd = getCommonDivisor(numerator, denominator)
        return if (gcd > 1) {
            Fraction(numerator / gcd, denominator / gcd)
        } else {
            this
        }
    }

    fun toDecimal(): BigDecimal {
        if (numerator == 0L || denominator == 0L) {
            return BigDecimal(0)
        }
        return BigDecimal(numerator).divide(BigDecimal(denominator))
    }

    operator fun component1() = numerator
    operator fun component2() = denominator

    operator fun unaryMinus(): Fraction {
        return Fraction(-numerator, denominator)
    }

    operator fun plus(other: Fraction): Fraction {
        val (fn, sn, d) = convertToEqualDenominators(Pair(this, other))
        return Fraction(fn + sn, d)
            .checkThreshold()
    }

    operator fun minus(other: Fraction): Fraction {
        val (fn, sn, d) = convertToEqualDenominators(Pair(this, other))
        return Fraction(fn - sn, d)
            .checkThreshold()
    }

    operator fun times(other: Fraction): Fraction {
        return Fraction(this.numerator * other.numerator, this.denominator * other.denominator)
            .checkThreshold()
    }

    operator fun div(other: Fraction): Fraction {
        return Fraction(this.numerator * other.denominator, this.denominator * other.numerator)
            .checkThreshold()
    }

    operator fun compareTo(other: Fraction): Int {
        val (fn, sn, _) = convertToEqualDenominators(Pair(this, other))
        return fn.compareTo(sn)
    }

    override operator fun equals(other: Any?): Boolean {
        if (other is Fraction) {
            val (fn, sn, _) = convertToEqualDenominators(Pair(this, other))
            return fn == sn
        }
        return false
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    private fun checkThreshold(): Fraction {
        if (denominator >= denominatorThreshold) {
            return simplify()
        }
        return this
    }

    companion object {
        private val regex = Regex("(-?\\d+)/(\\d+)")

        fun parseFraction(value: String): Fraction {
            val result = regex.matchEntire(value)
            if (result == null || result.groupValues.size != 3) {
                throw ParseException("Can not parse value $value", 0)
            }
            val numerator = java.lang.Long.parseLong(result.groups[1]!!.value)
            val denominator = java.lang.Long.parseLong(result.groups[2]!!.value)
            return Fraction(numerator, denominator)
        }

        private fun convertToEqualDenominators(fractions: Pair<Fraction, Fraction>): Triple<Long, Long, Long> {
            return if (fractions.first.denominator == fractions.second.denominator) {
                Triple(fractions.first.numerator, fractions.second.numerator, fractions.first.denominator)
            } else {
                Triple(
                    first = fractions.first.numerator * fractions.second.denominator,
                    second = fractions.second.numerator * fractions.first.denominator,
                    third = fractions.first.denominator * fractions.second.denominator
                )
            }
        }

        private fun getCommonDivisor(first: Long, second: Long): Long {
            var a = first
            var b = second
            while (b != 0L) {
                val tmp = b
                b = a % b
                a = tmp
            }
            return a
        }
    }

}


