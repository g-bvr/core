FROM openjdk:16-alpine
RUN apk add --no-cache git curl gnupg openssh
ARG JAR_FILE
ADD ${JAR_FILE} main.jar
ENV GITBEAVER_CLASSPATH plugins
ADD gitbeaver /bin/sh
RUN mkdir plugins && chmod u+x /bin/sh/gitbeaver
ENTRYPOINT ["gitbeaver"]