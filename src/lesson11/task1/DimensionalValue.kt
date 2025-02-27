@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "Величина с размерностью".
 *
 * Предназначен для представления величин вроде "6 метров" или "3 килограмма"
 * Общая сложность задания - средняя, общая ценность в баллах -- 18
 * Величины с размерностью можно складывать, вычитать, делить, менять им знак.
 * Их также можно умножать и делить на число.
 *
 * В конструктор передаётся вещественное значение и строковая размерность.
 * Строковая размерность может:
 * - либо строго соответствовать одной из abbreviation класса Dimension (m, g)
 * - либо соответствовать одной из приставок, к которой приписана сама размерность (Km, Kg, mm, mg)
 * - во всех остальных случаях следует бросить IllegalArgumentException
 */
class DimensionalValue(value: Double, dimension: String) : Comparable<DimensionalValue> {

    private val dimensionMap = Dimension.values().associate { it.abbreviation to it.name }

    val q1 = value
    val q2 = dimension

    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double get() = q1

    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension
        get() = when (q2.toString().length) {
            1 -> Dimension.valueOf(dimensionMap[q2.toString()]!!)
            2 -> Dimension.valueOf(dimensionMap[q2.toString()[1].toString()]!!)
            else -> throw IllegalArgumentException()
        }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */
    constructor(s: String) : this(TODO(), TODO())

    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue = TODO()

    /**
     * Смена знака величины
     */
    operator fun unaryMinus(): DimensionalValue = TODO()

    /**
     * Вычитание другой величины. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun minus(other: DimensionalValue): DimensionalValue = TODO()

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue = TODO()

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue = TODO()

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = TODO()

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
    override fun compareTo(other: DimensionalValue): Int = TODO()
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");

    fun qq() {
        val l = Dimension.values().associate { it.abbreviation to it.name }
    }
}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001);
}

fun main() {
    val qq = DimensionalValue(1.0, "Kg")
    //println(qq.value)
    println(qq.dimension)
    //for (i in Dimension.values()) println(i)
}