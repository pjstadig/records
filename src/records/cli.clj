(ns records.cli
  (:require
   [records.core :as core]))

(def sorter
  {"gender" core/sort-by-gender
   "birthdate" core/sort-by-birthdate
   "name" core/sort-by-name})

(defn -main
  [sort & files]
  (let [records (into #{}
                      (comp (map core/parse-file)
                            cat)
                      files)
        sorted ((sorter sort) records)]
    (println "LastName,FirstName,Gender,FavoriteColor,DateOfBirth")
    (doseq [record sorted]
      (println (core/format-record record)))))
