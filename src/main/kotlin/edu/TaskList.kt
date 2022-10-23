package edu

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime


const val TASK_COLUMN_LENGTH = 44
enum class Priority {
    C,
    H,
    N,
    L
}

enum class DueTag (longName: String) {
    I ("Intime"),
    T ("Today"),
    O ("Overdue")
}

fun main() {
    // write your code here

    var taskList = mutableListOf<TaskItem>()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val moshiType = Types.newParameterizedType(MutableList::class.java, TaskItem::class.java, DueTag::class.java, Priority::class.java)

    val moshiAdapter = moshi.adapter<MutableList<TaskItem>>(moshiType)

    val jsonFile = File("tasklist.json")
    if (jsonFile.exists()) {
        taskList = moshiAdapter.fromJson(jsonFile.readText())!!
    }


    var actionString: String
    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        actionString = readln().trim().lowercase()

        when (actionString) {
            "end" -> {
                saveAndExit(moshiAdapter, taskList)
                break
            }
            "add" -> inputTask(taskList)
            "print" -> printTaskList(taskList)
            "delete" -> deleteTask(taskList)
            "edit" -> editTask(taskList)
            else -> println("The input action is invalid")
        }
    }
}

fun saveAndExit(moshi: JsonAdapter<MutableList<TaskItem>>, taskList: MutableList<TaskItem>) {

    val jsonFile = File("tasklist.json")
    val jsonString = moshi.toJson(taskList)
    jsonFile.writeText(jsonString)

    println("Tasklist exiting!")
}

fun editTask(taskList: MutableList<TaskItem>) {

    printTaskList(taskList)
    if (taskList.isEmpty()) return

    var validTaskNumber = false
    var taskNumberToEdit = 0
    while (!validTaskNumber) {
        println("Input the task number (1-${taskList.size}):")
        try {
            taskNumberToEdit = readln().toInt()
            if (taskNumberToEdit in 1..taskList.size) {
                taskNumberToEdit-- // list index starts from zero
                validTaskNumber = true
            } else {
                println("Invalid task number")
            }
        } catch (e: Exception) {
            println("Invalid task number")
        }
    }

    var editCompleted = false
    while (!editCompleted) {
        println("Input a field to edit (priority, date, time, task):")

        when (readln()) {
            "priority" -> taskList[taskNumberToEdit].taskPriority = getUserInputForPriority()
            "date" -> taskList[taskNumberToEdit].taskDate =  getUserInputForDate()
            "time" -> taskList[taskNumberToEdit].taskTime = getUserInputForTime()
            "task" -> taskList[taskNumberToEdit].taskContent = getUserInputForTaskContent()
            else -> {
                println("Invalid field")
                continue
            }
        }
        editCompleted = true
        println("The task is changed")
    }
}

fun deleteTask(taskList: MutableList<TaskItem>) {
    printTaskList(taskList)

    if (taskList.isEmpty()) return

    var deleted = false
    var taskNumberToDelete: Int
    while (!deleted) {
        println("Input the task number (1-${taskList.size}):")
        try {
            taskNumberToDelete = readln().toInt()
            if (taskNumberToDelete in 1..taskList.size) {
                taskList.removeAt(taskNumberToDelete - 1)
                deleted = true
                println("The task is deleted")
            } else {
                println("Invalid task number")
            }
        } catch (e: Exception) {
            println("Invalid task number")
        }
    }
}

fun getUserInputForPriority() : Priority {
    while (true) {
        println("Input the task priority (C, H, N, L):")
        try {
            return Priority.valueOf(readln().uppercase())
        } catch (e: IllegalArgumentException){
            continue
        }
    }
}

fun getUserInputForDate() : String {
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        try {
            val matchResult = Regex("""(\d{1,4})-(\d{1,2})-(\d{1,2})""").find(readln())!!
            return LocalDate(matchResult.groupValues[1].toInt(), matchResult.groupValues[2].toInt(),
                matchResult.groupValues[3].toInt()).toString()
        } catch (e: Exception){
            println("The input date is invalid")
            continue
        }
    }
}

