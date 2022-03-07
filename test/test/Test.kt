package test

import java.lang.IllegalStateException
import kotlin.math.abs


fun plus(a: Int, b: Int): Int = a + b

fun minus(a: Int, b: Int): Int = a - b

fun maxSum(input: String): String {
    try {
        var max = 0
        var maxString = ""
        val list = "$input ".split(Regex(",\\s+"))
        println(list)
        if (list[0] == "" || list[0] == " ") throw IllegalArgumentException()
        if (list.size == 1) return input
        for (i in 0..list.size - 2) {
            val currentList = StringBuilder(list[i])
            var currentSum = list[i].trim().toInt()
            if (currentSum >= max) {
                max = currentSum
                maxString = currentList.toString()
            }
            for (j in i + 1 until list.size) {
                currentList.append(", ${list[j]}")
                currentSum += list[j].trim().toInt()
                if (currentSum >= max) {
                    max = currentSum
                    maxString = currentList.toString()
                }
            }
        }
        return maxString.trim()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException()
    } catch (e: IndexOutOfBoundsException) {
        throw IllegalArgumentException()
    }
}

fun chess(state: String, move: String): String {
    val listOfLetters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')
    val listOfChars = listOf('1', '2', '3', '4', '5', '6', '7', '8')
    val listOfNumbers = listOf(0, 7, 6, 5, 4, 3, 2, 1, 0)
    val list = (state.split("\r")).toMutableList()
    if (list.size != 8) throw IllegalStateException()
    for (i in 0..7) {
        if (list[i].trim().length != 8) throw IllegalStateException()
        list[i] = list[i].trim()
    }
    val board = mutableListOf<MutableList<Char>>()
    var whitePiece = Pair(0, 0)
    var blackPiece = Pair(0, 0)
    for (i in 0..7) {
        board.add(mutableListOf())
        for (j in 0..7) {
            board[i].add('x')
            board[i][j] = list[i][j]
            if (board[i][j] == '1') whitePiece = Pair(i, j)
            if (board[i][j] == '0') blackPiece = Pair(i, j)
        }
    }
    val moving = mutableListOf(0, 0, 0, 0)
    if (move.length != 5) throw IllegalStateException()
    if (move[0] !in listOfLetters || move[3] !in listOfLetters) throw IllegalStateException()
    if (move[1] !in listOfChars || move[4] !in listOfChars) throw IllegalStateException()
    if (move[2] != ' ') throw IllegalStateException()
    for (i in listOfLetters.indices) {
        if (move[0] == listOfLetters[i]) moving[1] = i
        if (move[1].toString() == "$i") moving[0] = listOfNumbers[i]
        if (move[3] == listOfLetters[i]) moving[3] = i
        if (move[4].toString() == "$i") moving[2] = listOfNumbers[i]
    }
    if (blackPiece.first != moving[0] || blackPiece.second != moving[1]) throw IllegalStateException()
    if (whitePiece.first == moving[0] || whitePiece.second == moving[1]) throw IllegalStateException()
    if (blackPiece.first == moving[2] || blackPiece.second == moving[3]) throw IllegalStateException()
    if (whitePiece.first == moving[2] || whitePiece.second == moving[3]) throw IllegalStateException()
    if (abs(moving[2] - moving[0]) > 2 || abs(moving[3] - moving[1]) > 2) throw IllegalStateException()
    board[blackPiece.first][blackPiece.second] = 'x'
    blackPiece = Pair(moving[2], moving[3])
    board[blackPiece.first][blackPiece.second] = '0'
    if ((whitePiece.first in moving[0]..moving[2] || whitePiece.first in moving[2]..moving[0]) &&
        whitePiece.second in moving[1]..moving[3] || whitePiece.second in moving[3]..moving[1]
    ) {
        board[whitePiece.first][whitePiece.second] = 'x'
    }
    val returnString = StringBuilder("")
    for (i in 0..7) {
        for (j in 0..7) {
            returnString.append(board[i][j])
        }
        returnString.append("\r")
    }
    return returnString.toString().trim()
}

fun trueOrFalse(expr: String, vars: String): String {
    try {
        val listOfExpr = expr.trim().lowercase().split(Regex("\\s+")).toMutableList()
        val listOfVars = vars.trim().lowercase().split("\r").toMutableList()
        val mapOfVars = mutableMapOf<String, Boolean>()
        for (i in listOfVars.indices) {
            listOfVars[i] = listOfVars[i].trim()
            val listOfThree = listOfVars[i].split(Regex("\\s+"))
            if (listOfThree.size != 3) throw IllegalStateException()
            val boolean = when (listOfThree[2].trim()) {
                "истина" -> true
                "ложь" -> false
                else -> throw IllegalStateException()
            }
            if (listOfThree[0].trim() in mapOfVars.keys) throw IllegalStateException()
            mapOfVars[listOfThree[0].trim()] = boolean
        }
        var count = 0
        var booleanNO = true
        var booleanOR = true
        val listOfResult = mutableListOf(false)
        for (i in listOfExpr.indices) {
            listOfExpr[i] = listOfExpr[i].trim()
            if (booleanOR) {
                if (listOfExpr[i] == "или") throw IllegalStateException()
            }
            if (!booleanOR) {
                if (listOfExpr[i] != "или") throw IllegalStateException()
            }
            if (listOfExpr[i] == "или") {
                booleanOR = true
                continue
            }
            if (!booleanNO) {
                if (listOfExpr[i] == "не") {
                    booleanNO = true
                    continue
                }
                booleanNO = true
                booleanOR = false
                val result = mapOfVars[listOfExpr[i]]!!
                count++
                listOfResult.add(false)
                listOfResult[count] = !result
                continue
            }
            if (listOfExpr[i] == "не") {
                booleanNO = false
                continue
            }
            val result = mapOfVars[listOfExpr[i]]!!
            count++
            listOfResult.add(false)
            listOfResult[count] = result
            booleanOR = false
        }
        return if (true in listOfResult) "ИСТИНА" else "ЛОЖЬ"
    } catch (e: NumberFormatException) {
        throw IllegalStateException()
    } catch (e: IndexOutOfBoundsException) {
        throw IllegalStateException()
    } catch (e: NullPointerException) {
        throw IllegalStateException()
    }
}


