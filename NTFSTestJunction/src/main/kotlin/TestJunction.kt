import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/*
 * Based on a gist
 *  https://gist.github.com/peterkir/1df3f0bf8508d8836101
 * from an answer
 *  http://stackoverflow.com/a/29699473/3599738
 * to a StackOverflow post "Determine whether a file is a junction (in Windows) or not?"
 *  http://stackoverflow.com/questions/13733275/determine-whether-a-file-is-a-junction-in-windows-or-not
 */

/*
 *
 * prepare the directory from cmd with admin rights
 *
 * mkdir c:\temp\a
 * cd /d c:\temp
 * mklink /j junction_to_a a
 * mklink /d symlink_to_a a
 *
 */
object TestJunction {

    /*
     * return on my machine
     *
     * # test for junction
     * a             -> isJunction = false
     * junction_to_a -> isJunction = true
     * symlink_to_a  -> isJunction = true
     *
     * # test for symlink
     * a             -> isSymLink = false
     * junction_to_a -> isSymLink = false
     * symlink_to_a  -> isSymLink = true
     *
     */

    @JvmStatic fun main(args: Array<String>) {

        val dir = "c:/temp"
        println("\n# test for junction")
        println("a             -> isJunction = " + isJunction(Paths.get(dir, "a")))
        println("junction_to_a -> isJunction = " + isJunction(Paths.get(dir, "junction_to_a")))
        println("symlink_to_a  -> isJunction = " + isJunction(Paths.get(dir, "symlink_to_a")))

        println("\n# test for symlink")
        println("a             -> isSymLink = " + Files.isSymbolicLink(Paths.get(dir, "a")))
        println("junction_to_a -> isSymLink = " + Files.isSymbolicLink(Paths.get(dir, "junction_to_a")))
        println("symlink_to_a  -> isSymLink = " + Files.isSymbolicLink(Paths.get(dir, "symlink_to_a")))
    }

    /**
     * returns true if the Path is a Windows Junction
     */
    private fun isJunction(p: Path): Boolean {
        var isJunction = false
        try {
            isJunction = p.compareTo(p.toRealPath()) != 0
        } catch (e: IOException) {
            e.printStackTrace() // TODO: handleMeProperly
        }

        return isJunction
    }
}