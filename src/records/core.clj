(ns records.core
  (:require
   [clj-time.coerce :as time.coerce]
   [clj-time.format :as time.format]
   [clojure.java.io :as io]
   [reducibles.core :refer [csv-reducible]])
  (:import
   (java.io StringReader StringWriter)))

(defn detect-separator
  [file]
  (with-open [f (io/reader file)]
    (condp re-find (first (line-seq f))
      #" " \space
      #"," \,
      #"\|" \|)))

(def formatter (time.format/formatter "M/d/YYYY"))

(defn parse-row
  [[last first gender color dob]]
  {:first first
   :last last
   :gender gender
   :color color
   :dob (time.coerce/to-date (time.format/parse formatter dob))})

(defn format-record
  [{:keys [last first gender color dob]}]
  (format "%s,%s,%s,%s,%s"
          last
          first
          gender
          color
          (time.format/unparse formatter dob)))

(defn ->string
  ^String [in]
  (with-open [rdr (io/reader in) sw (StringWriter.)]
    (io/copy rdr sw)
    (.toString sw)))

(defn parse-file
  [file]
  (let [str (->string file)]
    (into []
          (map parse-row)
          (csv-reducible (StringReader. str)
                         {:separator (detect-separator (StringReader. str))}))))

(defn sort-records
  [sort records]
  (let [records (into [] records)]
    (case sort
      "gender" (->> records
                    (sort-by :last)
                    (sort-by :gender))
      "birthdate" (sort-by :dob records)
      "name" (->> records
                  (sort-by :last)
                  reverse))))
