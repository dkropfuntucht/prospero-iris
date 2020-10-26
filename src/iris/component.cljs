;; - Copyright 2020 - dkropfuntucht
(ns iris.component
  (:require [rum.core :as rum]))

(declare game-object)
(rum/defc game-object
  [{:keys [border-radius
           children
           colour-rgb
           colour-rgba
           height
           render-status
           rotation
           status
           text
           text-padding
           translation
           sprite-sheet
           text-rgb
           texture
           width
           wireframe]}]

  (let [[pos-x pos-y pos-z] translation]
    (into
     [:div.object {:style (cond-> {:position "absolute"
                                   :width    (str width "px")
                                   :height   (str height "px")}

                            (= render-status :hidden)
                            (merge {:display "none"})

                            (and (number? pos-x) (number? pos-y))
                            (merge
                             {:top      (str pos-y "px")
                              :left     (str pos-x "px")})

                            (number? pos-z)
                            (merge {:z-index pos-z})

                            (number? rotation)
                            (merge
                             {:transform (str "rotate(" rotation "rad)")})

                            (string? texture)
                            (merge {:background-image (str "url(" texture ")")})

                            (and (map? texture) (= (:type texture) :sprite))
                            (merge {:background-image
                                    (str "url(" (:sprite-sheet-path sprite-sheet) ")")
                                    :background-position
                                    (str (* -1
                                            (:sprite-width sprite-sheet)
                                            (:col texture)) "px"
                                         " "
                                         (* -1
                                            (:sprite-height sprite-sheet)
                                            (:row texture)) "px")})

                            (some? border-radius)
                            (merge {:border-radius (str border-radius "px")})

                            (and (not wireframe) (some? colour-rgb))
                            (merge {:background (str "rgb("
                                                     (nth colour-rgb 0) ","
                                                     (nth colour-rgb 1) ","
                                                     (nth colour-rgb 2) ")")})

                            (and (not wireframe) (some? colour-rgba))
                            (merge {:background (str "rgba("
                                                     (nth colour-rgba 0) ","
                                                     (nth colour-rgba 1) ","
                                                     (nth colour-rgba 2)","
                                                     (nth colour-rgba 3) ")")})

                            (and wireframe (some? colour-rgb))
                            (merge {:border (str "solid 1px rgb("
                                                 (nth colour-rgb 0) ","
                                                 (nth colour-rgb 1) ","
                                                 (nth colour-rgb 2) ")")})

                            (and wireframe (some? colour-rgba))
                            (merge {:border (str "solid 1px rgba("
                                                 (nth colour-rgba 0) ","
                                                 (nth colour-rgba 1) ","
                                                 (nth colour-rgba 2)","
                                                 (nth colour-rgba 3) ")")})

                            (some? text-rgb)
                            (merge {:color (str "rgb("
                                                (nth text-rgb 0) ","
                                                (nth text-rgb 1) ","
                                                (nth text-rgb 2)")")})

                            (some? text)
                            (merge {:cursor "default"})

                            (some? text-padding)
                            (merge {:padding-left   (nth text-padding 0)
                                    :padding-top    (nth text-padding 1)
                                    :padding-right  (nth text-padding 2)
                                    :padding-bottom (nth text-padding 3)}))}
      (when (some? text)
        text)]

     (map game-object children))))
