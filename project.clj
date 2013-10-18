(defproject kraken "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 ;[ring/ring-json "0.2.0"]
                 [ring-middleware-format "0.3.1"]
                 [com.taoensso/carmine "2.3.1"]
                 [clojurewerkz/elastisch "1.2.0"]
                 [clj-time "0.6.0"]
                 [cascalog/cascalog-core "2.0.0-SNAPSHOT"]
                 [clojurewerkz/elastisch "1.0.2"]]
  
  :profiles { :dev {:dependencies [[org.apache.hadoop/hadoop-core "1.1.2"]]}}
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler kraken.server.handler/app
         :nrepl {:start? true}
         ;:auto-reload? true
         })
