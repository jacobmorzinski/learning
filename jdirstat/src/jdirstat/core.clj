(ns jdirstat.core
  (:require (org.tobereplaced.nio [file :as nio.file])
            #_ [clojure.core.async :as async :refer :all]
            [clojure.core.async
             :as async
             :refer [>! <! >!! <!! go go-loop chan buffer
                     close! thread alts! alts!! timeout]])
  (:import (java.nio.file FileSystems
                          Files
                          LinkOption
                          AccessMode
                          AccessDeniedException
                          NoSuchFileException
                          FileVisitResult
                          FileSystem
                          Path
                          FileStore)
           (java.nio.file.attribute DosFileAttributes)
           (java.nio.file.spi FileSystemProvider))
  (:gen-class))

#_
(nio.file/walk-file-tree
  "C:\\Users\\Jacob\\vimfiles\\backup"
  (nio.file/naive-visitor :visit-file (fn [x] (println x))))

(def ^Path my-path (nio.file/path (System/getenv "LOCALAPPDATA")))

(def ^Path my-temp (System/getProperty "java.io.tmpdir"))

; Components of Path p
; The root, if it exists, is not part of the iterator
; and needs to be handled separately.
(defn path->strcomps
  [^Path p]
  (let [comps (-> p .iterator iterator-seq)]
    (if-let [root (nio.file/root p)]
      (map str (conj comps root))
      (map str comps))))

(def resultref (ref {}))

(defn updaterefwithpath [r p]
  (dosync (alter r
                 assoc-in
                 (path->strcomps p)
                 (clojure.walk/keywordize-keys
                   (into {}
                         (nio.file/read-attributes
                           p
                           "*"
                           LinkOption/NOFOLLOW_LINKS))))))



#_(updaterefwithpath resultref (nio.file/absolute-path ""))

(def fv
  (nio.file/file-visitor
    :visit-file
    (fn [f a]
      (updaterefwithpath resultref f)
      FileVisitResult/CONTINUE)
    :visit-file-failed
    (fn [f exc]
      (printf "File failed: %s%n" f)
      (cond
        (instance? AccessDeniedException exc)
        FileVisitResult/CONTINUE
        :else
        (throw exc)
        ))
    :pre-visit-directory
    (fn [d a]
      #_(printf "Pre-dir: %s%n" d)
      FileVisitResult/CONTINUE)
    :post-visit-directory
    (fn [d exc]
      #_(printf "Post-dir: %s%n" d)
      (if exc
        (throw exc)
        FileVisitResult/CONTINUE))))

(defn testwalk
  [path fv]
  (nio.file/walk-file-tree path fv))



(defn updaterefwithgivenattrs [r p a]
  (dosync (alter r
                 assoc-in
                 (path->strcomps p)
                 a)))

(def fv2
  (nio.file/file-visitor
    :visit-file
    (fn [f a]
      (println "f is " f)
      (println "a is " a)
      (updaterefwithgivenattrs resultref f a)
      FileVisitResult/CONTINUE)
    :visit-file-failed
    (fn [f exc]
      (printf "File failed: %s%n" f)
      (cond
        (instance? AccessDeniedException exc)
        FileVisitResult/CONTINUE
        :else
        (throw exc)
        ))
    :pre-visit-directory
    (fn [d a]
      #_(printf "Pre-dir: %s%n" d)
      FileVisitResult/CONTINUE)
    :post-visit-directory
    (fn [d exc]
      #_(printf "Post-dir: %s%n" d)
      (if exc
        (throw exc)
        FileVisitResult/CONTINUE))))


#_ (testwalk (nio.file/path "C:\\Users\\Jacob\\target") fv)
#_ (clojure.walk/postwalk-demo  @resultref)



; Testing core.async
#_ (def in (chan 1))
(defn receiver-go
  [in]
  (go-loop [v (<! in)]
    (when v (println "Got a value in this loop:" v)
            (recur (<! in)))))
;(async/put! in 8)
;=> true
;Got a value in this loop: 8
;(async/close! in)
;=> nil
;(async/put! in 8)
;=> false

#_ (thread (testwalk my-path fv))


