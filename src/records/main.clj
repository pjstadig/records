(ns records.main
  (:require
   [clj-time.format :as time]
   [records.core :as core]))

(def formatter (time/formatter "M/d/YYYY"))

(defn format-record
  [{:keys [last first gender color dob]}]
  (format "%s,%s,%s,%s,%s"
          last
          first
          gender
          color
          (time/unparse formatter dob)))

(defn -main
  [sort & files]
  (let [records (into #{}
                      (comp (map core/parse-file)
                            cat)
                      files)
        sorted (core/sort-records sort records)]
    (println "LastName,FirstName,Gender,FavoriteColor,DateOfBirth")
    (doseq [record sorted]
      (println (format-record record)))))
