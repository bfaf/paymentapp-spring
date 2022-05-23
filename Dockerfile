FROM openjdk:11
ADD target/*.jar paymentapp
ENTRYPOINT ["java", "-jar","paymentapp"]
EXPOSE 8080
