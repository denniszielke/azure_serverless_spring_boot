# springfunctions

#https://stackabuse.com/dockerizing-a-spring-boot-application/
mvn spring-boot:run

mvn clean install
docker build -t denniszielke/springservicebus:6 .
docker run denniszielke/springservicebus -e "SB_CONNECTIONSTRING="


docker hub function https://hub.docker.com/_/microsoft-azure-functions-java

KeyVault: https://azure.microsoft.com/en-us/blog/simplifying-security-for-serverless-and-web-apps-with-azure-functions-and-app-service/

https://docs.microsoft.com/en-gb/azure/app-service/app-service-key-vault-references

ServiceBus Message properties
https://docs.microsoft.com/de-de/azure/azure-functions/functions-bindings-service-bus-trigger?tabs=java

Output bindings: https://docs.microsoft.com/en-us/azure/azure-functions/functions-bindings-service-bus-output?tabs=javascript

https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-java-applicationinsights

<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <Properties>
    <Property name="LOG_PATTERN">
      %d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${hostName} --- [%15.15t] %-40.40c{1.} : %m%n%ex
    </Property>
  </Properties>
  <Appenders>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%message%n</pattern>
    </encoder>
  </appender>
  <appender name="aiAppender"
    class="com.microsoft.applicationinsights.logback.ApplicationInsightsAppender">
  </appender>
  </Appenders>
  <logger name="com.azure.core" level="INFO" />
  <logger name="org.springframework.web" level="INFO"/>
  <root level="INFO">
    <appender-ref ref="STDOUT" />
  </root>
  <root level="trace">
    <appender-ref ref="aiAppender" />
  </root>
</configuration>


<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%message%n</pattern>
    </encoder>
  </appender>
  <appender name="aiAppender" 
      class="com.microsoft.applicationinsights.logback.ApplicationInsightsAppender">
        <instrumentationKey></instrumentationKey>
    </appender>
  <logger name="com.azure.core" level="INFO" />
  <logger name="org.springframework.web" level="INFO"/>
<Loggers>
        <Root level="trace">
          <AppenderRef ref="aiAppender"/>
        </Root>
      </Loggers>
</configuration>



<!-- <Configuration packages="com.microsoft.applicationinsights.log4j.v2">
  <Properties>
    <Property name="LOG_PATTERN">
      %d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${hostName} --- [%15.15t] %-40.40c{1.} : %m%n%ex
    </Property>
  </Properties>
  <Appenders>
    <Console name="Console" target="SYSTEM_OUT">
      <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
    </Console>
    <ApplicationInsightsAppender name="aiAppender">
    </ApplicationInsightsAppender>
  </Appenders>
  <Loggers>
    <Root level="trace">
      <AppenderRef ref="Console"  />
      <AppenderRef ref="aiAppender"  />
    </Root>
  </Loggers>
</Configuration> -->
<!-- 
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%message%n</pattern>
    </encoder>
  </appender>
  <logger name="com.azure.core" level="ERROR" />

  <root level="INFO">
    <appender-ref ref="STDOUT" />
  </root>
</configuration> -->