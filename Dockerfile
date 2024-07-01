FROM amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

COPY ${JAR_FILE} my-project.jar

ENTRYPOINT ["java","-jar","/my-project.jar"]