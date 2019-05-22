package com.heycar.platform.model;

import com.opencsv.bean.CsvBindByName;
import javax.validation.constraints.NotNull;

public class VendorListing {

    @NotNull(message = "Code cannot be null")
    @CsvBindByName(column = "code",required = true)
    private String code;

    @CsvBindByName(column = "year",required = true)
    @NotNull(message = "Year cannot be null")
    private String year;

    @CsvBindByName(column = "color",required = true)
    @NotNull(message = "Color cannot be null")
    private String color;

    @CsvBindByName(column = "price",required = true)
    @NotNull(message = "Price cannot be null")
    private String price;

    @NotNull(message = "Make cannot be null")
    private String make;

    @CsvBindByName(column = "make/model",required = true)
    private String makeModel;

    @NotNull(message = "Model cannot be null")
    private String model;

    @CsvBindByName(column = "power-in-ps",required = true)
    @NotNull(message = "Power cannot be null")
    private String kW;

    public String getCode() {
        return code;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(String makeModel) {

        System.out.println("Make model setter called.");
        if (null != makeModel) {
            this.make = makeModel.split("/")[0];
        }

        if (null != makeModel) {
            this.model = makeModel.split("/")[1];
        }

        this.makeModel = makeModel;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getkW() {
        return kW;
    }

    public void setkW(String kW) {
        this.kW = kW;
    }

    @Override
    public String toString() {
        return "VendorListing{" +
                "code='" + code + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", price='" + price + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", kW='" + kW + '\'' +
                '}';
    }
}
