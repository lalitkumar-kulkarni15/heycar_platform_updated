package com.heycar.platform.controller;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.exception.InvalidInputDataException;
import com.heycar.platform.exception.ListingProcessingException;
import com.heycar.platform.model.ListingList;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.service.IListingSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>This is a restfull service controller which houses the entry point methods to perform the related
 * C.R.U.D. operations on the vehicle listings. /p>
 *
 * @author  Lalit Kulkarni
 * @since   24-05-2019
 * @version 1.0
 */
@Validated
@RestController
@RequestMapping(value = "/heycar")
@Api(tags = "Vehicle listing API", value = "This API houses all the endpoints for adding/modifying/searching listing of vehicles.")
public class ListingController {

    Logger logger = LoggerFactory.getLogger(ListingController.class);

    @Autowired
    private IListingSvc listingSvc;

    // Exception messages which are used for throwing the exceptions.
    private final String DEALER_ID_NULL_MSG = "Dealer id is null as input in upload dListingCsv";
    private final String LISTING_OBJECT_NULL_MSG = "Listing object in uploadListingCsv is null";
    private final String LISTING_LIST_OBJECT_NULL_MSG = "List inside listing object in uploadListingCsv is null";
    private final String LISTING_OBJECT_NULL_SRCH_LSTNG_MSG = "Input search object is null in searchListing()";