fun getUserInputForTaskContent() : List<String> {
    println("Input a new task (enter a blank line to end):")

    val taskItemLines = mutableListOf<String>()

    var inputLine = ""
    do {
        inputLine = readln().trim()
        if (inputLine.isNotEmpty()) taskItemLines.add(inputLine)
    } while (inputLine.isNotEmpty())

    return taskItemLines
}

fun getUserInputForTime() : String {
    while (true) {
        println("Input the time (hh:mm):")
        try {
            val matchResult = Regex("""(\d{1,2}):(\d{1,2})""").find(readln())!!
            return LocalTime.of(matchResult.groupValues[1].toInt(),
                matchResult.groupValues[2].toInt()).toString()
        } catch (e: Exception){
            println("The input time is invalid")
            continue
        }
    }
}


fun inputTask(taskList: MutableList<TaskItem>) {
    val taskPriority = getUserInputForPriority()
    val taskDate = getUserInputForDate()
    val taskTime = getUserInputForTime()
    val taskContent = getUserInputForTaskContent()

    if (taskContent.isNotEmpty()) {
        taskList.add(TaskItem(taskPriority, taskDate, taskTime, taskContent))
    } else {
        println("The task is blank")
    }
}

fun printTaskList(taskList: List<TaskItem> ) {

    if (taskList.isEmpty()){
        println("No tasks have been input")
        return
    }

    println("""
        +----+------------+-------+---+---+--------------------------------------------+
        | N  |    Date    | Time  | P | D |                   Task                     |
        +----+------------+-------+---+---+--------------------------------------------+
    """.trimIndent())

    for (i in taskList.indices) {
        printTaskItem(i, taskList[i])
    }

}

private fun printTaskItem(i: Int, taskItem: TaskItem) {
    taskItem.calculateDueTag()
    val formattedTaskContent = formatContentLines(taskItem.taskContent)
    print("| ${i+1} ${if (i<9) " " else ""}| ${taskItem.taskDate} | ${taskItem.taskTime} | ")
    printPriority(taskItem.taskPriority)
    print (" | ")
    printDueTag(taskItem.dueTag)
    print(" |")
    println("${formattedTaskContent[0]}|")
    for (i in 1..formattedTaskContent.lastIndex) {
        println("|    |            |       |   |   |${formattedTaskContent[i]}|")
    }
    println("""
        +----+------------+-------+---+---+--------------------------------------------+
    """.trimIndent())
}

fun printDueTag(dueTag: DueTag) {
    when (dueTag) {
        DueTag.I -> print("\u001B[102m \u001B[0m")
        DueTag.T -> print("\u001B[103m \u001B[0m")
        DueTag.O -> print("\u001B[101m \u001B[0m")
    }
}

fun printPriority(taskPriority: Priority) {
    when (taskPriority) {
        Priority.C -> print("\u001B[101m \u001B[0m")
        Priority.H -> print("\u001B[103m \u001B[0m")
        Priority.N -> print("\u001B[102m \u001B[0m")
        Priority.L -> print("\u001B[104m \u001B[0m")
    }
}

fun formatContentLines (contentItems: List<String>) : List <String> {
    val formattedLines = mutableListOf<String>()
    for (line in contentItems) {
        var currentLine: String = line
        while (currentLine.length > TASK_COLUMN_LENGTH) {
            formattedLines.add(currentLine.substring(0, TASK_COLUMN_LENGTH))
            currentLine = currentLine.drop(TASK_COLUMN_LENGTH)
        }
        formattedLines.add(currentLine + String(CharArray(44 - currentLine.length){' '}))
    }
    return formattedLines
}

class TaskItem (var taskPriority: Priority, var taskDate: String, var taskTime: String, var taskContent: List<String> ) {
    var dueTag: DueTag
    init {
        dueTag = calculateDueTag()
    }

    fun calculateDueTag() : DueTag {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        val numberOfDays = currentDate.daysUntil(LocalDate.parse(taskDate))
        return when  {
            numberOfDays == 0 -> DueTag.T
            numberOfDays < 0 -> DueTag.O
            else -> DueTag.I
        }
    }
}


