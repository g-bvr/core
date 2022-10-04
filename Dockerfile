FROM jkube/java:16
ARG JAR_FILE
ADD ${JAR_FILE} /main.jar
ENV GITBEAVER_CLASSPATH /gitbeaver/classes
ENTRYPOINT ["java", "-jar", "main.jar"]