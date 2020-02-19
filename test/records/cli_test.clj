(ns records.cli-test
  (:require
   [clojure.test :refer [deftest is]]
   [records.cli :refer [format-record]]))

(deftest t-format-record
  (is (= "Smith,John,Male,Blue,1/5/1970"
         (format-record {:first "John",
                         :last "Smith",
                         :gender "Male",
                         :color "Blue",
                         :dob #inst "1970-01-05"}))))
