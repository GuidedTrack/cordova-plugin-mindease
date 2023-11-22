(ns install-deps
  "Downloads dependencies and puts them in the right places."
  (:require [babashka.fs :as fs]
            [clojure.java.io :as io]
            [org.httpkit.client :as http]))

(def java-deps
  ["https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar"])

(defn url-filename [url] (fs/file-name (.getPath (io/as-url url))))

(defn download-file
  [url target]
  (io/copy (:body @(http/get url {:as :stream})) target))

(defn main
  {:org.babashka/cli {:require [:java-deps-dir]}}
  [{:keys [java-deps-dir]}]
  (when-not (fs/exists? java-deps-dir) (fs/create-dir java-deps-dir))
  (doseq [url java-deps]
    (download-file url (io/file java-deps-dir (url-filename url)))))
