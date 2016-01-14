/**
 * Created by Jacob on 1/14/2016.
 */

package demo

import java.nio.file.FileSystem
import java.nio.file.FileSystems

fun main(args: Array<String>) {
    var fs = FileSystems.getDefault()
    var roots = fs.getRootDirectories()
    var stores = fs.fileStores
    println(roots)
}

