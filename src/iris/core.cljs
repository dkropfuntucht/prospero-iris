;; - Copyright 2020 - dkropfuntucht
(ns iris.core
  (:require [iris.audio :as audio]
            [iris.events :as events]
            [iris.component :as component]
            [prospero.events :as proevent]
            [prospero.pluggable-game-system :as progame]
            [rum.core :as rum]))

(defmethod progame/initialize-audio! ::game-system
  [game-system arg-map]
  (audio/ensure-audio-context!)
  game-system)

(defmethod progame/register-audio-track! ::game-system
  [game-system track-name track-source opts]
  (audio/register-audio-track! track-name track-source opts))

(defmethod progame/queue-audio-track! ::game-system
  [game-system track-name opts]
  (audio/queue-audio-track! track-name opts))

(defmethod progame/stop-audio-track! ::game-system
  [game-system track-name opts]
  (audio/stop-audio-track! track-name opts))

(def game-render-state (atom {:objects []}))
(def objects-cursor (rum/cursor-in game-render-state [:objects]))

(rum/defc game-root < rum/reactive
  [{:keys [display-background display-width display-height]}]
  (let [state (rum/react objects-cursor)]
    (into
     [:div.game-root {:style {:width       (str display-width "px")
                              :height      (str display-height "px")
                              :position    "absolute"
                              :overflow-x  "hidden"
                              :overflow-y  "hidden"
                              :background  (str "rgb("
                                                (nth display-background 0) ","
                                                (nth display-background 1) ","
                                                (nth display-background 2) ")")}

                      :on-context-menu (fn [e]
                                         (.preventDefault e)
                                         (.stopPropagation e)
                                         false)
                      :on-mouse-down   events/mouse-down-listener
                      :on-mouse-up     events/mouse-up-listener}]
     (map component/game-object state))))


(defmethod progame/initial-setup! ::game-system
  [{:keys [::web-root] :as   game-system} arg-map]
  (events/add-initial-listeners! js/window)
  (rum/mount (game-root game-system) web-root)
  game-system)

(defmethod progame/clear ::game-system
  [game-system  object arg-map]
  (reset! game-render-state {:objects object}))

(defmethod progame/render-object ::game-system
  [game-system  object arg-map])

(defmethod progame/sample-keyboard-state ::game-system
  [game-system arg-map]
  (events/sample-keyboard-state!))

(defmethod progame/sample-mouse-state ::game-system
  [game-system  arg-map]
  (events/sample-mouse-state!))
