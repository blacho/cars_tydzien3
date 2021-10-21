package pl.psokol.cars.service;

import pl.psokol.cars.model.Car;
import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> findAllCars();
    Optional<Car> addCar(Car car);
    Optional<Car> updateCar(Car car, long id);
    Optional<Car> findById(Long carId);
    List<Car> getCarsByColor(String color);
}
