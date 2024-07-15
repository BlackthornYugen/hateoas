#!/usr/bin/env bash
# Check out https://start.spring.io/
curl https://start.spring.io/starter.zip -o demo.zip \
    --data           'type=maven-project' \
    --data           'language=java' \
    --data           'platformVersion=3.3.1' \
    --data           'packaging=jar' \
    --data           'jvmVersion=22' \
    --data           'groupId=com.example' \
    --data           'artifactId=demo' \
    --data           'name=demo' \
    --data-urlencode "description=My dog doesn't need a passport" \
    --data           'packageName=com.example.demo' \
    --data           'dependencies=devtools,lombok,data-rest,data-jpa,h2'
sha256sum demo.zip
unzip -f demo.zip
mvn clean test