/**
 * Created by Jacob on 1/14/2016.
 */

package demo

import java.io.File
import java.nio.file.FileStore
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.collections.*

fun main(args: Array<String>) {
    val fs = FileSystems.getDefault()
    val roots = fs.rootDirectories
    val stores = fs.fileStores
    val ss = arrayOf<String>("Users", "Jacob", "Documents")
    val p = fs.getPath("C:", *ss)
    val f = p.toFile()
    if (f.isDirectory) {
        val c = f.listFiles()
        val c2 = c.joinToString(", ")
        println("Directory $f has children $c2")
        for (f2 in c) {
            val siz = size(f2)
            println("$f2 has size $siz")
        }
    } else if (f.isFile) {
        println("File $f has length")
        println(f.length())
    } else {
        throw Error("Wat is $f")
    }
    println(size(f))
}

fun size(f: File): Long {
    if (f.isFile) {
        return f.length()
    } else {
        if (f.isDirectory) {
            var s: Long = 0
            f.listFiles().forEach { file -> s += size(file) }
            return s
        } else {
            throw Error("Wat is $f")
        }
    }
}

