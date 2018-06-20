FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/firstapi-0.0.1-SNAPSHOT-standalone.jar /firstapi/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/firstapi/app.jar"]
