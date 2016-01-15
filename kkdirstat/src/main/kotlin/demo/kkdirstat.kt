/**
 * Created by Jacob on 1/14/2016.
 */

package demo

import java.nio.file.FileSystems
import kotlin.collections.joinToString

fun main(args: Array<String>) {
    var fs = FileSystems.getDefault()
    var roots = fs.rootDirectories
    var stores = fs.fileStores
    var ss  = arrayOf<String>("Users", "Jacob", ".bash_history")
    val p = fs.getPath("C:", *ss)
    val f = p.toFile()
    if (f.isDirectory) {
        println("Directory $f has files")
        println(f.list().joinToString(","))
    } else if (f.isFile) {
        println("File $f has length")
        println(f.length())
    } else {
        throw Error("Wat is $f")
    }
}

