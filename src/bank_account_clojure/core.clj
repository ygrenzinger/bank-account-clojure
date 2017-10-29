(ns bank-account-clojure.core
  (:require [clojure.string :as str]))

(defn create-account [] {:transactions []})

(defn- create-transaction [amount formatted-date-provider-fn]
  {:date (formatted-date-provider-fn) :amount amount})

(defn- add-transaction [account amount formatted-date-fn]
  (let [transaction (create-transaction amount formatted-date-fn)]
    (update-in account [:transactions] #(conj % transaction))))

(defn deposit [account amount formatted-date-fn]
  (add-transaction account amount formatted-date-fn))

(defn withdraw [account amount formatted-date-fn]
  (add-transaction account (- amount) formatted-date-fn))

(defn balance [transactions]
  (reduce #(+ %1 (:amount %2)) 0 transactions))

(defn add-balance-to-transaction [previous-transactions transaction]
  (merge transaction {:balance (+ (:amount transaction) (balance previous-transactions))}))

(defn add-balance-to-transactions [transactions]
  (reduce #(conj %1 (add-balance-to-transaction %1 %2)) [] transactions))

(defn print-transaction [transaction]
  (clojure.string/join " | " [ (:date transaction) (:amount transaction) (:balance transaction)]))

(defn print-statement [account]
  (->> (:transactions account)
       add-balance-to-transactions
       reverse
       (map print-transaction)
       (clojure.string/join "\n")
       (str "Date | Amount | Balance\n")))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