    /**
     * <p>This method saves the given vehicle listings data in the csv format into the data store.</p>
     *
     * @param  request        {@link HttpServletRequest}
     * @param  dealerId       This is the dealer id of the dealer who is uploading the vehicle listings.
     * @param  listing        This is the list of vehicle listing object which contains the properties
     *                        specific to the vehicle that needs to be posted to the platform by the dealers.
     *                        {@link VendorListing}
     * @return ResponseEntity Returns the http status code 201-Created if the listings were succesfully posted
     *                        to the data storage {@link HttpStatus}.
     *                        Also returns the uri of the resource created as a part of http response headers
     *                        with the key Location.
     */
    @ApiOperation(value = "Create a new Job Offer.", notes = "This API creates a new Job Offer and stores in the data store.")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Something went wrong in the service, please " + "contact the system" +
                 " administrator - Email - lalitkulkarniofficial@gmail.com"),@ApiResponse(code = 201, message = "Job offer has been" +
            " successfully created in the system.") })
    @ApiResponse(code = 400, message = "Bad input request.Please check the error description for more details.")
    @PostMapping(value = "/upload_csv/{dealer_id}",consumes = "text/csv",produces = "application/json")
    public ResponseEntity uploadListing(HttpServletRequest request,@PathVariable("dealer_id") String dealerId,
                                        @RequestBody ListingList listing) throws InvalidInputDataException,
                                        ListingProcessingException {

        logger.info("Inserting new listing with dealer Id : " + dealerId);
        // Input data validations
        validateListingReqCsv(dealerId, listing);
        // Insert listing to data store.
        final List<ListingDocument> listingDocument = this.listingSvc.addListingInDataStore(dealerId, listing.getList());
        logger.info("Listing inserted successfully");
        // Populate response headers
        HttpHeaders headers = getHttpHeaders(request, listingDocument);
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    /**
     * <p>This method validates the input request parameters - dealer id and listing list. for posting the
     * listing to the dta store. for listings that are posted by the dealers in the csv format.</p>
     *
     * @param dealerId                   This is the dealer id of the dealer who is uploading the vehicle listings.
     * @param listing                    This is the list of vehicle listing object which contains the properties
     *                                   specific to the vehicle that needs to be posted to the platform by the dealers.
     *                                   {@link VendorListing}
     * @throws InvalidInputDataException This exception is thrown if any of the input validations fail.
     */
    private void validateListingReqCsv(@PathVariable("dealer_id") String dealerId, @RequestBody ListingList listing)
            throws InvalidInputDataException {

        Optional.ofNullable(dealerId).orElseThrow(() -> new InvalidInputDataException(DEALER_ID_NULL_MSG));
        Optional.ofNullable(listing).orElseThrow(() -> new InvalidInputDataException(LISTING_OBJECT_NULL_MSG));
        Optional.ofNullable(listing.getList()).orElseThrow(() -> new InvalidInputDataException(LISTING_LIST_OBJECT_NULL_MSG));
    }

    /**
     * <p>This method populates the http response headers with the uri locations.</p>
     *
     * @param  request         This is the http servlet request object.
     * @param  listingDocument This is the list of vehicle listing object which contains the properties
     *                         specific to the vehicle that needs to be posted to the platform by the dealers.
     *                         {@link List<VendorListing>}
     * @return HttpHeaders     {@link HttpHeaders}
     */
    private HttpHeaders getHttpHeaders(HttpServletRequest request, List<ListingDocument> listingDocument) {
        HttpHeaders headers = new HttpHeaders();
        listingDocument.stream().forEach(doc -> headers.add("Location", request.getRequestURL().append("/")
                .append(doc.getDealerIdCode()).toString()));
        return headers;
    }

    /**
     * <p>This method saves the given vehicle listings data in the Json format into the data store.</p>
     *
     * @param  request                   {@link HttpServletRequest}
     * @param  dealerId                  This is the dealer id of the dealer who is uploading the vehicle listings.
     * @param  listing                   This is the list of vehicle listing object which contains the properties
     *                                   specific to the vehicle that needs to be posted to the platform by the dealers.
     *                                   {@link List<VendorListing>}
     * @return ResponseEntity            Returns the http status code 201-Created if the listings were succesfully posted
     *                                   to the data storage {@link HttpStatus}.
     *                                   Also returns the uri of the resource created as a part of http response headers
     *                                   with the key Location.
     * @throws URISyntaxException        This exception is thrown when there is any issue in the URI.
     * @throws InvalidInputDataException This exception is thrown if there is any validation exception.
     */
    @PostMapping(value = "/vehicle_listings/{dealer_id}",consumes = "application/json",produces = "application/json")
    public ResponseEntity uploadListing(HttpServletRequest request,@PathVariable("dealer_id") String dealerId,
                                     @Valid @RequestBody List<VendorListing> listing) throws URISyntaxException,
            InvalidInputDataException, ListingProcessingException {

        logger.info("Inserting new listing with dealer Id : "+dealerId+ " and listing : "+listing.toString());
        // Input data validations
        validateListingReqJson(dealerId, listing);
        // Insert listing to data store.
        final List<ListingDocument> listingDocument = this.listingSvc.addListingInDataStore(dealerId, listing);
        logger.info("Listing inserted successfully");
        // Populate response headers
        HttpHeaders headers = getHttpHeaders(request, listingDocument);
        return new ResponseEntity(headers,HttpStatus.CREATED);

    }

    /**
     * <p>This method validates the input request parameters - dealer id and listing list. for posting the
     * listing to the data store for listings that are posted by the dealers in the Json format.</p>
     *
     * @param dealerId                   This is the dealer id of the dealer who is uploading the vehicle listings.
     * @param listing                    This is the list of vehicle listing object which contains the properties
     *                                   specific to the vehicle that needs to be posted to the platform by the dealers.
     *                                   {@link VendorListing}
     * @throws InvalidInputDataException This exception is thrown if any of the input validations fail.
     */
    private void validateListingReqJson(@PathVariable("dealer_id") String dealerId, @RequestBody @Valid List<VendorListing> listing)
            throws InvalidInputDataException {

        Optional.ofNullable(dealerId).orElseThrow(() -> new InvalidInputDataException(DEALER_ID_NULL_MSG));
        Optional.ofNullable(listing).orElseThrow(() -> new InvalidInputDataException(LISTING_OBJECT_NULL_MSG));
    }

    /**
     * <p>This method provides search capability for searching the vehicle listings on the basis of following properties :-
     *  <ul>
     *      <li>Make</li>
     *      <li>Model</li>
     *      <li>Year</li>
     *      <li>Color</li>
     *  </ul>
     * </p>
     *
     * @param   allParams           This object contains the search parameters on the basis of which the vehicle listings
     *                                    would be searched.
     * @return                            {@link ResponseEntity<List<ListingDocument>>}
     * @throws  InvalidInputDataException This exception is thrown if the search params are null.
     */
    @GetMapping(value = "/searchByParam",produces = "application/json")
    public ResponseEntity<List<VendorListing>> searchListingByParams(@RequestParam Map<String,String> allParams)
            throws InvalidInputDataException, ListingProcessingException {

        // Validate input request.
        Optional.ofNullable(allParams).orElseThrow(()-> new InvalidInputDataException(LISTING_OBJECT_NULL_SRCH_LSTNG_MSG));
        // Search the listing based on the input criteria.
        List<VendorListing> listingDoc = this.listingSvc.searchListing(mapVendorListingReq(allParams));
        return ResponseEntity.ok(listingDoc);
    }

    private VendorListing mapVendorListingReq(@RequestParam Map<String, String> allParams) {
        VendorListing vendorListing = new VendorListing();
        vendorListing.setColor(allParams.get("color"));
        vendorListing.setMake(allParams.get("make"));
        vendorListing.setModel(allParams.get("model"));
        vendorListing.setYear(allParams.get("year"));
        return vendorListing;
    }

    /**
     * <p>This method searches all the listings which are present in the data store and sends it back to the consumer in the
     * JSON format.</p>
     *
     * @return {@link ResponseEntity<List<ListingDocument>>}
     */
    @GetMapping(value = "/searchAllListings",produces = "application/json")
    public ResponseEntity<List<VendorListing>> searchAllListings() throws ListingProcessingException {

        List<VendorListing> listingDoc = this.listingSvc.findAllListing();
        return ResponseEntity.ok(listingDoc);
    }

}
