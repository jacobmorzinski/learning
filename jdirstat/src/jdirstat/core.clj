(ns jdirstat.core
  (:import [java.nio.file FileSystems
                          FileSystem
                          Files
                          FileStore
                          LinkOption
                          Path
                          FileVisitor AccessMode]
           (java.nio.file.attribute BasicFileAttributes
                                    DosFileAttributes)
           (java.nio.file.spi FileSystemProvider)
           (sun.nio.fs WindowsFileAttributes))
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

(def fs (getFS))

(-> fs
    (.getPath "C:" (into-array String ["Users" "Jacob" "vimfiles"]))
    .toFile
    .listFiles
    seq)


(def lo-nofollow
  "An array of LinkOption/NOFOLLOW_LINKS for certain java.nio functions."
  (into-array LinkOption [(LinkOption/NOFOLLOW_LINKS)]))

(def lo-empty
  "An empty array of LinkOption for certain java.nio functions."
  (into-array LinkOption []))

(defn get-path
  "Call (. fs (getPath) x xs...)"
  ([fs x]
   (.getPath fs x (into-array String [])))
  ([fs x & xs]
   (.getPath fs x (into-array String xs))))

(def c (get-path fs "C:"))

; Attribute views
(def attribute-views (.supportedFileAttributeViews fs))
; Result on Windows: => #{"owner" "dos" "acl" "basic" "user"}
; Both "dos" and  "basic" have size information,
; as well as "isRegularFile", "isOther", etc.

;; For the "My Music" that was giving me errors,
;; "isOther" is true (and I wonder what the "attributes" means)
;; "attributes" appears to be a bitfield.
;; Contains bits for isReadonly/hidden/system/ isDirectory / etc

;; consider checking Files/isReadable
;; or isAccessible(var0, new AccessMode[]{AccessMode.READ})

(def attrs (Files/readAttributes c DosFileAttributes lo-empty))
; => #<WindowsFileAttributes sun.nio.fs.WindowsFileAttributes@2776e813>

(Files/readAttributes c "basic:*" lo-nofollow)

(for [v attribute-views]
  (Files/readAttributes c (str v ":*") lo-nofollow))

(reduce into {}
        (for [v attribute-views]
          {v (Files/readAttributes c (str v ":*") lo-nofollow)}))

(FileSystemProvider/installedProviders)

(def provider (.provider fs))
(def access-mode (into-array AccessMode [AccessMode/READ]))
(.checkAccess provider c access-mode)
;=>nil (success?)
(.checkAccess provider my-music access-mode)
;=> AccessDeniedException
(.checkAccess provider dropbox access-mode)
;=>NoSuchFileException
; The Files/isReadable calls checkAccess in a try block
; and monitors for any generic IO exception


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

