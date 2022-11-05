package edu


import java.io.File
import kotlin.system.exitProcess

class SearchEngine(file: File) {

    private val peopleList: List<String>
    private val searchIndex: Map<String,List<Int>>

    init {
        peopleList = file.readLines()
        searchIndex = buildWordIndex(peopleList)
    }

    private fun printMenu() {
        println(
            """
=== Menu ===
1. Find a person
2. Print all people
0. Exit
            """
        )
    }

    private fun printPeople() {
        println("=== List of people ===")
        peopleList.forEach(::println)
    }

    private fun findPerson() {
        println("Select a matching strategy: ALL, ANY, NONE")
        val matchType = readln()

        println("Enter a name or email to search all suitable people.")

        val searchStrings = readln().trim().lowercase().split(" ")

        val indexResultSet = mutableSetOf<Int>()
        if (matchType.lowercase() == "any") {
            for (searchString in searchStrings) {
                searchIndex[searchString]?.let { indexResultSet.addAll(it) }
            }
        } else if (matchType.lowercase() == "all") {
            for (searchString in searchStrings) {
                if (indexResultSet.isEmpty()) {
                    searchIndex[searchString]?.let { indexResultSet.addAll(it) }
                } else {
                    searchIndex[searchString]?.let { indexResultSet.retainAll(it.toSet())}
                }
            }
        } else if (matchType.lowercase() == "none") {
            indexResultSet.addAll(peopleList.indices)
            for (searchString in searchStrings) {
                searchIndex[searchString]?.let { indexResultSet.removeAll(it.toSet()) }
            }
        }

        if (indexResultSet.isEmpty())
            println("No matching people found.")
        else {
            for (lineIndex in indexResultSet) {
                println(peopleList[lineIndex])
            }
        }

    }

    fun menuActions() {
        var menuItem: Int
        while (true) {
            try {
                printMenu()

                menuItem = readln().toInt()
                check(menuItem in 0..2)

                when (menuItem) {
                    0 -> break
                    1 -> findPerson()
                    2 -> printPeople()

                }

            } catch (e: Exception) {
                println("Incorrect option! Try again.")
            }
        }
        println("Bye!")
    }


    private fun buildWordIndex(fileLines: List<String>): Map<String, MutableList<Int>> {
        return buildMap {
            for ((lineIndex, line) in fileLines.withIndex()) {
                val wordList = line.lowercase().split(" ")
                for (word in wordList) {
                    if (this.containsKey(word)) {
                        this[word]!!.add(lineIndex)
                    } else {
                        this[word] = mutableListOf(lineIndex)
                    }
                }
            }
        }
    }
}
fun main(args: Array<String>) {
    //println("Hello, World!")
    //    args.forEach(::println)
    val file: File

    val path = System.getProperty("user.dir")
    println("working dir - $path")
    if (args.size<2) {
        println("Filename is required for input: --data filename")
        exitProcess(0)
    }

    if (args[0] == "--data") {
        file = File(args[1])
        if (!file.exists()) {
            println("file ${args[1]} doesn't exist2")
            exitProcess(0)
        }
    } else {
        exitProcess(0)
    }
    //val path = System.getProperty("user.dir")

    val se = SearchEngine(file)

    se.menuActions()
}
