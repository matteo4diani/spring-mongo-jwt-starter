# 👋

### What's this?
This is a Spring sample app for a MongoDB-based backend, with JWT role-based authentication and real-time notifications through Server Sent Events. The sample project consists in a well documented shift management web app (in Eclipse, right click on the project and select "Generate Javadoc" to browse the docs as a website).
This sample project includes a Dockerfile to automate the creation of Docker images at build time through [Spotify's Dockerfile Maven](https://github.com/spotify/dockerfile-maven).
I'm developing this project to have a working POC for Spring's latest features, feel free to take from it.

### Setup
Just import in Eclipse as 'Existing Maven Project'. You need a running MongoDB instance/cluster for this project to function: 
* To try out the project substitute DB config and credentials in `application.properties` and run the project. 
* For user facing setups configuration and credentials should be passed through environment variables injected at runtime `(in application.properties you can find a sample of Spring's injection syntax for properties -> "${ENV_VARIABLE_NAME:default-value-if-env-variable-not-found}")`

# DevLog
### To Do #2
- Build test suite and environment - ONGOING - [How to test Spring apps](https://stackabuse.com/how-to-test-a-spring-boot-application/) - [How to use a separate property file for testing](https://www.baeldung.com/spring-tests-override-properties) - [How to test CORS functionality](https://stackoverflow.com/questions/42588692/testing-cors-in-springboottest) - [How to run integration tests in a specific order](https://stackoverflow.com/questions/3693626/how-to-run-test-methods-in-specific-order-in-junit4) - [How to test the web layer of a Spring app](https://spring.io/guides/gs/testing-web/)
- Implement logging - ✔️ - [Spring logging best practices](https://coralogix.com/blog/spring-boot-logging-best-practices-guide/✔️✔️)
- Build init phase - ✔️ - [How to add SPEL conditional expressions to Spring configurators](https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/el-access.html) - [How to schedule startup event listeners](https://stackoverflow.com/questions/27405713/running-code-after-spring-boot-starts)
- Setup GitHub->Heroku CI/CD
- Implement queries by responder

### To Do #1
- Implement enums where we have hardcoded collections of sort - ✔️
- Implement a generic and light-weight HATEOAS framework (see package util.http and util.http.hateoas) - ✔️
- Abstract away encumbering third-party libraries/method calls - ✔️
- Add postman collection - ✔️ - [How to add postman collection to GitHub repo](https://apitransform.com/how-to-add-postman-collection-to-github/)