# 👋

### What's this?
This is a Spring sample app for a MongoDB-based backend, with JWT role-based authentication and real-time notifications through Server Sent Events. The sample project consists in a well documented shift management web app (in Eclipse, right click on the project and select "Generate Javadoc" to browse the docs as a website).
This sample project includes a Dockerfile to automate the creation of Docker images at build time through [Spotify's Dockerfile Maven](https://github.com/spotify/dockerfile-maven).
I'm developing this project to have a working POC for Spring's latest features, feel free to take from it.

### Setup
Just import in Eclipse as 'Existing Maven Project'. You need a running MongoDB instance/cluster for this project to function: 
* To try out the project substitute DB config and credentials in `application.properties` and run the project. 
* For user facing setups configuration and credentials should be passed through environment variables injected at runtime `(in application.properties you can find a sample of Spring's injection syntax for properties -> "${ENV_VARIABLE_NAME:default-value-if-env-variable-not-found}")`
* To run on Docker, build with Maven with goals='clean package' and `docker run --env MONGODB_USERNAME=yourusername --env MONGODB_PASSWORD=yourpassword --env MONGODB_HOST_NAMES=host.docker.internal:27017 -p 8080:8080 -p 27017:27017 sashacorp/spring-mongo-jwt-api:0.0.1-SNAPSHOT`

# DevLog
### To Do #3
- Build test suite and environment - ✔️ - [How to test Spring apps](https://stackabuse.com/how-to-test-a-spring-boot-application/) - [How to use a separate property file for testing](https://www.baeldung.com/spring-tests-override-properties) - [How to test CORS functionality](https://stackoverflow.com/questions/42588692/testing-cors-in-springboottest) - [How to run integration tests in a specific order](https://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4) - [How to test the web layer of a Spring app](https://spring.io/guides/gs/testing-web/)
- Implement Docker build pipeline - ✔️ - [Multiple port bindings in Docker](https://stackoverflow.com/questions/20845056/how-can-i-expose-more-than-1-port-with-docker) - [How to reach localhost from inside a Docker container](https://stackoverflow.com/questions/24319662/from-inside-of-a-docker-container-how-do-i-connect-to-the-localhost-of-the-mach)
- Enhance test coverage - ONGOING
- Setup GitHub->Heroku CI/CD - ONGOING
- Implement queries by responder
- Add periodical activities (different collection... schedules?), representing fixed schedules, can be overwritten

### To Do #2
- Add Spring Boot Actuator and secure mappings (enabled shutdown mapping) - ✔️ - [How to add shutdown over HTTP function with Spring Actuator](https://www.baeldung.com/spring-boot-shutdown)
- Implement logging - ✔️ - [Spring logging best practices](https://coralogix.com/blog/spring-boot-logging-best-practices-guide/)
- Build init phase - ✔️ - [How to add SPEL conditional expressions to Spring configurators](https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/el-access.html) - [How to schedule startup event listeners](https://stackoverflow.com/questions/27405713/running-code-after-spring-boot-starts)

### To Do #1
- Implement enums where we have hardcoded collections of sort - ✔️
- Implement a generic and light-weight HATEOAS framework (see package util.http and util.http.hateoas) - ✔️
- Abstract away encumbering third-party libraries/method calls - ✔️
- Add postman collection - ✔️ - [How to add postman collection to GitHub repo](https://apitransform.com/how-to-add-postman-collection-to-github/)