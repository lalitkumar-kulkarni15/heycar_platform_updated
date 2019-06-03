package com.heycar.platform.controller;

import com.heycar.platform.HeycarPlatformApplication;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.utils.ITestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import static com.heycar.platform.constants.ListingTestConstants.MERCEDEZ;
import static com.heycar.platform.constants.ListingTestConstants.TWO_SIXTY_NINE_DOLAR;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
                classes = {HeycarPlatformApplication.class})
@TestPropertySource(locations = "classpath:application-test-json.properties")
public class ListingControllerIntJsonTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${server.port}")
    private String port;

    @Value("${application.test.host}")
    private String host;

    @Value("${application.test.postUrlJson}")
    private String postUrlJson;

    /**
     * <p>This test case invokes the upload listing APi by passing the vehicle listing in JSON format.
     *   It verifies if the response received has the http status code - 201 Created {@link HttpStatus}</p>
     *
     * @since 25-05-2019
     * @see   {@link ListingController}
     */
    @Test
    public void uploadListingTestReturns201CreatedForSuccessfullUpload(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingPositive(),
                                                        getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
         ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                                   host,port ),HttpMethod.POST,listingDocEnt, String.class);

         // Check if the response is not null and the http status code is - 201 Created.
         Assert.assertNotNull(response);
         Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingPositive(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setYear("1989");
        vendorListing.setCode("ba");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }


    private HttpHeaders getHttpHeaderJson(){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediatypeList = new ArrayList<>();
        mediatypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediatypeList);
        return httpHeaders;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingCode(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingCode(),
                                                        getHttpHeaderJson());

        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingCode(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setYear("1989");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingMake(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingMake(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingMake(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        vendorListing.setModel("X1");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingModel(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingModel(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingModel(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingPower(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingPower(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingPower(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setColor("Blue");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingYear(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingYear(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingYear(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setColor("Blue");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setkW("2000");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingColor(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingColor(),
                getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingColor(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setkW("2000");
        vendorListing.setPrice(TWO_SIXTY_NINE_DOLAR);
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTestReturns400BadRequestForMissingPrice(){

        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingMissingPrice(),
                getHttpHeaderJson());

        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlJson,
                host,port ),HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private List<VendorListing> createTestDataForNewVehicleListingMissingPrice(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setMake(MERCEDEZ);
        vendorListing.setModel("X1");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

}
