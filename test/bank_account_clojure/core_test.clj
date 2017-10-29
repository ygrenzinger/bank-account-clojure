(ns bank-account-clojure.core-test
  (:require [clojure.test :refer :all]
            [bank-account-clojure.core :refer :all]))

(deftest a-test

  (testing "should create account"
    (is (not (nil? (create-account)))))

  (testing "should create a deposit transaction"
    (is (= {:transactions [{:date "2017/08/20" :amount 200}]}
           (deposit (create-account) 200 (fn [] "2017/08/20")))))

  (testing "should create a withdraw transaction"
    (is (= {:transactions [{:date "2017/08/20" :amount -300}]}
           (withdraw (create-account) 300 (fn [] "2017/08/20")))))

  (testing "should add several transactions"
    (is (= {:transactions [{:date "2017/08/19" :amount 100}
                           {:date "2017/08/20" :amount 200}
                           {:date "2017/08/21" :amount -300}]}
           (-> (create-account)
               (deposit 100 (fn [] "2017/08/19"))
               (deposit 200 (fn [] "2017/08/20"))
               (withdraw 300 (fn [] "2017/08/21"))))))

  (testing "should compute balance for account"
    (is (= 50
           (-> (create-account)
               (deposit 100 (fn [] "2017/08/19"))
               (deposit 200 (fn [] "2017/08/20"))
               (withdraw 250 (fn [] "2017/08/21"))
               balance))))

  (testing "Print statement for account with multiple transactions"
    (is (= (-> (create-account)
               (deposit 1000 (fn [] "2017/08/18"))
               (withdraw 500 (fn [] "2017/08/19"))
               (deposit 100 (fn [] "2017/08/20"))
               (print-statement))
           (clojure.string/join
             "\n"
             [
              "Date | Amount | Balance"
              "2017/08/20 | 100 | 600"
              "2017/08/19 | -500 | 500"
              "2017/08/18 | 1000 | 1000"
              ]
             )))))
