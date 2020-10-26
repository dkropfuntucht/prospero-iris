(defproject prospero/iris "0.1.0"
  :description "Iris React renderer for Prospoero"
  :url "https://github.com/dkropfuntucht/prospero-iris"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [prospero/prospero   "0.1.0"]
                 [rum                 "0.12.3"]]
  :repl-options {:init-ns iris.core})
