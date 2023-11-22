(ns classpath
  "Prints the classpath for saving in $CLASSPATH.

   This is so that you can lint and compile the Java sources without having to
   build a whole Cordova project. The classpath consists of the Android
   sources, the Cordova sources and whatever else is needed for this plugin.
   These sources are taken from where they're already installed. This is
   expedient, but not robust. If Cordova put their classes.jar in a different
   place, this script breaks."
  (:require [babashka.fs :as fs]
            [clojure.string :as string]))

(defn check-classpath-entries
  [entries]
  (doseq [e entries]
    (when-not (fs/exists? e)
      (binding [*out* *err*] (println "warning:" e "doesn't exist")))))

(defn main
  {:org.babashka/cli {:require [:android-home :app-home :java-deps-dir]}}
  [{:keys [android-home app-home java-deps-dir]}]
  (let
    [classpath-entries
       (into
         [(str android-home "/platforms/android-33/android.jar")
          (str
            app-home
            "/platforms/android/CordovaLib/build/intermediates/compile_library_classes_jar/debug/classes.jar")]
         (fs/glob java-deps-dir "*.jar"))]
    (check-classpath-entries classpath-entries)
    (println (string/join ":" classpath-entries))))
