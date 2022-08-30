fun main () {

    val numbersArray = readln().split(",").map { it.toInt() }.sorted().distinct()
    println(numbersArray)


    var rangeList = mutableListOf<Int>()

    rangeList.add(numbersArray[0])
    for (i in 1 until numbersArray.size) {
        if (numbersArray[i] -  numbersArray[i-1] == 1) {
            rangeList.add(numbersArray[i])
        } else {
            printRangeList(rangeList, true)
            rangeList = mutableListOf<Int>()
            rangeList.add(numbersArray[i])
        }
    }
    printRangeList(rangeList, false)
}

fun printRangeList(rangeList: List<Int>, comma: Boolean) {
    if (rangeList.size>1) {
        print( ""+ rangeList.first() + "-" + rangeList.last())
    }  else {
        print(rangeList.elementAt(0))
    }
    if (comma) print (",")
}