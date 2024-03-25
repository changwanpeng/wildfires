FROM openjdk:latest

RUN mkdir -p /app/wildfires/backend

WORKDIR /app/wildfires/backend

COPY . .

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]