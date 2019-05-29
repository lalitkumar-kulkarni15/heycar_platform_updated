package com.heycar.platform.controller;

import com.heycar.platform.HeycarPlatformApplication;
import com.heycar.platform.converter.CsvHttpMessageConverter;
import com.heycar.platform.model.ListingList;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.utils.ITestUtils;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {HeycarPlatformApplication.class})
@TestPropertySource(locations = "classpath:application-test-csv.properties")
public class ListingControllerIntCsvTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private MockMvc mockMvc;

    @Value("${server.port}")
    private String port;

    @Value("${application.test.host}")
    private String host;

    @Value("${application.test.postUrlCsv}")
    private String postUrlCsv;

    @Value("${application.test.searchAllUrlCsv}")
    private String searchAllUrl;

    @Value("${application.test.searchByParamUrlCsv}")
    private String searchByParam;

    private HttpHeaders httpHeaders;

    /**
     * <p>This test case invokes the search all api and verifies :- </p>
     * <ul>
     *  <li>The HTTP Status code is 200 OK</li>
     *  <li>The response received is not empty</li>
     * </ul>
     *
     * @since 26-05-2019
     */
    @Test
    public void searchAllTest_Returns200OK_WithValidDataInserted(){

        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpEntity<VendorListing> searchListingReq = new HttpEntity<VendorListing>(null,getHttpHeaderJson());
        ResponseEntity<String> responseListings = restTemplate.exchange(ITestUtils.createURLWithPort
                                                  (searchAllUrl,host,port),
                                                  HttpMethod.GET,searchListingReq,String.class);

        Assert.assertNotNull(responseListings.getBody());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
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
    public void searchTest_Returns200OK_WhenSearchedByColor(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort(searchByParam,
                                       host,port)).queryParam("color", "Blue");


        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        Assert.assertNotNull(responseListings.getBody());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
    }

    @Test
    public void searchTest_Returns200OK_WhenSearchedByColorMakeModelYear(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort("/heycar/searchByParam",
                host,port)).queryParam("color", "White")
                           .queryParam("make","Maruti")
                           .queryParam("model","Swift")
                           .queryParam("year","1987");


        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        // Verify basic assertions
        Assert.assertNotNull(responseListings.getBody());
        Assert.assertFalse(responseListings.getBody().isEmpty());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());

    }

    @Test
    public void uploadListingCsvTest_Returns201Created_ForPositiveCsvListing() {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<ListingList> listingDocEnt = new HttpEntity<ListingList>(createTestDataForNewVehicleListingPositiveCsv(),
                getHttpHeaderCsv());

        List<HttpMessageConverter<?>> csvMessgeonverter = new ArrayList<>();
        csvMessgeonverter.add(new CsvHttpMessageConverter<>());
        restTemplate.getRestTemplate().setMessageConverters(csvMessgeonverter);
        ResponseEntity response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlCsv,
                host,port ), HttpMethod.POST,listingDocEnt, String.class);

        // Check if the response is not null and the http status code is - 201 Created.
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());

    }

    private HttpHeaders getHttpHeaderCsv(){

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("text/csv"));
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private ListingList createTestDataForNewVehicleListingPositiveCsv(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setYear("1987");
        vendorListing.setCode("adp");
        vendorListing.setColor("White");
        vendorListing.setkW("2000");
        vendorListing.setMakeModel("Maruti/Swift");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        ListingList listingList = new ListingList();
        listingList.setList(listingDocList);
        return listingList;
    }

    //@Test(expected = HttpMessageNotWritableException.class)
    @Test
    public void uploadListingCsvTest_ReturnsBadReq_WhenCodeMissing() throws HttpMessageNotWritableException {

        // Step 1 : Create the Http entity object which contains the request body and headers.
        HttpEntity<ListingList> listingDocEnt = new HttpEntity<ListingList>(createTestDataForNewVehicleListingCodeMissing(),
                getHttpHeaderCsv());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));

        TestRestTemplate restTemplate = getRestTemplateWithConvrtrs(converter);
        ResponseEntity<?> response = null;
        invokeApiAndAssertResp(listingDocEnt, restTemplate);
    }

    private void invokeApiAndAssertResp(HttpEntity<ListingList> listingDocEnt, TestRestTemplate restTemplate) {
        ResponseEntity<?> response;
        try {
             response = restTemplate.exchange(ITestUtils.createURLWithPort(postUrlCsv,
                    host, port), HttpMethod.POST, listingDocEnt, String.class);
        } catch(HttpMessageNotWritableException ex){
            assertException(ex);
        }
    }

    private TestRestTemplate getRestTemplateWithConvrtrs(MappingJackson2HttpMessageConverter converter) {
        List<HttpMessageConverter<?>> csvMessgeonverter = new ArrayList<>();
        csvMessgeonverter.add(new CsvHttpMessageConverter<>());
        csvMessgeonverter.add(converter);
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.getRestTemplate().setMessageConverters(csvMessgeonverter);
        return restTemplate;
    }

    private void assertException(HttpMessageNotWritableException ex) {
        if(ex.getCause() instanceof CsvRequiredFieldEmptyException){
            CsvRequiredFieldEmptyException csvExp = (CsvRequiredFieldEmptyException) ex.getCause();
            String msg = csvExp.getMessage();
            Assert.assertEquals("Field 'code' is mandatory but no value was provided.",msg);
        } else {
            Assert.fail();
        }
    }

    private ListingList createTestDataForNewVehicleListingCodeMissing(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setYear("1987");
        vendorListing.setColor("White");
        vendorListing.setkW("2000");
        vendorListing.setMakeModel("Maruti/Swift");
        vendorListing.setPrice("269 $");
        List<VendorListing> listingDocList = new ArrayList<>();
        listingDocList.add(vendorListing);
        ListingList listingList = new ListingList();
        listingList.setList(listingDocList);
        return listingList;
    }

}
