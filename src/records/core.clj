(ns records.core
  (:require
   [clj-time.coerce :as time.coerce]
   [clj-time.format :as time.format]
   [clojure.data.csv :as csv]
   [clojure.java.io :as io])
  (:import
   (java.io StringReader StringWriter)))

(defn detect-separator
  [line]
  (condp re-find line
    #" " \space
    #"," \,
    #"\|" \|))

(def formatter (time.format/formatter "M/d/YYYY"))

(defn parse-row
  [[last first gender color dob]]
  {:first first
   :last last
   :gender gender
   :color color
   :dob (time.coerce/to-date (time.format/parse formatter dob))})

(defn parse-file
  "Read the first line to detect separator, then parse the rest of the file."
  [readable]
  (with-open [rdr (io/reader readable)]
    (let [line (.readLine rdr)
          sep (detect-separator line)]
      (-> []
          (into (map parse-row)
                (csv/read-csv (StringReader. line) :separator sep))
          (into (map parse-row)
                (csv/read-csv rdr :separator sep))))))

(defn sort-by-gender
  [records]
  (->> records
       (sort-by :last)
       (sort-by :gender)))

(defn sort-by-birthdate
  [records]
  (sort-by :dob records))

(defn sort-by-name
  [records]
  (->> records
       (sort-by :last)
       reverse))
