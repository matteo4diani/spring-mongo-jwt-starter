## What's this?
This is a Spring template for a MongoDB-based backend, with JWT role-based authentication and real-time notifications through Server Sent Events. The project features a fully-documented (in Eclipse, right click on the project and select "Generate Javadoc" to browse the docs as a website) sample app for shift management, including a Dockerfile for automatic containerization.

## Setup
Just import in Eclipse as 'Existing Maven Project', then 'mvn update'. You need a running MongoDB instance/cluster for this project to function: substitute credentials, URL and database name and run the project.

## To Do
1. Implement ENUMS where we have hardcoded collections of sort
2. Implement HATEOAS on relevant entities
3. Abstract away third-party libraries
4. Build init phase
5. Build test suite and environment
6. Implement front-end
7. Setup GitHub CI/CD

