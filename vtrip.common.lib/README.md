-  VTRIP JAVA COMMON LIBRARY
- JAVA VERSION: 21
- HOW TO USE THIS LIBRARY:
  + 1 - Add the dependency in your pom.xml file:
    ```xml
    <dependency>
      <groupId>com.vtrip</groupId>
      <artifactId>vtrip.common.lib</artifactId>
      <version>enter_version_here</version>
    </dependency>
    ```
  + 2 - Add authentication in your Maven settings.xml (~/.m2/settings.xml) file:
    ```xml
    <settings>
      <servers>
        <server>
          <id>github</id>
          <username>GITHUB_USERNAME</username>
          <password>YOUR_PERSONAL_ACCESS_TOKEN</password>
        </server>
      </servers>
    </settings>
    ```
  + 3 - Add GitHub Packages as a repository in your pom.xml file:
    ```xml
    <repositories>
      <repository>
        <id>github</id>
        <name>GitHub CloudHMS Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/CloudHMS/vtrip.common.lib</url>
      </repository>
    </repositories>
    ```

- HOW TO USE CONFIG:
  + Use local with application.yml
  + Use external file:
    + Add env variable: ENV_CONFIG_URL with value is the link of file.
    + Must add ```EnvironmentPostProcessor``` key in ```spring.factories```. 
      + https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/resources/META-INF/spring.factories#L1
    + Key must be the same with application if you want to replace it. 
    If you want to add mapping key (individual/prefix group), you have to implement ```IExternalConfigMappingProvider``` and add key in ```spring.factories``` with value is the class that you implement.
      + Config: https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/resources/META-INF/spring.factories#L2
      + Mapping: https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/java/your_company_name/your_project_name/your_module_name/config/mapper/CustomExternalConfigMapping.java#L8C1-L8C7


- HOW TO USE CACHING SERVICE:
  + Call provider already defined to use or write new and implement from ```IBaseCacheProvider```.
    + https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/java/your_company_name/your_project_name/your_module_name/service/greeting/GreetingService.java#L38


- HOW TO USE KAFKA:
  + Add config Kafka in config file
    + Example: https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/config/application.yml#L17
  + Call ```@KafkaListener``` with defined factories: 
    + ```autoCommitListenerFactory```: for auto commit
      + Example: https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/java/your_company_name/your_project_name/your_module_name/pubsub/consumer/ReservationKafkaConsumer.java#L16
    + ```manualCommitListenerFactory```: for manual commit (ackMode: MANUAL_IMMEDIATE)
      + Example: https://github.com/CloudHMS/vtrip.project.template/blob/6983439a9053bab00c72faca31742f40c6709d4a/src/main/java/your_company_name/your_project_name/your_module_name/pubsub/consumer/ReservationKafkaConsumer.java#L28