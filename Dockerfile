FROM openjdk:16-alpine
RUN apk add --no-cache git curl gnupg openssh
ARG JAR_FILE
ADD ${JAR_FILE} main.jar
ENV GITBEAVER_CLASSPATH plugins
RUN mkdir plugins
ENTRYPOINT ["java", "-cp", "main.jar:plugins", "org.jkube.gitbeaver.Main"]