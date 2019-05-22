package com.heycar.platform.model;

import com.opencsv.bean.CsvBindByName;

public class CsvVendorListing extends Listing {

    @CsvBindByName(column = "make/model",required = true)
    private String makeModel;

    @CsvBindByName(column = "power-in-ps",required = true)
    private String powerInPs;

    public String getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }

    public String getPowerInPs() {
        return powerInPs;
    }

    public void setPowerInPs(String powerInPs) {
        this.powerInPs = powerInPs;
    }

}
