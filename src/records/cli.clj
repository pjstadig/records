(ns records.cli
  (:require
   [clj-time.coerce :as time.coerce]
   [clj-time.format :as time.format]
   [records.core :as core]))

(def sorter
  {"gender" core/sort-by-gender
   "birthdate" core/sort-by-birthdate
   "name" core/sort-by-name})

(defn format-record
  [{:keys [last first gender color dob]}]
  (format "%s,%s,%s,%s,%s"
          last
          first
          gender
          color
          (time.format/unparse core/formatter (time.coerce/to-date-time dob))))

(defn -main
  [sort & files]
  (let [records (into #{}
                      (comp (map core/parse-file)
                            cat)
                      files)
        sorted ((sorter sort) records)]
    (println "LastName,FirstName,Gender,FavoriteColor,DateOfBirth")
    (doseq [record sorted]
      (println (format-record record)))))
