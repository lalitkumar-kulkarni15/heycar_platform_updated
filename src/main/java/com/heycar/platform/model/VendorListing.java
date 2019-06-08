package com.heycar.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import lombok.*;
import javax.validation.constraints.NotNull;
import static com.heycar.platform.constants.VehicleListingConstants.FORWARD_SLASH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
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

    @Setter(AccessLevel.NONE)
    @CsvBindByName(column = "make/model",required = true)
    @JsonIgnore
    private String makeModel;

    @NotNull(message = "Model cannot be null")
    private String model;

    @CsvBindByName(column = "power-in-ps",required = true)
    @NotNull(message = "Power cannot be null")
    private String kW;

    public void setMakeModel(String makeModel) {

        if (null != makeModel) {
            this.make = makeModel.split(FORWARD_SLASH)[0];
        }

        if (null != makeModel) {
            this.model = makeModel.split(FORWARD_SLASH)[1];
        }

        this.makeModel = makeModel;
    }

    public String getkW() {
        return kW;
    }

    public void setkW(String kW) {
        this.kW = kW;
    }

}
