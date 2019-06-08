package com.heycar.platform.document;

import lombok.*;
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
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "users", type = "user", shards = 1)
public class ListingDocument {

    // A unique combination of the dealer id and code.
    @Id
    private String dealerIdCode;

    private String dealerId;

    private String code;

    private String make;

    private String model;

    public String getkW() {
        return kW;
    }

    public void setkW(String kW) {
        this.kW = kW;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String kW;

    private String year;

    private String color;

    private String price;

}
