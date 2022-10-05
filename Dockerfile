FROM jkube/java:16
ARG JAR_FILE
ADD ${JAR_FILE} main.jar
ENV GITBEAVER_CLASSPATH plugins
#ENTRYPOINT ["java", "-cp", "main.jar:plugins", "org.jkube.gitbeaver.Main"]