# Smart Parking Lot Management System 
RESTful APIs using Spring Boot to manage a smart parking lot system. The application can handle vehicle entries and exits, manage parking spots, calculate parking fees based on duration and vehicle type, and provide real-time occupancy status.

## how to build
you can build this project with following code.
since there is priorities in running tests, they should be run manually using IDE so we skip them
```mvn clean install -DskipTests```

## how to run project
you can use following code
```mvn spring-boot:run```

## how to run tests
each test has it's own how-to and should be run by priorities

class ```UserServiceTest```: 
nothing is needed each unit test should work perfectly
class ```ParkingSpotServiceTest```: 
1. first use the ```addParkingSpotTest()``` method to add parking spots it can be run in loop to add multiple parking spots
2. then use `id` from ```addParkingSpotTest()``` to send it for ```deleteParkingSpotTest()``` or ```freeUpParkingSpotTest()```

class ```ParkingLotServiceTest```:
1. first use ```registerVehicleTest()``` method to add vehicle entry and remember the `id`
2. then use ```registerVehicleExitTest()``` method and `id` of previous method to fill the exit time of vehicle

class ```ReportingServiceTest``` and  ```ParkingManagementServiceTest```:
these classes must be called after previous classes are used since they fill the needed data otherwise the test will fail

## Documentation (Swagger)
> [!NOTE]
> documentation is available at:  `http://localhost:8080/swagger/swagger-ui.html`
