(ns sass4clj-component.core
	(:require
      [com.stuartsierra.component :as component]
      [watchtower.core]
      [sass4clj.core]
		[clojure.java.io :as io])
	(:gen-class))


(defn main-file? [file]
	(and
		(or
			(.endsWith (.getName file) ".scss")
			(.endsWith (.getName file) ".sass") )
		(not (.startsWith (.getName file) "_"))))

(defn find-main-files
	[source-paths]
	(mapcat
		(fn [source-path]
			(let [file (io/file source-path)]
				(->> (file-seq file)
					(filter main-file?)
					(map (fn [x] [(.getPath x) (.toString (.relativize (.toURI file) (.toURI x)))])))))
		source-paths))

(defn compile-sass
	[{:keys [target-path source-paths] :as options}]
	(doseq [[path relative-path] (vec (find-main-files source-paths))
	       :let [output-rel-path (clojure.string/replace relative-path #"\.(sass|scss)$" ".css")
	             output-path     (.getPath (io/file target-path output-rel-path))]]
		(println (format "Compiling Sass: %s" relative-path))
		(sass4clj.core/sass-compile-to-file
			path
			output-path
			(-> options
				(dissoc :target-path :source-paths)
				(update-in [:output-style] (fn [x] (if x (keyword x))))
				(update-in [:verbosity] (fn [x] (or x 1)))))))


(defrecord SassWatcher [target-path source-paths output-style verbosity watcher-future]
	component/Lifecycle

	(start [component]
		(println "Starting Sass watcher for paths" source-paths)
		(let [f (watchtower.core/watcher
				source-paths
				(watchtower.core/rate 100)
				(watchtower.core/file-filter watchtower.core/ignore-dotfiles)
				(watchtower.core/file-filter (watchtower.core/extensions :scss :sass))
				(watchtower.core/on-change (constantly (compile-sass {:target-path target-path :source-paths source-paths :output-style output-style :verbosity verbosity}))))]
			(assoc component :watcher-future f)))

	(stop [component]
		(println "Stopping Sass watcher")
		(some-> component :watcher-future future-cancel)
		(assoc component :watcher-future nil)))