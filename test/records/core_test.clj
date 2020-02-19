(ns records.core-test
  (:require
   [clojure.test :refer [deftest is]]
   [records.core :refer [parse-file parse-row
                         sort-by-gender
                         sort-by-name
                         sort-by-birthdate]])
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

(deftest t-sort-by-gender
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
         (sort-by-gender records))))

(deftest t-sort-by-birthdate
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
         (sort-by-birthdate records))))

(deftest t-sort-by-name
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
         (sort-by-name records))))