;(def resultref (ref {}))
;=> #'jdirstat.core/resultref
;(dosync
;  (alter resultref
;         assoc (str my-path)
;         (nio.file/read-attributes my-path "basic:*")))
;=>
;{"C:\\Users\\Jacob\\AppData\\Local" {"lastAccessTime" #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                     "lastModifiedTime" #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                     "size" 16384,
;                                     "creationTime" #<FileTime 2015-11-28T02:42:21.387966Z>,
;                                     "isSymbolicLink" false,
;                                     "isRegularFile" false,
;                                     "fileKey" nil,
;                                     "isOther" false,
;                                     "isDirectory" true}}


;{(str my-path)
; (nio.file/read-attributes my-path "*" LinkOption/NOFOLLOW_LINKS)}
;=>
;{"C:\\Users\\Jacob\\AppData\\Local" {"lastAccessTime" #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                     "lastModifiedTime" #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                     "size" 16384,
;                                     "creationTime" #<FileTime 2015-11-28T02:42:21.387966Z>,
;                                     "isSymbolicLink" false,
;                                     "isRegularFile" false,
;                                     "fileKey" nil,
;                                     "isOther" false,
;                                     "isDirectory" true}}

;(apply list
;       (str (nio.file/root my-path))
;       (for [i (range (.getNameCount my-path))]
;         (str (.getName my-path i))))
;=> ("C:\\" "Users" "Jacob" "AppData" "Local")


;(->> my-path
;     .iterator
;     iterator-seq
;     (map str))
;=> ("Users" "Jacob" "AppData" "Local")
;(def resultref {})
;=> #'jdirstat.core/resultref
;(def my-tree (->> my-path
;                  .iterator
;                  iterator-seq
;                  (map str)))
;=> #'jdirstat.core/my-tree
;(assoc-in resultref my-tree "New")
;=> {"Users" {"Jacob" {"AppData" {"Local" "New"}}}}





#_ (dosync (alter resultref
               assoc-in (path->strcomps my-path)
               "Nick"))

;(def resultref (ref {}))
;=> #'jdirstat.core/resultref
;(dosync (alter resultref
;               assoc-in (path->strcomps my-path)
;               (clojure.walk/keywordize-keys (into {} (nio.file/read-attributes my-path "*" LinkOption/NOFOLLOW_LINKS)))))
;=>
;{"C:\\" {"Users" {"Jacob" {"AppData" {"Local" {:creationTime #<FileTime 2015-11-28T02:42:21.387966Z>,
;                                               :isSymbolicLink false,
;                                               :isOther false,
;                                               :lastAccessTime #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                               :isRegularFile false,
;                                               :size 16384,
;                                               :lastModifiedTime #<FileTime 2016-01-20T05:47:00.830189Z>,
;                                               :fileKey nil,
;                                               :isDirectory true}}}}}}




(defn ^FileSystem getFS
  "Get the default ^java.nio.file.FileSystem"
  []
  (FileSystems/getDefault))

#_
(defn root-paths
  "Get root directories
  Returns a seq over a ^java.lang.Iterable<java.nio.file.Path>"
  []
  (->> (getFS)
       .getRootDirectories
       .iterator
       iterator-seq))

#_
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

(def ^LinkOption lo-nofollow
  "An array of LinkOption/NOFOLLOW_LINKS for certain java.nio functions."
  (into-array LinkOption [(LinkOption/NOFOLLOW_LINKS)]))

(def ^LinkOption lo-empty
  "An empty array of LinkOption for certain java.nio functions."
  (into-array LinkOption []))

(defn get-path
  "Call (. fs (getPath) x xs...)"
  ([fs x]
   (.getPath fs x (into-array String [])))
  ([fs x & xs]
   (.getPath fs x (into-array String xs))))

(def c (get-path fs "C:\\"))

; Attribute views
(def attribute-views (.supportedFileAttributeViews fs))

; Result on Windows: => #{"owner" "dos" "acl" "basic" "user"}
; Both "dos" and  "basic" have size information,
; as well as "isRegularFile", "isOther", etc.

