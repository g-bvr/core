FROM openjdk:17-alpine
RUN apk add --no-cache git curl gnupg openssh
ADD target/main.jar main.jar
ADD lib/google-cloud-pubsub-1.123.2.jar pubsub.jar
ENV GITBEAVER_CLASSPATH plugins
ADD gitbeaver gitbeaver
RUN mkdir plugins && chmod u+x gitbeaver
ENV PATH=$PATH:/root/google-cloud-sdk/bin
ENTRYPOINT ["/gitbeaver"]