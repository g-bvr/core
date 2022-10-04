FROM openjdk:15-alpine as build
RUN ["/opt/openjdk-15/bin/jlink", "--compress=2", \
     "--module-path", "/opt/openjdk-15/jmods/", \
     "--add-modules", "java.base,java.logging,java.xml,jdk.unsupported,java.naming,java.compiler", \
     "--no-header-files", \
     "--output", "/jlinked"]
RUN mvn compile
FROM alpine:latest
COPY --from=build /jlinked /opt/jdk/
ENV PATH="/opt/jdk/bin:${PATH}"
