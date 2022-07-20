(defproject medley-test "1.4.0"
  :description "A lightweight library of useful, mostly pure functions"
  :url "https://github.com/weavejester/medley"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;; NOTE: :dependencies not included for ClojureCLR as per lein-clr version
  ;; 0.2.2
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :plugins [[lein-codox "0.10.8"]
            [lein-cljsbuild "1.1.7"]
            [lein-doo "0.1.10"]
            [lein-clr "0.2.2"]]
  :codox
  {:output-path "codox"
   :metadata {:doc/format :markdown}
   :source-uri "http://github.com/weavejester/medley/blob/{version}/{filepath}#L{line}"}
  :cljsbuild
  {:builds
   {:test
    {:source-paths ["src" "test"]
     :compiler {:output-to "target/main.js"
                :output-dir "target"
                :main medley.test-runner
                :optimizations :simple}}}}
  :clr {:cmd-templates  {:clj-exe   [[CLJCLR15_40 %1]]
                         :clj-dep   ["mono" ["target/clr/clj" %1]]
                         :clj-url   "https://sourceforge.net/projects/clojureclr/files/clojure-clr-1.10.0-Release-net4.6.1.zip/download"
                         :clj-zip   "clojure-clr-1.10.0-Release-net4.6.1.zip"
                         :curl      ["curl" "--insecure" "-f" "-L" "-o" %1 %2]
                         ;:nuget-ver [#_"mono" [*PATH "nuget.exe"] "install" %1 "-Version" %2]
                         ;:nuget-any [#_"mono" [*PATH "nuget.exe"] "install" %1]
                         :unzip     ["unzip" "-d" %1 %2]
                         ;:wget      ["wget" "--no-check-certificate" "--no-clobber" "-O" %1 %2]
                         }
        ;; for automatic download/unzip of ClojureCLR,
        ;; 1. make sure you have curl or wget installed and on PATH,
        ;; 2. uncomment deps in :deps-cmds, and
        ;; 3. use :clj-dep instead of :clj-exe in :main-cmd and :compile-cmd
        :deps-cmds      [
                         [:curl  :clj-zip :clj-url] ; edit to use :curl instead of :wget
                         [:unzip "../clj" :clj-zip]
                         ]
        :main-cmd      [:clj-dep "Clojure.Main461.exe"]
        :compile-cmd   [:clj-dep "Clojure.Compile.exe"]}
  :doo {:paths {:rhino "lein run -m org.mozilla.javascript.tools.shell.Main"}}
  :aliases
  {"test-cljs" ["doo" "rhino" "test" "once"]
   "test-clj"  ["with-profile" "default:+1.7:+1.8:+1.10" "test"]
   "test-cljr" ["clr" "test" "medley.core-test"]
   "test-all"  ["do" ["test-clj"] ["test-cljs"] ["test-cljr"]]}
  :profiles
  {:provided {:dependencies [[org.clojure/clojurescript "1.10.439"]]}
   :test {:dependencies [[org.mozilla/rhino "1.7.7"]]}
   :dev {:dependencies [[criterium "0.4.3"]]
         :jvm-opts ^:replace {}}
   :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.10 {:dependencies [[org.clojure/clojure "1.10.0"]]}})
