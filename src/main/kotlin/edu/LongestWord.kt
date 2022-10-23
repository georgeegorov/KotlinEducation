package edu

import java.io.File

fun main () {
    println(System.getProperty("user.dir"))
    val f = File ("test.txt")
    println("File " + f.absoluteFile + "  exists =  " + f.exists())

    val s = "privet"

    f.writeText(s)
    f.appendText(s)

    println(f.readText())

}