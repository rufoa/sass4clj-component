# sass4clj-component

This is a Clojure [component](https://github.com/stuartsierra/component/) that performs automatic Sass->CSS compilation using [sass4clj](https://github.com/Deraen/sass4clj/) whenever you modify your Sass files. It is designed to be used with [figwheel](https://github.com/bhauman/lein-figwheel/)'s live CSS reloading feature.

[![Clojars Project](https://img.shields.io/clojars/v/sass4clj-component.svg)](https://clojars.org/sass4clj-component)

## Usage

The config map syntax is the same as [lein-sass4clj](https://github.com/Deraen/sass4clj/tree/master/lein-sass4clj). Valid entries are `:target-path`, `:source-paths`, `:output-style` and `:verbosity`.

Use `map->SassWatcher` to create the component record:

```clojure
(ns example
    (:require [sass4clj-component.core :refer [map->SassWatcher]]
    ; ...

(component/system-map
   ; ...
    :sass-watcher (map->SassWatcher {:source-paths ["resources/sass"]
                                     :target-path  "resources/css"
                                     :output-style :compressed})
```

To read the config from your leiningen `project.clj`, you can use `leiningen.core.project/read` then get the `:sass` key from the map:

```clojure
(ns example
   (:require [sass4clj-component.core :refer [map->SassWatcher]]
             [leiningen.core.project :as p]
   ; ...

   :sass-watcher (map->SassWatcher (:sass (p/read)))
```

This allows the same config to be shared between sass4clj-component and lein-sass4clj.

## License

Copyright © 2016 rufoa

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.