;; - Copyright 2020 - dkropfuntucht
(ns iris.events
  (:require [prospero.events :as proevent]))

(def keyboard-down  (atom #{}))
(def keyboard-held  (atom #{}))
(def keyboard-up    (atom #{}))
(def mouse-state    (atom {}))

(defn translate-key
  [key-code]
  ({"ArrowLeft"  ::proevent/key-arrow-left
    "ArrowRight" ::proevent/key-arrow-right
    "ArrowUp"    ::proevent/key-arrow-up
    "ArrowDown"  ::proevent/key-arrow-down

    "Backspace"  ::proevent/key-backspace
    "Delete"     ::proevent/key-delete
    "Pause"      ::proevent/key-pause

    "KeyA"       ::proevent/key-letter-a
    "KeyB"       ::proevent/key-letter-b
    "KeyC"       ::proevent/key-letter-c
    "KeyD"       ::proevent/key-letter-d
    "KeyE"       ::proevent/key-letter-e
    "KeyF"       ::proevent/key-letter-f
    "KeyG"       ::proevent/key-letter-g
    "KeyH"       ::proevent/key-letter-h
    "KeyI"       ::proevent/key-letter-i
    "KeyJ"       ::proevent/key-letter-j
    "KeyK"       ::proevent/key-letter-k
    "KeyL"       ::proevent/key-letter-l
    "KeyM"       ::proevent/key-letter-m
    "KeyN"       ::proevent/key-letter-n
    "KeyO"       ::proevent/key-letter-o
    "KeyP"       ::proevent/key-letter-p
    "KeyQ"       ::proevent/key-letter-q
    "KeyR"       ::proevent/key-letter-r
    "KeyS"       ::proevent/key-letter-s
    "KeyT"       ::proevent/key-letter-t
    "KeyU"       ::proevent/key-letter-u
    "KeyV"       ::proevent/key-letter-v
    "KeyW"       ::proevent/key-letter-w
    "KeyX"       ::proevent/key-letter-x
    "KeyY"       ::proevent/key-letter-y
    "KeyZ"       ::proevent/key-letter-z

    "Space"      ::proevent/key-space-bar}

   key-code
   :unknown))

(defn button-codes
  [buttons]
  (cond-> #{}
    (= 1 (bit-and buttons 1))
    (conj ::proevent/mouse-button-0)

    (= 2 (bit-and buttons 2))
    (conj ::proevent/mouse-button-1)))

(defn find-parent
  [object]
  (loop [obj object]
    (if (= (.. obj -className) "game-root")
      obj
      (recur (.. obj -parentNode)))))

(defn mouse-up-listener
  [e]
  (let [root  (find-parent (.. e -target))
        x     (.. e -clientX)
        y     (.. e -clientY)
        coord [(- x (.. root -offsetLeft))
               (- y (.. root -offsetTop))]
        state @mouse-state
        down  (:down-buttons state #{})
        codes (button-codes (.. e -buttons))
        up    (set (remove #(contains? codes %) down))]

    (.preventDefault e)
    (.stopPropagation e)

    (reset! mouse-state
            {:change        ::proevent/mouse-up
             :event-buttons up
             :down-buttons  codes
             :up-buttons    up
             :mouse-coords  coord}))
  false)

(defn mouse-down-listener
  [e]
  (let [root  (find-parent (.. e -target))
        x     (.. e -clientX)
        y     (.. e -clientY)
        coord [(- x (.. root -offsetLeft))
               (- y (.. root -offsetTop))]
        codes (button-codes (.. e -buttons))]

    (.preventDefault e)
    (.stopPropagation e)

    (reset! mouse-state
            {:change        ::proevent/mouse-down
             :event-buttons codes
             :down-buttons  codes
             :up-buttons    #{}
             :mouse-coords  coord}))
  false)

(defn add-initial-listeners!
  [browser-window]
  (.addEventListener browser-window
                     "keydown"
                     (fn [e]
                       (.preventDefault e)
                       (swap! keyboard-down
                              #(conj % (translate-key (.. e -code))))
                       (swap! keyboard-held
                              #(conj % (translate-key (.. e -code))))))
  (.addEventListener browser-window
                     "keyup"
                     (fn [e]
                       (.preventDefault e)
                       (swap! keyboard-up
                              (fn [s]
                                (-> s
                                    (conj (translate-key (.. e -code)))
                                    (conj ::proevent/key-any-key))))
                       (swap! keyboard-held
                              #(disj % (translate-key (.. e -code)))))))

(defn sample-keyboard-state!
  []
  (let [state {:keyboard-down @keyboard-down
               :keyboard-held @keyboard-held
               :keyboard-up   @keyboard-up}]
    (reset! keyboard-up #{})
    (reset! keyboard-down #{})
    state))

(defn sample-mouse-state!
  []
  (let [state @mouse-state]
    (reset! mouse-state
            {:change        nil
             :event-buttons #{}
             :down-buttons  (:down-buttons @mouse-state)
             :up-buttons    #{}
             :mouse-coords  (:mouse-coords state)})
    state))
