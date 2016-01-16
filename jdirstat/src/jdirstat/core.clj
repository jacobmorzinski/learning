(ns jdirstat.core
  (:import [java.nio.file FileSystems
                          FileSystem
                          Files
                          FileStore
                          LinkOption
                          Path
                          FileVisitor]
           [java.lang Iterable]
           (java.nio.file.attribute BasicFileAttributes))
  (:gen-class))

(defn getFS
  "Get the default ^java.nio.file.FileSystem"
  []
  (FileSystems/getDefault))

(defn root-paths
  "Get root directories
  Returns a seq over a ^java.lang.Iterable<java.nio.file.Path>"
  []
  (->> (getFS)
       .getRootDirectories
       .iterator
       iterator-seq))

(defn root-filestores
  "Get root filestores
  Returns a seq over a ^java.lang.Iterable<java.nio.file.FileStore>"
  []
  (->> (getFS)
       .getFileStores
       .iterator
       iterator-seq))

; Use (root-filestores) to get a list of roots
; Use FileStore/getFileStore(Path) to see what filestore a file is in

(def ^FileStore ffs (first (root-filestores)))
(doto ffs
     .type
     .name)

(-> fs
    (.getPath "C:" (into-array String ["Users" "Jacob" "vimfiles"]))
    .toFile
    .listFiles
    seq)

; For this multi-arity function, Clojure guesses wrong.
; We have to be explicit that the second arg is an empty array.
(.getPath fs "/" (make-array String 0))
(.getPath fs "/" (into-array String []))
(.getPath fs "/" (into-array String ["etc"]))

(.getPath fs "C:" (into-array String []))
(.getPath fs "C:" (into-array String ["Users"]))
(.getPath fs "C:" (into-array String ["Users" "Jacob"]))

(-> fs
    (.getPath "C:" (into-array String ["Users"]))
    .toFile
    .isDirectory)


(Files/readAttributes
  ufile
  BasicFileAttributes
  (into-array LinkOption []))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [fs (getFS)
        fsp (.provider fs)
        fssep (.getSeparator fs)
        roots (.getRootDirectories fs)
        stores (.getFileStores fs)
        fss (iterator-seq (.iterator stores))]
    (doseq []
      (seq (map println [fs fsp fssep roots]))
      (for [store stores]
        (let [total-space (.getTotalSpace store)
              unallocated-space (.getUnallocatedSpace store)
              usable-space (.getUsableSpace store)]
          (doseq []
            (seq (map println [total-space
                               unallocated-space
                               usable-space])))))))
  (println "Hello, World!"))

