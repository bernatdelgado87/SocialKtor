FROM openjdk:8-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/myBackendWithKtor/ /app/
WORKDIR /app/bin
CMD ["kotlin    /com/example/application/Application.kt"]