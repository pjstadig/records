(ns records.main
  (:require
   [clj-time.format :as time]
   [clojure.java.io :as io]
   [reducibles.core :refer [csv-reducible]]))

(defn detect-separator
  [file]
  (with-open [f (io/reader file)]
    (condp re-find (first (line-seq f))
      #" " \space
      #"," \,
      #"\|" \|)))

(def formatter (time/formatter "M/d/YYYY"))

(defn parse-row
  [[last first gender color dob]]
  {:first first
   :last last
   :gender gender
   :color color
   :dob (time/parse formatter dob)})

(defn parse-file
  [file]
  (eduction (map parse-row)
            (csv-reducible file {:separator (detect-separator file)})))

(defn sort-records
  [sort records]
  (case sort
    "gender" (->> records
                  (sort-by :last)
                  (sort-by :gender))
    "dob" (sort-by :dob records)
    "last" (->> records
                (sort-by :last)
                reverse)))

(defn print-record
  [{:keys [last first gender color dob]}]
  (println (format "%s,%s,%s,%s,%s"
                   last
                   first
                   gender
                   color
                   (time/unparse formatter dob))))

(defn -main
  [sort & files]
  (let [records (into #{}
                      (comp (map parse-file)
                            cat)
                      files)
        sorted (sort-records sort records)]
    (println "LastName,FirstName,Gender,FavoriteColor,DateOfBirth")
    (doseq [record sorted]
      (print-record record))))
