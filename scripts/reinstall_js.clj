(ns reinstall-js
  "Imitates what cordova plugin add does with a JavaScript script file

   …but much faster. So far it can only deal with one file (see pf:js). When
  we need it to start dealing with more files, we can observe what cordova
  plugin add does with those and expand this task accordingly."
  (:require [babashka.cli :as cli]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(def pf:js "www/mindease.js")

(defn cordova-wrap
  [js]
  (string/join \newline
               ["cordova.define(\"cordova-plugin-mindease.mindease\","
                "function(require, exports, module) {" js "});" ""]))

(def cli-opts {:args->opts [:app-path], :require [:app-path]})

(defn -main
  [& args]
  (let [{:keys [app-path]} (cli/parse-opts args cli-opts)
        wrapped-js (cordova-wrap (slurp pf:js))]
    (doseq [f (file-seq (io/file app-path "platforms"))
            :when (.endsWith (.getPath f)
                             (str "cordova-plugin-mindease/" pf:js))]
      (spit f wrapped-js))))
