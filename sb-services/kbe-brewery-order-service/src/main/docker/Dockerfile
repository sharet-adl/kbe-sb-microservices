FROM openjdk:17-slim as builder
WORKDIR application
ADD maven/${project.build.finalName}.jar ./
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM openjdk:17-slim
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}
LABEL "collect_logs_with_filebeat"="true"
LABEL "decode_log_event_to_json_object"="true"

#ENV JAVA_OPTS=""

EXPOSE 8080

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.launch.JarLauncher"]