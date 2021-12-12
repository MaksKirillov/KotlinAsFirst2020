import lesson8.task1.Line
import lesson8.task1.Point
import kotlin.math.PI

fun main() {
    val point = Point(3.0, 2.0)
    val angle = PI / 4
    val line = Line(point, angle)

    val point2 = Point(1.0, 0.0)
    val angle2 = PI / 4
    val line2 = Line(point2, angle2)
    println(line)
    println(line2)
}