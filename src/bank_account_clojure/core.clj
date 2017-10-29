(ns bank-account-clojure.core
  (:require [clojure.string :as str]))

;;private functions

(defn- create-transaction [amount formatted-date-provider-fn]
  {:date (formatted-date-provider-fn) :amount amount})

(defn- add-transaction [account amount formatted-date-fn]
  (let [transaction (create-transaction amount formatted-date-fn)]
    (update-in account [:transactions] #(conj % transaction))))

(defn- transactions-balance [transactions]
  (reduce #(+ %1 (:amount %2)) 0 transactions))

(defn- add-balance-to-transaction [previous-transactions transaction]
  (merge transaction {:balance (+ (:amount transaction) (transactions-balance previous-transactions))}))

(defn- add-balance-to-transactions [transactions]
  (reduce #(conj %1 (add-balance-to-transaction %1 %2)) [] transactions))

(defn- print-transaction [transaction]
  (clojure.string/join " | " [ (:date transaction) (:amount transaction) (:balance transaction)]))

;;public functions

(defn create-account [] {:transactions []})

(defn deposit [account amount formatted-date-provider-fn]
  (add-transaction account amount formatted-date-provider-fn))

(defn withdraw [account amount formatted-date-provider-fn]
  (add-transaction account (- amount) formatted-date-provider-fn))

(defn balance [account]
  (transactions-balance (:transactions account)))

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
