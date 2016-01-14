(ns jdirstat.core
  (:import [java.nio.file FileSystems
                          FileSystem
                          FileStore]
           [java.lang Iterable])
  (:gen-class))

(defn ^FileSystem getFS
  "Get FileSystems"
  []
  (FileSystems/getDefault))

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

