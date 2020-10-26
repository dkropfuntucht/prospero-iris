;; - Copyright 2020 - dkropfuntucht
(ns iris.audio
  (:require [prospero.audio :as proaudio]
            [prospero.events :as proevent]))

(def audio-system (atom {:context nil
                         :sounds  {}}))

(defn ensure-audio-context!
  []
  (if (nil? (:context@audio-system))
    (let [constructor (or js/AudioContext
                          (.. js/window -AudioContext)
                          (.. js/window -webkitAudioContext))
          ctx         (constructor.)]
      (swap! audio-system #(assoc % :context ctx))
      ctx)
    (:context @audio-system)))

(defn source-audio!
  [track-name]
  (if-let [sound (get-in @audio-system [:sounds track-name])]
    sound
    (let [ctx      (ensure-audio-context!)
          element (.getElementById js/document track-name)
          source  (.createMediaElementSource ctx element)
          _       (.connect source (.. ctx -destination))
          sound   {:element element
                   :source  source}]
      (swap! audio-system #(assoc-in % [:sounds track-name] sound))
      sound)))

(defn register-audio-track!
  [track-name track-source opts]
  (let [body    (.querySelector js/document "body")
        element (.getElementById js/document track-name)]
    (when (nil? element)
      (let [aud-elem (.createElement js/document "audio")]
        (.setAttribute aud-elem "id" track-name)
        (.setAttribute aud-elem "src" track-source)
        (.appendChild body aud-elem)))
    (source-audio! track-name)))

(defn queue-audio-track!
  [track-name {:keys [::proaudio/emit-signal-on-end] :as opts}]
  (let [ctx     (ensure-audio-context!)
        state   (.. ctx -state)
        _       (when (= "suspended" state) (.resume ctx))
        sound   (source-audio! track-name)]
    (when (some? emit-signal-on-end)
      (aset (:element sound) "onended"
            (fn [e]
              (proevent/send-signal! emit-signal-on-end track-name))))
    (.play (:element sound))))

(defn stop-audio-track!
  [track-name opts]
  (let [ctx     (ensure-audio-context!)
        state   (.. ctx -state)
        _       (when (= "suspended" state) (.resume ctx))
        sound   (source-audio! track-name)]
    (.pause (:element sound))))
