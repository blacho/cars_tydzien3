package pl.psokol.cars.service;

import org.springframework.stereotype.Service;
import pl.psokol.cars.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final List<Car> carList;

    public CarServiceImpl() {
        carList = new ArrayList<Car>();
        carList.add(new Car(1,"Toyota","Corolla","Blue"));
        carList.add(new Car(2,"FSO","Polonez","Red"));
        carList.add(new Car(3,"FSM","Fiat 126p","Grey"));
    }

    @Override
    public Optional<Car> addCar(Car car) {
        boolean isProductExists = carList.stream().anyMatch(element -> element.getId() == car.getId());
        return isProductExists ? Optional.empty() : Optional.of(saveProduct(car));
    }

    @Override
    public List<Car> findAllCars() {
        return carList;
    }

    @Override
    public Optional<Car> updateCar(Car car, long id) {
        return carList.stream()
                .filter(element -> element.getId() == id)
                .findFirst()
                .map(element -> {
                    element.setBrand(car.getBrand());
                    element.setModel(car.getModel());
                    element.setColor(car.getColor());
                    return element;
                });
    }

    private Car saveProduct(Car car) {
        carList.add(car);
        return car;
    }

    @Override
    public Optional<Car> findById(Long carId) {
        return carList.stream().filter(element -> element.getId() == carId).findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color){
        return carList
                .stream()
                .filter(car -> color.equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }

}
