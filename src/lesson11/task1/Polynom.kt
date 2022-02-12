@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {

    private var polynom = doubleArrayOf()
    private var maxPower = 0

    init {
        val mutablePolynom = coeffs.toMutableList()
        for (i in coeffs.indices) {
            if (coeffs[i] == 0.0) mutablePolynom.removeAt(i) else break
        }
        polynom = if (mutablePolynom.isEmpty()) doubleArrayOf(0.0) else mutablePolynom.toDoubleArray()
        maxPower = if (polynom.size - 1 < 0) 0 else polynom.size - 1
    }

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = polynom[maxPower - i]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var power = maxPower
        var result = 0.0
        for (coefficient in polynom) {
            result += coefficient * x.pow(power)
            power--
        }
        return result
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = maxPower

    /**
     * Сложение
     */
    private fun plusPolynom(bigPolynom: DoubleArray, smallPolynom: DoubleArray): Polynom {
        val reversedBig = bigPolynom.reversedArray()
        val reversedSmall = smallPolynom.reversedArray()
        val plusPolynom = mutableListOf<Double>()
        var number = 0
        for (coefficient in reversedSmall) {
            plusPolynom.add(number, coefficient + reversedBig[number])
            number++
        }
        for (i in number until reversedBig.size) {
            plusPolynom.add(i, reversedBig[i])
        }
        return Polynom(*plusPolynom.toDoubleArray().reversedArray())
    }

    operator fun plus(other: Polynom): Polynom =
        if (maxPower >= other.maxPower) plusPolynom(polynom, other.polynom) else plusPolynom(other.polynom, polynom)


    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val mutablePolynom = mutableListOf<Double>()
        for ((number, coefficient) in polynom.withIndex()) {
            mutablePolynom.add(number, -coefficient)
        }
        return Polynom(*mutablePolynom.toDoubleArray())
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = plus(-other)

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val timesPolynom = mutableListOf<Double>()
        for (i in 0..maxPower + other.maxPower) {
            timesPolynom.add(0.0)
        }
        var power = maxPower + 1
        for (coefficient in polynom) {
            power--
            var otherPower = other.maxPower + 1
            for (otherCoefficient in other.polynom) {
                otherPower--
                val timesPower = power + otherPower
                val timesCoefficient = coefficient * otherCoefficient + timesPolynom[timesPower]
                timesPolynom[timesPower] = timesCoefficient
            }
        }
        return Polynom(*timesPolynom.toDoubleArray().reversedArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    private fun divPolynom(other: Polynom): Pair<Polynom, Polynom> {
        var result = Polynom(*mutableListOf(0.0).toDoubleArray())
        var divisible = Polynom(*polynom)
        while (divisible.maxPower >= other.maxPower) {
            val multiplier = mutableListOf(divisible.coeff(divisible.maxPower) / other.coeff(other.maxPower))
            for (i in 0 until divisible.maxPower - other.maxPower) {
                multiplier.add(0.0)
            }
            val multiplierPolynom = Polynom(*multiplier.toDoubleArray())
            divisible -= (multiplierPolynom * other)
            result += multiplierPolynom
            if (divisible.polynom.contentEquals(doubleArrayOf(0.0))) break
        }
        return Pair(result, divisible)
    }

    operator fun div(other: Polynom): Polynom = divPolynom(other).first

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = divPolynom(other).second

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        other is Polynom && polynom.contentEquals(other.polynom)

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int {
        var hash = 0
        for (coefficient in polynom) {
            hash += coefficient.toInt()
            hash += (hash shl 10)
            hash = (hash.toDouble().pow(hash shr 6)).toInt()
        }
        hash += hash shl 3
        hash = (hash.toDouble().pow(hash shr 11)).toInt()
        hash += hash shl 15
        return hash
    }
}
