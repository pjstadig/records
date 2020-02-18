(ns records.core-test
  (:require
   [clj-time.core :refer [date-time]]
   [clojure.test :refer [deftest is]]
   [records.core :refer [parse-file parse-row sort-records]]))

(deftest t-parse-row
  (let [row (parse-row ["Smith" "John" "Male" "Blue" "1/5/1970"])]
    (is (= "Smith" (:last row)))
    (is (= "John" (:first row)))
    (is (= "Male" (:gender row)))
    (is (= (date-time 1970 1 5) (:dob row)))))

(deftest t-sort-records
  (let [records (parse-file "comma.csv")]
    (is (= [{:first "Jane",
             :last "Miller",
             :gender "Female",
             :color "Blue",
             :dob (date-time 1970 6 5)}
            {:first "Jane",
             :last "Smith",
             :gender "Female",
             :color "Purple",
             :dob (date-time 1973 1 5)}
            {:first "John",
             :last "Miller",
             :gender "Male",
             :color "Yellow",
             :dob (date-time 1973 6 5)}
            {:first "John",
             :last "Smith",
             :gender "Male",
             :color "Blue",
             :dob (date-time 1970 1 5)}]
           (sort-records "gender" records)))
    (is (= [{:first "John",
             :last "Smith",
             :gender "Male",
             :color "Blue",
             :dob (date-time 1970 1 5)}
            {:first "Jane",
             :last "Miller",
             :gender "Female",
             :color "Blue",
             :dob (date-time 1970 6 5)}
            {:first "Jane",
             :last "Smith",
             :gender "Female",
             :color "Purple",
             :dob (date-time 1973 1 5)}
            {:first "John",
             :last "Miller",
             :gender "Male",
             :color "Yellow",
             :dob (date-time 1973 6 5)}]
           (sort-records "dob" records)))
    (is (= [{:first "John",
             :last "Smith",
             :gender "Male",
             :color "Blue",
             :dob (date-time 1970 1 5)}
            {:first "Jane",
             :last "Smith",
             :gender "Female",
             :color "Purple",
             :dob (date-time 1973 1 5)}
            {:first "John",
             :last "Miller",
             :gender "Male",
             :color "Yellow",
             :dob (date-time 1973 6 5)}
            {:first "Jane",
             :last "Miller",
             :gender "Female",
             :color "Blue",
             :dob (date-time 1970 6 5)}]
           (sort-records "last" records)))))
