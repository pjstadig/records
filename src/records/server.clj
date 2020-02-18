(ns records.server
  (:require
   [clojure.string :as str]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :refer [not-found]]
   [records.core :refer [format-record parse-file sort-records]]
   [ring.adapter.jetty :refer [run-jetty]]))

(defonce records (atom []))

(defn render-records [records]
  {:headers {"content/type" "text/plain"}
   :status 200
   :body (str/join "\n" (map format-record records))})

(defroutes app
  (POST "/records" {:keys [body] :as req}
        (swap! records into (parse-file body))
        {:status 200 :body "OK"})
  (GET "/records/gender" {}
       (let [sorted (sort-records "gender" @records)]
         (render-records sorted)))
  (GET "/records/birthdate" {}
       (let [sorted (sort-records "dob" @records)]
         (render-records sorted)))
  (GET "/records/name" {}
       (let [sorted (sort-records "last" @records)]
         (render-records sorted)))
  (not-found "Not Found"))

(defonce server (atom nil))

(defn start!
  [port]
  (swap! server
         (fn [server]
           (if server
             server
             (run-jetty app {:port port :join? false})))))

(defn stop!
  []
  (swap! server
         (fn [server]
           (when server
             (.stop server)
             nil)))
  (reset! records []))

(defn restart!
  [port]
  (stop!)
  (start! port))

(defn -main
  []
  (start! 8080))
