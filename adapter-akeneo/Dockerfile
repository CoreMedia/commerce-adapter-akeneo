ARG JAVA_APPLICATION_BASE_IMAGE_TAG
FROM coremedia/java-application-base:${JAVA_APPLICATION_BASE_IMAGE_TAG:-2.0.7}
ENV JAVA_HEAP=64m
EXPOSE 6565 8081 8199
HEALTHCHECK --start-period=30s --interval=30s --timeout=3s CMD curl -Lf http://localhost:8081/actuator/health || exit 1
COPY --chown=coremedia:coremedia target/adapter-akeneo-app.jar /coremedia/application.jar
COPY --chown=coremedia:coremedia src/docker /coremedia
# Windows compatibility step
RUN find /coremedia -type d -exec chmod 770 {} \;
CMD ["application"]
