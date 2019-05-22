package com.heycar.platform.model;

import com.opencsv.bean.CsvBindByName;
import javax.validation.constraints.NotNull;

public class Listing {

    private long listingId;

    @NotNull(message = "Code cannot be null")
    @CsvBindByName(column = "code",required = true)
    private String code;

    @NotNull(message = "Year cannot be null")
    @CsvBindByName(column = "year",required = true)
    private String year;

    @NotNull(message = "Color cannot be null")
    @CsvBindByName(column = "color",required = true)
    private String color;

    @NotNull(message = "Price cannot be null")
    @CsvBindByName(column = "price")
    private String price;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

}
