const val widePrint: Boolean = false;

fun main () {
    val numRows = 9
    val numColumns = 9
    print("How many mines do you want on the field?")
    val numMines = readln().toInt()

    val area2DArray = Array(numRows) {Array(numColumns){AreaCell()}}

    plantMines(area2DArray, numMines)

    recalculateSurroundingMines(area2DArray)

    printArea(area2DArray, false)

    var areaHasUndiscoveredMines = false
    var userHitMine = false;
    do {
        println("Set/delete mine marks (x and y coordinates):")
        val inputString = readln()
        if (inputString == "exit") System.exit(0)
        val list = inputString.split(" ")
        val userColumn = list[0].toInt()
        val userRow = list[1].toInt()
        val userAction = if (list[2] == "free") USER_ACTION.EXPLORE else USER_ACTION.MARK_MINE

        if (userAction == USER_ACTION.MARK_MINE) {

            if (!area2DArray.elementAt(userRow - 1).elementAt(userColumn - 1).userExplored) {
                area2DArray[userRow - 1][userColumn - 1].userMarkMine = !area2DArray[userRow - 1][userColumn - 1].userMarkMine
            }
        }

        if (userAction == USER_ACTION.EXPLORE) {
            if (area2DArray.elementAt(userRow - 1).elementAt(userColumn - 1).minePlanted) {
                userHitMine = true
            } else if (!area2DArray.elementAt(userRow - 1).elementAt(userColumn - 1).userExplored){
               // area2DArray[userRow - 1][userColumn - 1].userExplored = true
                exploreCell(area2DArray,userRow - 1, userColumn - 1)
            }

        }

        printArea(area2DArray, false)

        areaHasUndiscoveredMines = false
        for (rowArray in area2DArray) {
            for (cell in rowArray) {
                if (cell.minePlanted && !cell.userMarkMine) areaHasUndiscoveredMines = true
            }
        }
    } while (areaHasUndiscoveredMines && !userHitMine)

    if (userHitMine) {
        println()
        printArea(area2DArray, true)
        println("You stepped on a mine and failed!")
    }
    else if (!areaHasUndiscoveredMines) println("Congratulations! You found all the mines!")
}

fun plantMines(area2DArray: Array<Array<AreaCell>>, numMines: Int) {
    val numRows = area2DArray.size
    val numColumns = area2DArray[0].size
    var placedMines = 0;
    do {
        val randomRow = (0 until numRows).random()
        val randomColumn = (0 until numColumns).random()
        if (!area2DArray.elementAt(randomRow).elementAt(randomColumn).minePlanted) {
            area2DArray[randomRow][randomColumn].minePlanted = true
            placedMines++
        }
    }
    while (placedMines < numMines)
}

fun recalculateSurroundingMines(area2DArray: Array<Array<AreaCell>>) {
    val numRows = area2DArray.size
    val numColumns = area2DArray[0].size
    for (row in 0 until numRows) {
        for (column in 0 until numColumns) {
            if (!area2DArray.elementAt(row).elementAt(column).minePlanted) {
                //               area2DArray[row][column].numberSurroundingMines = calculateSurroundingMines(row, numRows, column, numColumns, area2DArray)

                val mineUp = if (row > 0 && area2DArray.elementAt(row-1).elementAt(column).minePlanted) 1 else 0
                val mineDown = if (row < numRows-1 && area2DArray.elementAt(row+1).elementAt(column).minePlanted) 1 else 0
                val mineLeft = if (column > 0 && area2DArray.elementAt(row).elementAt(column-1).minePlanted) 1 else 0
                val mineRight = if (column < numColumns-1 && area2DArray.elementAt(row).elementAt(column+1).minePlanted) 1 else 0
                val mineUpLeft = if (row > 0 && column > 0 && area2DArray.elementAt(row-1).elementAt(column-1).minePlanted) 1 else 0
                val mineUpRight = if (row > 0 && column < numColumns-1 && area2DArray.elementAt(row-1).elementAt(column+1).minePlanted) 1 else 0
                val mineDownLeft = if (row < numRows-1 && column > 0 && area2DArray.elementAt(row+1).elementAt(column-1).minePlanted) 1 else 0
                val mineDownRight = if (row < numRows-1 && column < numColumns-1 && area2DArray.elementAt(row+1).elementAt(column+1).minePlanted) 1 else 0

                area2DArray[row][column].numberSurroundingMines = mineUp + mineDown + mineLeft + mineRight + mineUpLeft + mineUpRight + mineDownLeft + mineDownRight;
            }
        }
    }
}



