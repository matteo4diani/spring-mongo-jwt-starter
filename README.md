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

### To Do #1
- Implement ENUMS where we have hardcoded collections of sort - ✔️
- Implement a generic and ultralight-weight HATEOAS framework (see package util.http and util.http.hateoas) - ✔️
- Abstract away encumbering third-party libraries/method calls - ✔️
- Add postman collection - ✔️ - [https://apitransform.com/how-to-add-postman-collection-to-github/](https://apitransform.com/how-to-add-postman-collection-to-github/)


### To Do #2

- Build test suite and environment
- Build init phase - ✔️ - [https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/el-access.html](https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/el-access.html)
- Setup GitHub->Heroku CI/CD
- Implement queries by responder
- Implement Vue front-end