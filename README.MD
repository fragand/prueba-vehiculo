# Design
This project was design under the idea of a car application service.

*These are the packages:*

### Configuration
This package configures the spring boot application and some beans.

### Presentation
This packages allows creating different endpoints or controllers, to receive calls from outside the API.

Currently, there are 1 classes for car application:

* create car application
* get one car application by id: this was develop because of the point 5 "Hacer end-point de consulta", because the information that the user will get is the id and all the information related to the car application  
* get car applications by color


Every endpoint is annotated with @ApiOperation (*spring fox*) meaning that each of them will appear on a swagger ui, 
if you start the project you can find it here http://localhost:8080/swagger-ui.html this page can help you to call every endpoint.

Also, the endpoint code, validates that the request are valid, such us id > 1 

### Service
This is the layer where the business code is implemented such us, create application, get by color, etc 
This also uses spring core, to autowire beans, and also uses the converters from spring to do conversion in a cleaner way

### Model
At this packages, there are the request, responses and  the entities for the business code and the converters, 
so I can handle conversion between each of them.

### Exception
This packages handles all related to errors and exceptions, here is a ControllerAdvice for handling the exceptions 
and converting it into a nice http response, and if there is an error it throws exception depending on it HttpStatus.


# Project specifications

*this project was develop under java 8*

## Framework & libraries
### Maven
For dependency management 
### Spring Mvc
For getting request the project is using spring mvc which allow us to handle json and do rest calls on an easy way
### Spring core
for dependency injection it's used spring core to allow handle dependencies between layers
### Lombok
It's using lombok for the model classes in order to have the builder pattern and much nice stuff for model classes such us,
toString, equals, hashcode and avoid write getters and setters.
https://projectlombok.org/features/all
### Spring fox / swagger
To allow getting the swagger-ui to show available endpoints the project is using spring fox to bind swagger.

## Testing
### Unit test
For unit test it's using junit5 and mockito.
### Integration test
It's using junit5 and SpringBootTest this allows to call an api on an easy way please check AlienCreationTest.
### Running test and building the project
You can run mvn clean install, to run every test and build the project, this will run every unit and integration test.

## Starting the project
Application -> right click -> run

### Manual Test
You can use the swagger-ui to call every endpoint on http://localhost:8080/swagger-ui/#/

# Notes
* The project was build under the idea of a car applications.
* Regarding point 5 from the test (Hacer end-point de consulta) I did an endpoint to get an application for the id, 
because the create endpoint is returning the car application with the id, so instead of searching by age, color, model, 
I thought would be better to get only by id, maybe I misunderstood but since the exercise said that is with the information 
from the creation, and because we have the id there, then I thought that.  
* I didn't update the infrastructure packages at all because it's just for mocking the external services.