fun exploreCell(area2DArray: Array<Array<AreaCell>>, exploredRow: Int, exploredColumn: Int) {

    val numRows = area2DArray.size
    val numColumns = area2DArray[0].size

    if (!area2DArray[exploredRow][exploredColumn].userExplored) {
        if (!area2DArray.elementAt(exploredRow).elementAt(exploredColumn).minePlanted) area2DArray[exploredRow][exploredColumn].userExplored = true;
    } else {
        return
    }

    if (area2DArray.elementAt(exploredRow).elementAt(exploredColumn).numberSurroundingMines == 0) {
        if (exploredRow > 0) exploreCell(area2DArray,exploredRow-1, exploredColumn)
        if (exploredRow < numRows-1)  exploreCell(area2DArray,exploredRow+1, exploredColumn)
        if (exploredColumn > 0) exploreCell(area2DArray,exploredRow, exploredColumn-1)
        if (exploredColumn < numColumns-1) exploreCell(area2DArray, exploredRow, exploredColumn+1)
        if (exploredRow > 0 && exploredColumn > 0) exploreCell(area2DArray, exploredRow -1, exploredColumn -1)
        if (exploredRow > 0 && exploredColumn < numColumns - 1) exploreCell(area2DArray, exploredRow -1, exploredColumn + 1)
        if (exploredRow < numRows-1 && exploredColumn > 0) exploreCell(area2DArray, exploredRow+1, exploredColumn-1)
        if (exploredRow < numRows-1 && exploredColumn < numColumns-1) exploreCell(area2DArray, exploredRow + 1, exploredColumn + 1)
    }

}

fun printArea(area2DArray: Array<Array<AreaCell>>, showMines:Boolean) {
    val numColumns = area2DArray.elementAt(0).size;
    if (widePrint) {
        print(" │"); (1 until numColumns).forEach{print("$it ")}; print(numColumns); print("│"); println()
    } else {
        print(" │"); (1 until numColumns+1).forEach{print("$it")}; print("│"); println()
    }

    printDashLine(numColumns)

    for (row in area2DArray.indices) {
        print("${row+1}|")
        val rowArray = area2DArray.elementAt(row);

        if (!showMines) {
            print(
                rowArray.joinToString(if (widePrint) " " else "",
                    transform = {
                        if (it.userExplored) {
                            if (it.numberSurroundingMines == 0) "/" else it.numberSurroundingMines.toString()
                        } else {
                            if (it.userMarkMine) "*" else "."
                        }
                    }
                )
            )
        }
        else {
            print(
                rowArray.joinToString(if (widePrint) " " else "",
                    transform = {
                        if (it.minePlanted) "X" else {
                            if (it.userExplored) {
                                if (it.numberSurroundingMines == 0) "/" else it.numberSurroundingMines.toString()
                            }
                            else
                                "."
                        }
                    }
                )
            )
        }
        print("|")
        println()
    }
    printDashLine(numColumns)
}

fun printDashLine(length: Int) {
    if (widePrint) {
        print("-|"); repeat(length - 1) { print("--") }; print("-"); print("|"); println()
    } else {
        print("-|"); repeat(length) { print("-") }; print("|"); println()
    }
}

class AreaCell {
    var minePlanted = false
    var userMarkMine = false
    var userExplored = false;
    var numberSurroundingMines = 0
}

enum class USER_ACTION {
    EXPLORE,
    MARK_MINE
}
