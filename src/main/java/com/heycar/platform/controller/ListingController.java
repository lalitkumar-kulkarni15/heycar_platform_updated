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
import static com.heycar.platform.constants.VehicleListingConstants.COLOR;
import static com.heycar.platform.constants.VehicleListingConstants.MAKE;
import static com.heycar.platform.constants.VehicleListingConstants.MODEL;
import static com.heycar.platform.constants.VehicleListingConstants.YEAR;
import static com.heycar.platform.constants.VehicleListingConstants.LOCATION;
import static com.heycar.platform.constants.VehicleListingConstants.FORWARD_SLASH;

/**
 * <p>
 * This is a restfull service controller which houses the entry point methods to perform the related
 * C.R.U.D. operations on the vehicle listings.
 * /p>
 *
 * @author  Lalit Kulkarni
 * @since   24-05-2019
 * @version 1.0
 */
@Validated
@RestController
@RequestMapping(value = "/heycar")
@Api(value = "Vehicle listing API")
public class ListingController {

    @Autowired
    private IListingSvc listingSvc;

    // Exception messages which are used for throwing the exceptions.
    private static final String LISTING_OBJECT_NULL_SRCH_LSTNG_MSG = "Input search object is null in searchListing()";

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
    @ApiOperation(value = "Upload the vehicle listing to the portal in csv format. ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully uploaded the vehicle listing."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal technical exception has occurred.")
    })
    @PostMapping(value = "/upload_csv/{dealer_id}",consumes = "text/csv",produces = "application/json")
    public ResponseEntity uploadListing(HttpServletRequest request,@PathVariable("dealer_id") String dealerId,
                                        @RequestBody ListingList listing) throws
                                        ListingProcessingException {

        final List<ListingDocument> listingDocument = this.listingSvc.addListingInDataStore(dealerId, listing.getList());
        HttpHeaders headers = getHttpHeaders(request, listingDocument);
        return new ResponseEntity(headers, HttpStatus.CREATED);
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
        listingDocument.stream().forEach(doc -> headers.add(LOCATION, request.getRequestURL().append(FORWARD_SLASH)
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
    @ApiOperation(value = "Upload the vehicle listing to the portal in json format. ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully uploaded the vehicle listing."),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal technical exception has occurred.")
    })
    @PostMapping(value = "/vehicle_listings/{dealer_id}",consumes = "application/json",produces = "application/json")
    public ResponseEntity uploadListing(HttpServletRequest request,@PathVariable("dealer_id") String dealerId,
                                     @Valid @RequestBody List<VendorListing> listing) throws URISyntaxException,
                                     ListingProcessingException {

        final List<ListingDocument> listingDocument = this.listingSvc.addListingInDataStore(dealerId, listing);
        HttpHeaders headers = getHttpHeaders(request, listingDocument);
        return new ResponseEntity(headers,HttpStatus.CREATED);

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
     * @param   allParams                 This object contains the search parameters on the basis of which the vehicle listings
     *                                    would be searched.
     * @return                            {@link ResponseEntity<List<ListingDocument>>}
     * @throws  InvalidInputDataException This exception is thrown if the search params are null.
     */
    @ApiOperation(value = "Search the vehicle listing by search parameters", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved vehicle listings"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal technical exception has occurred.")
    })
    @GetMapping(value = "/searchByParam",produces = "application/json")
    public ResponseEntity<List<VendorListing>> searchListingByParams(@RequestParam Map<String,String> allParams)
            throws InvalidInputDataException, ListingProcessingException {

        List<VendorListing> listingDoc = this.listingSvc.searchListing(mapVendorListingReq(Optional.ofNullable(allParams)
                                 .orElseThrow(()-> new InvalidInputDataException(LISTING_OBJECT_NULL_SRCH_LSTNG_MSG))));
        return ResponseEntity.ok(listingDoc);
    }

    private VendorListing mapVendorListingReq(Map<String, String> allParams) {
        VendorListing vendorListing = new VendorListing();
        vendorListing.setColor(allParams.get(COLOR));
        vendorListing.setMake(allParams.get(MAKE));
        vendorListing.setModel(allParams.get(MODEL));
        vendorListing.setYear(allParams.get(YEAR));
        return vendorListing;
    }

    /**
     * <p>This method searches all the listings which are present in the data store and sends it back to the consumer in the
     * JSON format.</p>
     *
     * @return {@link ResponseEntity<List<ListingDocument>>}
     */
    @ApiOperation(value = "Search all the vehicle listings", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved vehicle listings"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal technical exception has occurred.")
    })
    @GetMapping(value = "/searchAllListings",produces = "application/json")
    public ResponseEntity<List<VendorListing>> searchAllListings() throws ListingProcessingException {

        List<VendorListing> listingDoc = this.listingSvc.findAllListing();
        return ResponseEntity.ok(listingDoc);
    }

}
