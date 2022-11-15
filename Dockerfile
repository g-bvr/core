FROM openjdk:16-alpine
RUN apk add --no-cache git curl gnupg openssh
ADD target/main.jar main.jar
ENV GITBEAVER_CLASSPATH plugins
ADD gitbeaver gitbeaver
RUN mkdir plugins && chmod u+x gitbeaver
ENTRYPOINT ["/gitbeaver"]