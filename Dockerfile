FROM adoptopenjdk:11-jre-hotspot

ADD build/libs/project-webservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
RUN chmod 777 app.jar
ENTRYPOINT ["java","-jar", "app.jar"]