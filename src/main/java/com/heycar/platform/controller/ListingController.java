package com.heycar.platform.controller;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.model.Listing;
import com.heycar.platform.model.ListingList;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.model.JsonVendorListingList;
import com.heycar.platform.service.IListingSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/heycar")
public class ListingController {

    Logger logger = LoggerFactory.getLogger(ListingController.class);

    @Autowired
    private IListingSvc listingSvc;

    /*@PostMapping(value = "/upload_csv/{dealer_id}",consumes = "text/csv")
    public ResponseEntity<ListingList> postCsvListings(@PathVariable("dealer_id") String dealerId,
                                                    @RequestBody ListingList listings){


        return null;
    }*/

    @PostMapping(value = "/upload_csv/{dealer_id}",consumes = "text/csv")
    public ResponseEntity test(@PathVariable("dealer_id") String dealerId,
                               @RequestBody ListingList listing){

        logger.info("Inserting new listing with dealer Id : "+dealerId+ " and listing : "+listing.toString());
        final List<ListingDocument> listingDocument = this.listingSvc.addListing(dealerId, listing.getList());
        //setUriToHeaders(response, request, listingDocument);
        logger.info("Listing inserted successfully");
        HttpHeaders headers = new HttpHeaders();

        for(ListingDocument doc : listingDocument) {
            headers.add("Location", doc.getDealerIdCode());
        }

        return new ResponseEntity(headers,HttpStatus.CREATED);
    }



    @PostMapping(value = "/vehicle_listings/{dealer_id}",
                consumes = "application/json",
                produces = "application/json")
    public ResponseEntity addListing(
                           @PathVariable("dealer_id") String dealerId,
                           @Valid @RequestBody List<VendorListing> listing) throws URISyntaxException {

        logger.info("Inserting new listing with dealer Id : "+dealerId+ " and listing : "+listing.toString());
        final List<ListingDocument> listingDocument = this.listingSvc.addListing(dealerId, listing);
        //setUriToHeaders(response, request, listingDocument);
        logger.info("Listing inserted successfully");
        HttpHeaders headers = new HttpHeaders();

        for(ListingDocument doc : listingDocument) {
            headers.add("Location", doc.getDealerIdCode());
        }

        return new ResponseEntity(headers,HttpStatus.CREATED);

    }

    private void setUriToHeaders(HttpServletResponse response, HttpServletRequest request, List<ListingDocument> listingDocument) {
        for(ListingDocument doc : listingDocument){
            response.addHeader("Location",request.getRequestURL().append("/").append(doc.getDealerIdCode()).toString());
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<ListingDocument>> searchListing(@RequestBody VendorListing listingSrchPrms) {

        List<ListingDocument> listingDoc = this.listingSvc.searchListing(listingSrchPrms);
        return ResponseEntity.ok(listingDoc);
    }


    @GetMapping(value = "/searchAll")
    public ResponseEntity<List<ListingDocument>> findListing() {

        List<ListingDocument> listingDoc = this.listingSvc.findAllListing();
        return ResponseEntity.ok(listingDoc);
    }

}
