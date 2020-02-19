(ns records.core-test
  (:require
   [clojure.test :refer [deftest is]]
   [records.core :refer [format-record parse-file parse-row sort-records]])
  (:import
   (java.io StringReader)))

(deftest t-parse-row
  (let [row (parse-row ["Smith" "John" "Male" "Blue" "1/5/1970"])]
    (is (= "Smith" (:last row)))
    (is (= "John" (:first row)))
    (is (= "Male" (:gender row)))
    (is (= #inst "1970-01-05T00:00:00.000-00:00" (:dob row)))))

(deftest t-parse-file
  (try
    (parse-file (StringReader. "Smith,John,Male,Blue,1/5/1970"))
    (catch java.io.IOException _
      (is false "should consume input only once"))))

(deftest t-format-record
  (is (= "Smith,John,Male,Blue,1/5/1970"
         (format-record {:first "John",
                         :last "Smith",
                         :gender "Male",
                         :color "Blue",
                         :dob #inst "1970-01-05"}))))

(def records
  [{:first "John",
    :last "Smith",
    :gender "Male",
    :color "Blue",
    :dob #inst "1970-01-05T00:00:00.000-00:00"}
   {:first "Jane",
    :last "Smith",
    :gender "Female",
    :color "Purple",
    :dob #inst "1973-01-05T00:00:00.000-00:00"}
   {:first "John",
    :last "Miller",
    :gender "Male",
    :color "Yellow",
    :dob #inst "1973-06-05T00:00:00.000-00:00"}
   {:first "Jane",
    :last "Miller",
    :gender "Female",
    :color "Blue",
    :dob #inst "1970-06-05T00:00:00.000-00:00"}])

(deftest t-parse-file
  (is (= records (parse-file "comma.csv")))
  (is (= records (parse-file "space.csv")))
  (is (= records (parse-file "pipe.csv"))))

(deftest t-sort-records
  (is (= [{:first "Jane",
           :last "Miller",
           :gender "Female",
           :color "Blue",
           :dob #inst "1970-06-05"}
          {:first "Jane",
           :last "Smith",
           :gender "Female",
           :color "Purple",
           :dob #inst "1973-01-05"}
          {:first "John",
           :last "Miller",
           :gender "Male",
           :color "Yellow",
           :dob #inst "1973-06-05"}
          {:first "John",
           :last "Smith",
           :gender "Male",
           :color "Blue",
           :dob #inst "1970-01-05"}]
         (sort-records "gender" records)))
  (is (= [{:first "John",
           :last "Smith",
           :gender "Male",
           :color "Blue",
           :dob #inst "1970-01-05"}
          {:first "Jane",
           :last "Miller",
           :gender "Female",
           :color "Blue",
           :dob #inst "1970-06-05"}
          {:first "Jane",
           :last "Smith",
           :gender "Female",
           :color "Purple",
           :dob #inst "1973-01-05"}
          {:first "John",
           :last "Miller",
           :gender "Male",
           :color "Yellow",
           :dob #inst "1973-06-05"}]
         (sort-records "birthdate" records)))
  (is (= [{:first "Jane",
           :last "Smith",
           :gender "Female",
           :color "Purple",
           :dob #inst "1973-01-05"}
          {:first "John",
           :last "Smith",
           :gender "Male",
           :color "Blue",
           :dob #inst "1970-01-05"}
          {:first "Jane",
           :last "Miller",
           :gender "Female",
           :color "Blue",
           :dob #inst "1970-06-05"}
          {:first "John",
           :last "Miller",
           :gender "Male",
           :color "Yellow",
           :dob #inst "1973-06-05"}]
         (sort-records "name" records))))
