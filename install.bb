#!/usr/bin/env bb

(require '[babashka.fs :as fs]
         '[clojure.string :as str]
         '[babashka.process :refer [shell]])

(def bin-dir "/usr/local/bin/")

(defn create-symlink
  [file link]
  (println file "->" link)
  (shell "ln -sf" file link)
  (shell "chmod +x" file)
  (shell "chmod +x" link))

(let [files (->> (fs/list-dir ".")
                 (map fs/canonicalize)
                 (map str)
                 (filter #(str/ends-with? % ".bb"))
                 (filter #(not (str/ends-with? % "install.bb"))))
      symlinks (->> files
                    (map fs/file-name)
                    (map #(subs % 0 (- (count %) 3)))
                    (map #(str bin-dir %)))]
  (doseq [f files
          s symlinks]
    (create-symlink f s)))