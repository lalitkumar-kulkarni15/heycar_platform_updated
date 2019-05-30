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

    private HttpHeaders httpHeaders;

    /**
     * <p>This test case invokes the upload listing APi by passing the vehicle listing in JSON format.
     *   It verifies if the response received has the http status code - 201 Created {@link HttpStatus}</p>
     *
     * @since 25-05-2019
     * @see   {@link ListingController}
     */
    @Test
    public void uploadListingTest_Returns201Created_ForSuccessfullUpload(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingPositive(),
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
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }


    private HttpHeaders getHttpHeaderJson(){

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediatypeList = new ArrayList<>();
        mediatypeList.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(mediatypeList);
        return httpHeaders;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingCode(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingCode(),
                                                        getHttpHeaderJson());

        /**
         * Step 2 : Pass the http entity created in #step 1 along with the resource url to rest template to hit the
         *          service and get the response back which contains the http status code and the uri of the resources
         *          created.
         */

        //List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //Add the Jackson Message converter
        //MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        //converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));

        //MappingJackson2HttpMessageConverter converterJSon = new MappingJackson2HttpMessageConverter();
        //converterJSon.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        //List<HttpMessageConverter<?>> csvMessgeonverter = new ArrayList<>();
        //csvMessgeonverter.add(converter);
        //csvMessgeonverter.add(converterJSon);

        //restTemplate.getRestTemplate().setMessageConverters(csvMessgeonverter);

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
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingMake(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingMake(),
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
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingModel(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingModel(),
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
        vendorListing.setMake("Mercedes");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingPower(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingPower(),
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
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingYear(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingYear(),
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
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setkW("2000");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingColor(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingColor(),
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
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setkW("2000");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }

    @Test
    public void uploadListingTest_Returns400BadRequest_ForMissingPrice(){

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<List<VendorListing>> listingDocEnt = new HttpEntity<List<VendorListing>>(createTestDataForNewVehicleListingMissingPrice(),
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

    private List<VendorListing> createTestDataForNewVehicleListingMissingPrice(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setCode("ba");
        vendorListing.setYear("1989");
        vendorListing.setMake("Mercedes");
        vendorListing.setModel("X1");
        vendorListing.setColor("Blue");
        vendorListing.setkW("2000");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        return listingDocList;
    }






}
