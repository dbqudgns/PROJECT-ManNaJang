FROM openjdk:17-jdk

ENV ACCESS_KEY=""
ENV SECRET_KEY=""
ENV JWT_KEY=""
ENV MYSQL_URL=""
ENV MYSQL_USERNAME=""
ENV MYSQL_PASSWORD=""
ENV CHATGPT_KEY=""
COPY build/libs/*SNAPSHOT.jar baeunday_app.jar

ENTRYPOINT ["java", "-jar", "/baeunday_app.jar"]