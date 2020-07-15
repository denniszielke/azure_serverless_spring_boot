# springfunctions

#https://stackabuse.com/dockerizing-a-spring-boot-application/
```
mvn spring-boot:run

mvn clean install
docker build -t denniszielke/springservicebus:6 .
docker run denniszielke/springservicebus -e "SB_CONNECTIONSTRING="

```
