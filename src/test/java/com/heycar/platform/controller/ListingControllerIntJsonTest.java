package com.heycar.platform.controller;

import com.heycar.platform.HeycarPlatformApplication;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.utils.ITestUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.heycar.platform.constants.ListingTestConstants.*;
import static com.heycar.platform.constants.VehicleListingConstants.*;
import static com.heycar.platform.utils.ITestUtils.getHttpEntityWithHeaders;
import static com.heycar.platform.utils.ITestUtils.readFileAsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {HeycarPlatformApplication.class})
@TestPropertySource(locations = "classpath:application-test-json.properties")
public class ListingControllerIntJsonTest {

    @Value("${server.port}")
    private String port;

    @Value("${application.test.host}")
    private String host;

    @Value("${application.test.postUrlJson}")
    private String postUrlJson;

    /**
     * <p>
     * This test case invokes the upload listing APi by passing the vehicle listing in JSON format.
     * It verifies if the response received has the http status code - 201 Created {@link HttpStatus}</p>
     *
     * @see {@link ListingController}
     * @since 25-05-2019
     */
    @Test
    public void uploadListingTestReturns201CreatedForSuccessfullUpload() {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingPositive(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created with location header.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    /**
     * <p>This test case invokes the upload listing APi by passing the vehicle listing in JSON format.
     * It verifies if the response received has the location header with the uri of created resource.
     * {@link HttpStatus}</p>
     *
     * @see {@link ListingController}
     * @since 25-05-2019
     */
    @Test
    public void uploadListingTestReturnsLocHeaderForSuccessfullUpload() {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingPositive(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created with location header.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getHeaders());
        Assert.assertNotNull(response.getHeaders().getLocation());
        Assert.assertEquals(LOC_HEADER_FR_SUCCESS_LISTNG_UPLOAD, response.getHeaders().getLocation().toString());

    }

    private List<VendorListing> createTestDataForNewVehicleListingPositive() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .color("Blue")
                .kW("2000")
                .model("X1")
                .make(MERCEDEZ)
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;

    }

    private List<VendorListing> createTestDataForNewVehicleListingUpdated() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .color("Pink_Updated")
                .kW("2000")
                .model("X1_Updated")
                .make(MERCEDEZ)
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }


    private HttpHeaders getHttpHeaderJson() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediatypeList = new ArrayList<>();
        mediatypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediatypeList);
        return httpHeaders;
    }

    @Test
    public void uploadListingTestVerifiesUpdatedListingPositive() throws IOException, JSONException {

        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingPositive(),
                getHttpHeaderJson());

        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        HttpEntity<List<VendorListing>> listingDocEntUpdated = new HttpEntity<>(createTestDataForNewVehicleListingUpdated(),
                getHttpHeaderJson());

        restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEntUpdated, String.class);

        final ResponseEntity<String> responseListings = srchStringResponseEntityOfUpdtdListng();

        Assert.assertNotNull(responseListings);
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(LISTING_UPDTD_JSON_RESP_FILE_NM)),
                responseListings.getBody(), JSONCompareMode.LENIENT);

    }

    private ResponseEntity<String> srchStringResponseEntityOfUpdtdListng() {
        HttpEntity<?> entity = getHttpEntityWithHeaders();

        TestRestTemplate restTemplate = new TestRestTemplate();

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort(SRCH_BY_PARAM_URL,
                host, port)).queryParam(YEAR, "1989").queryParam(COLOR, "Pink_Updated")
                .queryParam(MAKE, MERCEDEZ).queryParam(MODEL, "X1_Updated");

        return restTemplate.exchange
                (builder.toUriString(), HttpMethod.GET, entity, String.class);
    }


    @Test
    public void uploadListingTestReturns400BadRequestForMissingCode() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingCode(),
                getHttpHeaderJson());

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(CODE_MISSING_IN_JSON_RESP_FILE_NM)), response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingCode() {

        VendorListing vendorListing = VendorListing.builder()
                .year("1989")
                .color("Blue")
                .kW("2000")
                .model("X1")
                .make(MERCEDEZ)
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingMake() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingMake(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(MAKE_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingMake() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .color("Blue")
                .kW("2000")
                .model("X1")
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingModel() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingModel(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(MODEL_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingModel() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .color("Blue")
                .kW("2000")
                .make(MERCEDEZ)
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingPower() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingPower(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(POWER_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingPower() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .color("Blue")
                .make(MERCEDEZ)
                .model("X1")
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingYear() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingYear(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(YEAR_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingYear() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .color("Blue")
                .make(MERCEDEZ)
                .model("X1")
                .kW("2000")
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingColor() throws IOException, JSONException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingColor(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(COLOR_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingColor() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .make(MERCEDEZ)
                .model("X1")
                .kW("2000")
                .price(TWO_SIXTY_NINE_DOLAR)
                .build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingPrice() throws IOException, JSONException {

        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingPrice(),
                getHttpHeaderJson());

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host, port), HttpMethod.POST, listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(PRICE_MISSING_IN_JSON_RESP_FILE_NM))
                , response.getBody(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingPrice() {

        VendorListing vendorListing = VendorListing.builder()
                .code("ba")
                .year("1989")
                .make(MERCEDEZ)
                .model("X1")
                .color("Blue")
                .kW("2000").build();

        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

}
