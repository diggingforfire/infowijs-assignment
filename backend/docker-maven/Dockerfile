FROM maven:3.9.9-eclipse-temurin-23 AS builder
WORKDIR /project
COPY . /project/
RUN mvn package

FROM eclipse-temurin:23
ENV APP_DIR=/application
ENV APP_FILE=infowijs.jar

EXPOSE 8888

WORKDIR $APP_DIR
COPY --from=builder /project/target/infowijs-backend*-fat.jar $APP_DIR/$APP_FILE

ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]
