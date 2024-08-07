#!/usr/bin/env bb

(require '[babashka.fs :as fs])
(require '[clojure.string :as str])
(require '[babashka.process :refer [shell]])

(def home-dir (str (fs/home) "/"))
(def bin-dir (str home-dir "bin/"))

(when-not (fs/exists? bin-dir)
  (fs/create-dir bin-dir))

(defn create-symlink
  [file link]
  (println file "->" link)
  (shell "ln -sf" file link)
  (shell "chmod +x" file)
  (shell "chmod +x" link))

(let [files (->> (fs/list-dir ".")
              (map str)
              (filter #(str/ends-with? % ".bb"))
              (filter #(and (not (str/ends-with? % "preloads.bb")) (not (str/ends-with? % "install.bb")))))
      pathless (map fs/file-name files)
      no-exts (map #(subs % 0 (- (count %) 3)) pathless)
      symlinks (map #(str bin-dir %) no-exts)
      canons (map (comp str fs/canonicalize) files)
      pairs (map list canons symlinks)]
  (mapv #(create-symlink (first %) (second %)) pairs))