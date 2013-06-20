(ns brainiac.plugins.twitter-search
  (:use [clojure.java.io :only (reader)]
        [twitter.oauth]
        [clojure.contrib.json :only (read-json)])
  (:require [brainiac.plugin :as brainiac]))

(def my-creds (make-oauth-creds "9EzrkvtCbxzBpoU92zuXzg"
								"x3jsJay9slRPTiA06YlaindvMgYox0yFWvqUeXV7J6I"
								"25827733-pwLlCTAgtvjWkoKG3GNIzk1h5gNShTxptzAWJPPQ"
								"P8FUcwcV37v3QN3HNDDAJzQnXi8kHD2nxB7XfExevnY"))

(defn format-tweet [tweet]
  {
   :name (:from_user_name tweet)
   :handle (:from_user tweet)
   :text (:text tweet)
   :profile_image_url (:profile_image_url_https tweet)
   })


(defn transform [stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "twitter-search"
    :data (map format-tweet (:results json)))))

(defn url [term]
  (format (:oauth-creds my-creds ("https://api.twitter.com/1.1/search/tweets.json?q=%s" term))))
  
(defn configure [{:keys [term program-name]}]
  (brainiac/simple-http-plugin
    {:url (url term)}
    transform program-name))
