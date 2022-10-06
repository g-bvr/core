FROM openjdk:16-alpine
RUN apk add --no-cache git curl gnupg openssh &&
    wget https://apache.lauf-forum.at/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip &&
    unzip apache-maven-3.6.3-bin.zip -d /opt/
ENV PATH="/:/opt/apache-maven-3.6.3/bin:${PATH}"
ARG JAR_FILE
ADD ${JAR_FILE} main.jar
ENV GITBEAVER_CLASSPATH plugins
RUN mkdir plugins
ENTRYPOINT ["java", "-cp", "main.jar:plugins", "org.jkube.gitbeaver.Main"]