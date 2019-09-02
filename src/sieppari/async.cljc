(ns sieppari.async
  #?(:clj (:refer-clojure :exclude [await])))

(defprotocol AsyncContext
  (continue [t f])
  #?(:clj (await [t])))

(defn async?
  [x]
  (satisfies? AsyncContext x))

#?(:clj
   (extend-protocol AsyncContext
     clojure.lang.IDeref
     (continue [c f] (let [p (promise)]
                       (future (p (f @c)))
                       p))
     (await [c] @c)))

#?(:cljs
   (extend-protocol AsyncContext
     js/Promise
     (continue [t f] (.then t f))))