@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import java.lang.IllegalStateException
import kotlin.math.abs

/**
 * Класс "табличная функция".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса хранит таблицу значений функции (y) от одного аргумента (x).
 * В таблицу можно добавлять и удалять пары (x, y),
 * найти в ней ближайшую пару (x, y) по заданному x,
 * найти (интерполяцией или экстраполяцией) значение y по заданному x.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class TableFunction {

    private val listOfX = mutableListOf<Double>()
    private val listOfY = mutableListOf<Double>()
    private var sizeInt = 0

    /**
     * Количество пар в таблице
     */


    val size: Int get() = sizeInt

    /**
     * Добавить новую пару.
     * Вернуть true, если пары с заданным x ещё нет,
     * или false, если она уже есть (в этом случае перезаписать значение y)
     */
    fun add(x: Double, y: Double): Boolean = if (x in listOfX) {
        listOfY[listOfX.indexOf(x)] = y
        false
    } else {
        listOfX.add(x)
        listOfY.add(y)
        sizeInt++
        true
    }

    /**
     * Удалить пару с заданным значением x.
     * Вернуть true, если пара была удалена.
     */
    fun remove(x: Double): Boolean = if (x in listOfX) {
        listOfY.removeAt(listOfX.indexOf(x))
        listOfX.remove(x)
        sizeInt--
        true
    } else {
        false
    }

    /**
     * Вернуть коллекцию из всех пар в таблице
     */
    fun getPairs(): Collection<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        for (i in 0 until sizeInt) {
            result.add(Pair(listOfX[i], listOfY[i]))
        }
        return result
    }

    /**
     * Вернуть пару, ближайшую к заданному x.
     * Если существует две ближайшие пары, вернуть пару с меньшим значением x.
     * Если таблица пуста, бросить IllegalStateException.
     */
    fun findPair(x: Double): Pair<Double, Double> {
        if (listOfX.isEmpty()) throw IllegalStateException()
        var min = Double.MAX_VALUE
        var result = 0.0
        for (i in 0 until sizeInt) {
            if (abs(x - listOfX[i]) < min) {
                min = abs(x - listOfX[i])
                result = listOfX[i]
            }
        }
        return Pair(result, listOfY[listOfX.indexOf(result)])
    }

    /**
     * Вернуть значение y по заданному x.
     * Если в таблице есть пара с заданным x, взять значение y из неё.
     * Если в таблице есть всего одна пара, взять значение y из неё.
     * Если таблица пуста, бросить IllegalStateException.
     * Если существуют две пары, такие, что x1 < x < x2, использовать интерполяцию.
     * Если их нет, но существуют две пары, такие, что x1 < x2 < x или x > x2 > x1, использовать экстраполяцию.
     */
    private fun findSmaller(x: Double): Pair<Double, Double> {
        var min = Double.MAX_VALUE
        var result = Pair(0.0, 0.0)
        for (i in 0 until sizeInt) {
            if (abs(x - listOfX[i]) < min && listOfX[i] < x) {
                min = abs(x - listOfX[i])
                result = Pair(listOfX[i], listOfY[i])
            }
        }
        return result
    }

    private fun findBigger(x: Double): Pair<Double, Double> {
        var min = Double.MAX_VALUE
        var result = Pair(0.0, 0.0)
        for (i in 0 until sizeInt) {
            if (abs(x - listOfX[i]) < min && listOfX[i] > x) {
                min = abs(x - listOfX[i])
                result = Pair(listOfX[i], listOfY[i])
            }
        }
        return result
    }

    fun getValue(x: Double): Double {
        if (listOfX.isEmpty()) throw IllegalArgumentException()
        if (listOfX.size == 1) return listOfY[0]
        if (x in listOfX) return listOfY[listOfX.indexOf(x)]
        var booleanSmaller = false
        var booleanBigger = false
        for (i in 0 until sizeInt) {
            if (listOfX[i] < x) booleanSmaller = true
            if (listOfX[i] > x) booleanBigger = true
        }
        return if (booleanBigger && booleanSmaller) {
            val pair1 = findSmaller(x)
            val pair2 = findBigger(x)
            (x - pair1.first) * (pair2.second - pair1.second) / (pair2.first - pair1.first) + pair1.second
        } else if (booleanSmaller) {
            val pair1 = findSmaller(x)
            val pair2 = findSmaller(pair1.first)
            (x - pair2.first) * (pair2.second - pair1.second) / (pair2.first - pair1.first) + pair2.second
        } else {
            val pair1 = findBigger(x)
            val pair2 = findBigger(pair1.first)
            pair1.second - (pair1.first - x) * (pair2.second - pair1.second) / (pair2.first - pair1.first)
        }
    }

    /**
     * Таблицы равны, если в них одинаковое количество пар,
     * и любая пара из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TableFunction || sizeInt != other.sizeInt) return false
        val list = mutableSetOf<Pair<Double, Double>>()
        val otherList = mutableSetOf<Pair<Double, Double>>()
        for (i in 0 until sizeInt) {
            list.add(Pair(listOfX[i], listOfY[i]))
            otherList.add(Pair(other.listOfX[i], other.listOfY[i]))
        }
        return (list == otherList)
    }

    override fun hashCode(): Int {
        var hash = 0
        val list = mutableMapOf<Double, Double>()
        for (i in 0 until sizeInt) {
            list[listOfX[i]] = listOfY[i]
        }
        val newList = list.toSortedMap()
        for ((key, value) in newList) {
            hash += key.toInt() * 10 + value.toInt()
            hash += (hash shl 10)
        }
        hash += hash shl 15
        return hash
    }
}