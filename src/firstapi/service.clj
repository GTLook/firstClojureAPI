(ns firstapi.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (ring-resp/response "Your todo list"))

(defn test-data
  [request]
  (ring-resp/response [{:task "Make an API" :text "make sure it works before you present it"}
                      {:task "Ask Greg" :text "See if I can set up the repl better"}]))

;;(def counter (atom 0))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
; (def routes #{["/" :get (conj common-interceptors `home-page)]
;               ["/about" :get (conj common-interceptors `about-page)]
;               ["/test" :get (conj common-interceptors `test-data)]
;               ["/inc" :get (fn [request] {:body (str @counter)})
;                        :post (fn [request] {:body (str (swap! counter inc))})]})

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

; Terse/Vector-based routes
(def routes
 `[[["/" {:get home-page}
     ^:interceptors [(body-params/body-params) http/html-body]
     ["/about" {:get about-page}]
     ["/test-data" (get {:get test-data} 1)]]]])


;; Consumed by firstapi.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})
;
; (defn start []
;   (http/start (http/create-server service-map)))
;
; ;; For interactive development
; (defonce server (atom nil))
;
; (defn start-dev []
;   (reset! server
;           (http/start (http/create-server
;                        (assoc service-map
;                               ::http/join? false)))))
;
; (defn stop-dev []
;   (http/stop @server))
;
; (defn restart []
;   (stop-dev)
;   (start-dev))
