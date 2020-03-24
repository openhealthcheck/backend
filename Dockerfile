# docker build -t wirvsvirus-backend .
# docker container run -p 8080:8080 wirvsvirus-backend
FROM openjdk:13-jdk-alpine AS build

ENV MAVEN_VERSION 3.6.0
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . .

RUN mvn clean install

FROM openjdk:13-jdk-alpine AS deploy
COPY --from=build /workspace/target/openhealthcheck-backend-0.0.1-SNAPSHOT.jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
