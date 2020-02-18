(ns records.main-test
  (:require
   [clj-time.core :refer [date-time]]
   [clojure.test :refer [deftest is]]
   [records.main :refer [format-record]]))

(deftest t-format-record
  (is (= "Smith,John,Male,Blue,1/5/1970"
         (format-record {:first "John",
                         :last "Smith",
                         :gender "Male",
                         :color "Blue",
                         :dob (date-time 1970 1 5)}))))
