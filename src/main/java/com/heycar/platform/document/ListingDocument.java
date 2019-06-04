package com.heycar.platform.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * <p>
 *  This is the elastic search document to hold the vehicle listings properties.
 * </p>
 *
 * @since   01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
@Document(indexName = "users", type = "user", shards = 1)
public class ListingDocument {

    public ListingDocument() {
    }

    public ListingDocument(String dealerIdCode,String dealerId, String code, String make, String model, String kW,
                           String year, String color, String price) {

        this.dealerIdCode = dealerIdCode;
        this.dealerId = dealerId;
        this.code = code;
        this.make = make;
        this.model = model;
        this.kW = kW;
        this.year = year;
        this.color = color;
        this.price = price;
    }

    // A unique combination of the dealer id and code.
    @Id
    private String dealerIdCode;

    private String dealerId;

    private String code;

    private String make;

    private String model;

    private String kW;

    private String year;

    private String color;

    private String price;

    public String getDealerIdCode() {
        return dealerIdCode;
    }

    public void setDealerIdCode(String dealerIdCode) {
        this.dealerIdCode = dealerIdCode;
    }

    public String getkW() {
        return kW;
    }

    public void setkW(String kW) {
        this.kW = kW;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public ListingDocument(String dealerId) {
        this.dealerId = dealerId;
    }
}
