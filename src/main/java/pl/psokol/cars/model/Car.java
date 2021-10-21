package pl.psokol.cars.model;

import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


public class Car extends RepresentationModel {
    @NotNull(message = "Id can't be null")
    @Max(100000)
    private long id;

    @NotNull(message = "brand can't be null")
    private String brand;

    @NotNull(message = "model can't be null")
    private String model;

    @NotNull(message = "color can't be null")
    private String color;

    public Car(long id, String mark, String model, String color) {
        this.id = id;
        this.brand = mark;
        this.model = model;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
