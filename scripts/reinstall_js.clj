(ns reinstall-js
  "Imitates what `cordova plugin add` does with a JavaScript file

   …but much faster. So far it can only deal with one file (see pf:js). When
   we need it to start dealing with more files, we can observe what `cordova
   plugin add` does with those and expand this script accordingly."
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def pf:js "www/mindease.js")

(defn cordova-wrap
  [js]
  (string/join \newline
               ["cordova.define(\"cordova-plugin-mindease.mindease\","
                "function(require, exports, module) {" js "});" ""]))

(defn main
  {:org.babashka/cli {:require [:app-home]}}
  [{:keys [app-home]}]
  (let [wrapped-js (cordova-wrap (slurp pf:js))]
    (doseq [f (file-seq (io/file app-home "platforms"))
            :when (.endsWith (.getPath f)
                             (str "cordova-plugin-mindease/" pf:js))]
      (spit f wrapped-js))))
