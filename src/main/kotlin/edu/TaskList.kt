package edu

enum class Priority (value: String) {
    CRITICAL("C"),
    HIGH ("H"),
    NORMAL ("N"),
    LOW ("L")
}

fun main() {
    // write your code here

    val taskList = mutableListOf<MutableList<String>>()



    var actionString = ""
    while (true) {
        println("Input an action (add, print, end):")
        actionString = readln().trim().lowercase()

        when (actionString) {
            "end" -> { println("Tasklist exiting!"); break }
            "add" -> inputTask(taskList)
            "print" -> printTaskList(taskList)
            else -> println("The input action is invalid")
        }
    }
}


fun inputTask(taskList: MutableList<MutableList<String>>) {

    println("Input a new task (enter a blank line to end):")

    var taskItem = mutableListOf<String>()

    var inputLine = ""
    while (true){
        inputLine = readln().trim()
        if (inputLine.isNotEmpty()) {
            taskItem.add(inputLine)
        } else {
            if (taskItem.isNotEmpty()) {
                taskList.add(taskItem)
                break
            } else {
                println("The task is blank")
                break
            }
        }
    }
}

fun printTaskList(taskList: List<List<String>> ) {

    if (taskList.isEmpty()){
        println("No tasks have been input")
    }
    else {
        var prefix = ""
        for (i in taskList.indices) {
            prefix = if (i < 9)  "  " else " "
            print(i + 1)
            print(prefix)
            println(taskList[i].first())
            for (taskIndex in 1.. taskList[i].lastIndex) {
                print("$prefix ")
                print(taskList[i][taskIndex])
                println()
            }
            println()
        }
    }
}
/*
data class TaskItem (val p: Priority, date: ){

}
*/


