package eu.vmladenov.amymoney.local_test.infrastructure

import eu.vmladenov.amymoney.infrastructure.Fraction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.text.ParseException

class FractionTests {

    @Nested
    inner class Adding {
        @Test
        fun addSameDenominators() {
            val a = Fraction(3, 2)
            val b = Fraction(5, 2)
            val r = a + b
            assertEquals(Fraction(8, 2), r)
        }

        @Test
        fun addDifferentDenominators() {
            val a = Fraction(3, 4)
            val b = Fraction(5, 3)
            val r = a + b
            assertEquals(Fraction(29, 12), r)
        }
    }

    @Nested
    inner class Subtraction {
        @Test
        fun subtractSameDenominators() {
            val a = Fraction(3, 2)
            val b = Fraction(5, 2)
            val r = a - b
            assertEquals(Fraction(-2, 2), r)
        }

        @Test
        fun subtractDifferentDenominators() {
            val a = Fraction(5, 3)
            val b = Fraction(3, 4)
            val r = a - b
            assertEquals(Fraction(11, 12), r)
        }
    }

    @Nested
    inner class Multiply {
        @Test
        fun multiplySameDenominators() {
            val a = Fraction(3, 2)
            val b = Fraction(5, 2)
            val r = a * b
            assertEquals(Fraction(15, 4), r)
        }

        @Test
        fun multiplyDifferentDenominators() {
            val a = Fraction(5, 3)
            val b = Fraction(3, 4)
            val r = a * b
            assertEquals(Fraction(15, 12), r)
        }
    }

    @Nested
    inner class Division {
        @Test
        fun divideSameDenominators() {
            val a = Fraction(3, 2)
            val b = Fraction(5, 2)
            val r = a / b
            assertEquals(Fraction(6, 10), r)
        }

        @Test
        fun divideDifferentDenominators() {
            val a = Fraction(5, 3)
            val b = Fraction(3, 4)
            val r = a / b
            assertEquals(Fraction(20, 9), r)
        }
    }

    @Nested
    inner class StringParse {
        @Test
        fun parseString0() {
            val f = Fraction.parseFraction("2/3")
            assertEquals(Fraction(2, 3), f)
        }

        @Test
        fun parseString1() {
            val f = Fraction.parseFraction("4/15")
            assertEquals(Fraction(4, 15), f)
        }

        @Test
        fun parseString2() {
            val f = Fraction.parseFraction("40/1")
            assertEquals(Fraction(40, 1), f)
        }

        @Test
        fun parseString3() {
            val f = Fraction.parseFraction("342/16")
            assertEquals(Fraction(342, 16), f)
        }

        @Test
        fun parseString4() {
            val f = Fraction.parseFraction("10000/137603")
            assertEquals(Fraction(10000, 137603), f)
        }

        @Test
        fun parseStringNegative() {
            val f = Fraction.parseFraction("-227/20")
            assertEquals(Fraction(-227, 20), f)
        }

        @Test
        fun parseWrongString0() {
            assertThrows<ParseException> {
                Fraction.parseFraction("asd")
            }
        }

        @Test
        fun parseWrongString1() {
            assertThrows<ParseException> {
                Fraction.parseFraction("1/")
            }
        }

        @Test
        fun parseWrongString2() {
            assertThrows<ParseException> {
                Fraction.parseFraction("/2")
            }
        }
    }

    @Nested
    inner class Comparison {
        @Test
        fun greaterThan() {
            val f0 = Fraction(3, 4)
            val f1 = Fraction(1, 3)
            assertTrue(f0 > f1)
        }

        @Test
        fun greaterOrEqualThan() {
            val f0 = Fraction(3, 4)
            val f1 = Fraction(1, 3)
            assertTrue(f0 >= f1)
        }

        @Test
        fun lessThan() {
            val f0 = Fraction(3, 4)
            val f1 = Fraction(1, 3)
            assertTrue(f1 < f0)
        }

        @Test
        fun lessOrEqualThan() {
            val f0 = Fraction(3, 4)
            val f1 = Fraction(1, 3)
            assertTrue(f1 <= f0)
        }

        @Test
        fun equalFraction() {
            val f0 = Fraction(3, 4)
            val f1 = Fraction(6, 8)
            assertTrue(f0 == f1)
        }

        @Test
        fun equalNull() {
            val f0 = Fraction(3, 4)
            assertFalse(f0.equals(null))
        }

        @Test
        fun equalOtherType() {
            val f0 = Fraction(3, 4)
            assertFalse(f0.equals(Pair(3, 4)))
        }
    }

    @Test
    fun destruction() {
        val f = Fraction(3, 2)
        val (n, d) = f
        assertEquals(3L, n)
        assertEquals(2L, d)
    }

    @Test
    fun unaryMinus() {
        val f = Fraction(3, 12)
        val minusF = -f
        assertEquals(Fraction(-3, 12), minusF)
    }

    @Test
    fun simplify() {
        val f = Fraction(3, 12)
        val simplified = f.simplify()
        assertEquals(1, simplified.numerator)
        assertEquals(4, simplified.denominator)
        assertNotSame(simplified, f)
    }
}
