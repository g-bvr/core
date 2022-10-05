FROM jkube/java:16
RUN apk add --no-cache git
ARG JAR_FILE
ADD ${JAR_FILE} main.jar
ENV GITBEAVER_CLASSPATH plugins
RUN mkdir plugins
ENTRYPOINT ["java", "-cp", "main.jar:plugins", "org.jkube.gitbeaver.Main"]