; Maybe I should get OS-specific attributes...
; like "dos:*" on Windows
#_
(defn get-attrs
  "Get attributes of a filesystem item.  Don't follow links.
  Optional arg v can be a supported attribute view, i.e \"dos\" or \"unix\""
  ([f] (get-attrs f "basic"))
  ([f v] {:path f
          :attrs (Files/readAttributes f (str v ":*") lo-nofollow)}))

#_
(get-attrs c "dos")

;Any way to make this lazy?
;need a more elegant filter.
;TODO: catch get-attrs exceptions inside get-attrs
#_
(defn get-dirstream
  [f]
  (try
    (with-open [s (Files/newDirectoryStream f)]
      (->> s
           .iterator
           iterator-seq
           (map get-attrs)
           (filter #(get-in % [:attrs "isRegularFile"]))
           doall))
    (catch AccessDeniedException _ ())))

#_
(get-dirstream my-music)

;; For the "My Music" that was giving me errors,
;; "isOther" is true (and I wonder what the "attributes" means)
;; "attributes" appears to be a bitfield.
;; Contains bits for isReadonly/hidden/system/ isDirectory / etc

;; consider checking Files/isReadable
;; or isAccessible(var0, new AccessMode[]{AccessMode.READ})

#_
(def attrs (Files/readAttributes c DosFileAttributes lo-empty))
; => #<WindowsFileAttributes sun.nio.fs.WindowsFileAttributes@2776e813>

#_
(Files/readAttributes c "basic:*" lo-nofollow)

#_
(for [v attribute-views]
  (Files/readAttributes c (str v ":*") lo-nofollow))

#_
(reduce into {}
        (for [v attribute-views]
          {v (Files/readAttributes c (str v ":*") lo-nofollow)}))

#_
(FileSystemProvider/installedProviders)

#_
(def provider (.provider fs))
#_
(def access-mode (into-array AccessMode [AccessMode/READ]))
#_
(.checkAccess provider c access-mode)
;=>nil (success?)

(def my-music (get-path fs "C:\\"
                        "Users" "Jacob"
                        "Documents" "My Music"))
#_
(try (.checkAccess provider my-music access-mode)
     (catch AccessDeniedException e e))
;=> AccessDeniedException


(def dropbox (get-path fs "C:\\"
                       "Users" "Jacob"
                       "Dropbox"))
#_
(try (.checkAccess provider dropbox access-mode)
     (catch NoSuchFileException e e))
;=>NoSuchFileException
; The Files/isReadable calls checkAccess in a try block
; and monitors for any generic IO exception


;(WindowsPath$WindowsPathWithAttributes)

;(-> (Files/list dropbox)
;    .iterator
;    iterator-seq
;    first
;    (.toRealPath lo-empty))
;=> #<WindowsPath C:\Users\Jacob\Dropbox (Personal)\.dropbox>
;(-> (Files/list dropbox)
;    .iterator
;    iterator-seq
;    first
;    (.toRealPath lo-nofollow))
;=> #<WindowsPath C:\Users\Jacob\Dropbox\.dropbox>

;(.toRealPath dropbox lo-empty)
;=> #<WindowsPath C:\Users\Jacob\Dropbox (Personal)>
;(.toRealPath dropbox lo-nofollow)
;=> #<WindowsPath C:\Users\Jacob\Dropbox>

;(-> dropbox                ;Path
;    (Files/list)           ;java stream
;    .iterator              ;java iterator
;    iterator-seq           ;clojure seq
;    first                  ;WindowsPathWithAttributes
;    .get                   ;WindowsFileAttributes sun.nio.fs.WindowsFileAttributes
;    .size)                 ;BasicFileAttributes.size()

;However, on Unix with Java 7, I don't get the cached attributes.
;So I should plan to write my own walker.
;Try to open a dir
;cache attributes with files as they are retrieved
;close dirs when done
;allow starting point
;allow max depth



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
      (for [^FileStore store stores]
        (let [total-space (.getTotalSpace store)
              unallocated-space (.getUnallocatedSpace store)
              usable-space (.getUsableSpace store)]
          (doseq []
            (seq (map println [total-space
                               unallocated-space
                               usable-space])))))))
  (println "Hello, World!"))

