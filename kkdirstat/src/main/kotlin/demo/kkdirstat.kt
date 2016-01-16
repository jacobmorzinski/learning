/**
 * Created by Jacob on 1/14/2016.
 */

package demo

import java.io.File
import java.nio.file.DirectoryStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.collections.forEach
import kotlin.collections.joinToString

fun main(args: Array<String>) {
    val fs = FileSystems.getDefault()
    val roots = fs.rootDirectories
    val stores = fs.fileStores
    val ss = arrayOf<String>("Users", "Jacob", "Documents")
    val p = fs.getPath("C:", *ss)
    // sizeOf(p)
    streamingSizeOf(p)
}

fun streamingSizeOf(p: Path) {
    println("Working on $p")
    if (Files.isDirectory(p)) {
        println("is directory $p")
        val stream: Stream<Path> = Files.list(p) ?: return
        val siz = stream
                    .mapToLong { i: Path -> Files.size(i) }
                    .sum()
        stream.close()
        println(siz)
    } else if (Files.isRegularFile(p)) {
        println("is regular file $p")
    } else {
        throw Error("Wat is this $p")
    }
    Unit
}

fun sizeOf(p: Path) {
    val f = p.toFile()
    if (f.isDirectory) {
        val c = f.listFiles()
        val c2 = c.joinToString(", ")
        println("Directory $f has children $c2")
        for (f2 in c) {
            val siz: Long
            try {
                siz = size(f2)
            } catch(e: Exception) {
                throw Error("Re-throwing", e)
            }
            println("$f2 has size $siz")
        }
    } else if (f.isFile) {
        println("File $f has length")
        println(f.length())
    } else {
        throw Error("Wat is $f")
    }
    val totalSize = size(f)
    println("Total Size is $totalSize")
}

fun size(f: File): Long {
    if (f.isFile) {
        return f.length()
    } else {
        if (f.isDirectory) {
            var s: Long = 0
            val files: Array<out File>? = f.listFiles()
            if (files == null) {
                return 0
            }
            files?.forEach { file -> s += size(file) }
            return s
        } else {
            throw Error("Wat is $f")
        }
    }
}

