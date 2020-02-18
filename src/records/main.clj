(ns records.main
  (:require
   [records.core :as core]))

(defn -main
  [sort & files]
  (let [records (into #{}
                      (comp (map core/parse-file)
                            cat)
                      files)
        sorted (core/sort-records sort records)]
    (println "LastName,FirstName,Gender,FavoriteColor,DateOfBirth")
    (doseq [record sorted]
      (println (core/format-record record)))))
