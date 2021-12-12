@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val length = center.distance(other.center)
        val sumOfRadius = radius + other.radius
        return if (length <= sumOfRadius) {
            0.0
        } else {
            length - sumOfRadius
        }
    }


    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean {
        val length = center.distance(p)
        return length <= radius
    }
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()

    fun length() = begin.distance(end)

    fun center() = Point((begin.x + end.x) / 2, (begin.y + end.y) / 2)

    fun angle() = ((atan((begin.y - end.y) / (begin.x - end.x))) + 2 * PI) % PI
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    val set = points.toSet()
    if (set.size < 2) throw IllegalArgumentException()
    var maxLength = 0.0
    for (pointA in set) {
        for (pointB in set) {
            if (maxLength < pointA.distance(pointB)) maxLength = pointA.distance(pointB)
        }
    }
    for (pointA in set) {
        for (pointB in set) {
            if (maxLength == pointA.distance(pointB)) {
                return Segment(pointA, pointB)
            }
        }
    }
    return Segment(Point(0.0, 0.0), Point(0.0, 0.0))
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val center = diameter.center()
    val radius = diameter.length() / 2
    return Circle(center, radius)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(
        point.y * cos(angle) - point.x * sin(angle), angle
    )

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val sin1 = sin(angle)
        val cos1 = cos(angle)
        val sin2 = sin(other.angle)
        val cos2 = cos(other.angle)
        val a = other.b
        val x = (a * cos1 - b * cos2) / (sin1 * cos2 - sin2 * cos1)
        val y = (b * sin2 - a * sin1) / (cos1 * sin2 - cos2 * sin1)
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line {
    val angle = s.angle()
    return Line(Point(s.begin.x, s.begin.y), angle)
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val angle = Segment(a, b).angle()
    return Line(Point(a.x, a.y), angle)
}

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val center = Segment(a, b).center()
    val angle = (Segment(a, b).angle() + PI / 2) % PI
    return Line(center, angle)
}

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> = TODO()

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val bisector1 = bisectorByPoints(a, b)
    val bisector2 = bisectorByPoints(b, c)
    val center = bisector1.crossPoint(bisector2)
    val radius = center.distance(a)
    return Circle(center, radius)
}

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun circleByThreePointsDelta(a: Point, b: Point, c: Point, delta: Double): Circle {
    val bisector1 = bisectorByPoints(a, b)
    val bisector2 = bisectorByPoints(b, c)
    val center = bisector1.crossPoint(bisector2)
    val radius = center.distance(a)
    return Circle(center, radius + delta)
}

fun minContainingCircle(vararg points: Point): Circle {
    val set = points.toSet().toList()
    if (set.isEmpty()) throw IllegalArgumentException()
    if (set.size == 1) return Circle(set[0], 0.0)
    val maxDiameter = diameter(*points)
    var radius = maxDiameter.length() / 2
    var center = maxDiameter.center()
    var contains = true
    val maxDiameterCircle = Circle(center, radius)
    for (point in set) {
        if (!maxDiameterCircle.contains(point)) contains = false
    }
    if (!contains) radius *= 10
    for (point1 in 0..set.size - 3) {
        for (point2 in point1 + 1..set.size - 2) {
            for (point3 in point2 + 1 until set.size) {
                val circle = circleByThreePointsDelta(set[point1], set[point2], set[point3], 0.1e-5)
                contains = true
                for (point in set) {
                    if (!circle.contains(point)) contains = false
                }
                if (contains && circle.radius < radius) {
                    radius = circle.radius
                    center = circle.center
                }
            }
        }
    }
    return Circle(center, radius)
}

