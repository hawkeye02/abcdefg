(ns brainiac.plugins.dark-sky
  (:require [brainiac.plugin :as brainiac]
  			[clojure.contrib.math :as math])
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)]))


(defn format-forecast [forecast]
  {
    :temp (math/round (:temperature forecast))
    :current-summary (:icon forecast)
    :hour-summary (:summary forecast)
  })

(defn transform [stream]
  (let [json (read-json (reader stream))]
    (assoc {}
      :name "dark-sky"
      :title "Right now, outside..."
      :data (format-forecast (:currently json)))))

(defn url [api-key lat lon]
  (format "https://api.forecast.io/forecast/%s/%s,%s" api-key lat lon))

(defn configure [{:keys [api-key lat lon program-name]}]
  (brainiac/simple-http-plugin
    {:url (url api-key lat lon)}
    transform program-name))
