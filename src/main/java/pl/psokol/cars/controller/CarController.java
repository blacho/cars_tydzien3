package pl.psokol.cars.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.psokol.cars.model.Car;
import pl.psokol.cars.service.CarServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars", produces = {
        MediaType.APPLICATION_JSON_VALUE/*,
        MediaType.APPLICATION_XML_VALUE*/})

public class CarController {

    private final CarServiceImpl carService;

    public CarController(CarServiceImpl carService) {
        this.carService = carService;
    }

    // https://stackoverflow.com/questions/60762394/where-is-spring-resourcesupport
    //https://docs.spring.io/spring-hateoas/docs/current/reference/html/
    @GetMapping
    ResponseEntity<CollectionModel<Car>> getAllCars(){
        List<Car> allCars = carService.findAllCars();
        allCars.forEach(car -> addLinkToCar(car));
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> carEntityModels = new CollectionModel<>(allCars, link);
        return new ResponseEntity<>(carEntityModels, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<EntityModel<Car>> getCarById(@PathVariable long id){
        Link link = linkTo(CarController.class).slash(id).withSelfRel();
        Optional<Car> carOptional = carService.findById(id);

        if(carOptional.isPresent()){
            EntityModel<Car> carEntityModel = new EntityModel<>(carOptional.get(), link);
            return new ResponseEntity<>(carEntityModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/query")
    ResponseEntity<CollectionModel<Car>> getCarByColorParam(@RequestParam String color) {
        List<Car> carList1 =  carService.getCarsByColor(color);

        if (carList1.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carList1.forEach(car -> addLinkToCar(car));
        carList1.forEach(car -> car.add(linkTo(CarController.class).withRel("allColors")));
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> carEntityModels = new CollectionModel<>(carList1, link);
        return new ResponseEntity<>(carEntityModels, HttpStatus.OK);
    }

    @GetMapping(path = "/color/{color}")
    ResponseEntity<CollectionModel<Car>> getCarByColorPath(@PathVariable String color) {
        List<Car> carList1 =  carService.getCarsByColor(color);

        if (carList1.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carList1.forEach(car -> addLinkToCar(car));
        carList1.forEach(car -> car.add(linkTo(CarController.class).withRel("allColors")));
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> carEntityModels = new CollectionModel<>(carList1, link);
        return new ResponseEntity<>(carEntityModels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCar(@Validated @RequestBody Car car){
        List<Car> carList1 =  carService.findAllCars();
        boolean add = carList1.add(car);
        if(add){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
     }

     @PutMapping
    public  ResponseEntity modCar(@RequestBody Car newCar ){
         List<Car> carList1 =  carService.findAllCars();
         Optional<Car> first =carList1.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
         if (first.isPresent()) {
             carList1.remove(first.get());
             carList1.add(newCar);
             return new ResponseEntity<>(HttpStatus.CREATED);
         }
         return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }

    @PatchMapping("/car/{id}")
    public  ResponseEntity modCarField(@RequestBody Car partialUpdateCar, @PathVariable long id ){
        Optional<Car> car = carService.updateCar(partialUpdateCar, id);
        if (car.isPresent()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Car> delCarById(@PathVariable long id){
        List<Car> carList1 =  carService.findAllCars();
        Optional<Car> first =carList1.stream().filter(car -> car.getId() == id).findFirst();
        if(first.isPresent()){
            carList1.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void addLinkToCar( Car car) {
        car.add(linkTo(CarController.class).slash(car.getId()).withSelfRel());
    }

}
