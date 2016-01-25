(defproject sass4clj-component "0.1.0"
	:description "Clojure Component version of sass4clj 'auto' compilation mode"
	:url "https://github.com/rufoa/sass4clj-component"
	:license
		{:name "Eclipse Public License"
		 :url "http://www.eclipse.org/legal/epl-v10.html"}
	:dependencies
		[[org.clojure/clojure "1.7.0"]
		 [watchtower "0.1.1"]
		 [com.stuartsierra/component "0.3.1"]
		 [deraen/sass4clj "0.2.0"]]
	:main sass4clj-component.core
	:aot [sass4clj-component.core])