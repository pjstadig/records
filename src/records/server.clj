(ns records.server
  (:require
   [cheshire.core :as json]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :refer [not-found]]
   [records.core :as core]
   [ring.adapter.jetty :refer [run-jetty]]))

(defonce records (atom #{}))

(defn render-records [records]
  {:headers {"content/type" "application/json"}
   :status 200
   :body (json/encode (vec records))})

(defroutes app
  (POST "/records" {:keys [body] :as req}
        (swap! records into (core/parse-file body))
        {:status 200 :body "OK"})
  (GET "/records/gender" {}
       (let [sorted (core/sort-by-gender @records)]
         (render-records sorted)))
  (GET "/records/birthdate" {}
       (let [sorted (core/sort-by-birthdate @records)]
         (render-records sorted)))
  (GET "/records/name" {}
       (let [sorted (core/sort-by-name @records)]
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
  (reset! records #{}))

(defn restart!
  [port]
  (stop!)
  (start! port))

(defn -main
  ([]
   (-main "8080"))
  ([port]
   (start! (Long/parseLong port))))
