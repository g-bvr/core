FROM jkube/java:16-minimal
ARG JAR_FILE
ADD ${JAR_FILE} /main.jar
ENV GITBEAVER_CLASSPATH /gitbeaver/classes
CMD java -jar main.jar