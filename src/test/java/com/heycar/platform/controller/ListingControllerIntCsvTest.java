package com.heycar.platform.controller;

import com.heycar.platform.HeycarPlatformApplication;
import com.heycar.platform.converter.CsvHttpMessageConverter;
import com.heycar.platform.model.ListingList;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.heycar.platform.constants.VehicleListingConstants.HEADER_ACCEPT;
import static com.heycar.platform.utils.ITestUtils.readFileAsString;
import static com.heycar.platform.constants.ListingTestConstants.RESP_JSON_PREFIX;
import static com.heycar.platform.constants.ListingTestConstants.SRCH_BY_COLOR_200RESP_INT_TST;
import static com.heycar.platform.constants.ListingTestConstants.SRCH_BY_COLOR_MK_MODEL_YR_RESP_INT_TST;
import static com.heycar.platform.constants.ListingTestConstants.SRCH_BY_COLOR_MK_YR_RESP_INT_TST;
import static com.heycar.platform.constants.ListingTestConstants.SRCH_BY_YR_RESP_INT_TST;
import static com.heycar.platform.constants.ListingTestConstants.SRCH_BY_PARAM_URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {HeycarPlatformApplication.class})
@TestPropertySource(locations = "classpath:application-test-csv.properties")
public class ListingControllerIntCsvTest {

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

    public static final String COLOR = "color";

    public static final String WHITE = "white";

    @Test
    public void uploadListingCsvTestReturns201CreatedForPositiveCsvListing() {

        ResponseEntity response = uploadCsvAndGetResponseEntity();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());

    }

    private ResponseEntity uploadCsvAndGetResponseEntity() {

        HttpEntity<ListingList> listingDocEnt = new HttpEntity<>(createTestDataForNewVehicleListingPositiveCsv(),
                getHttpHeaderCsv());

        TestRestTemplate restTemplate = new TestRestTemplate();
        List<HttpMessageConverter<?>> csvMessgeConverter = new ArrayList<>();
        csvMessgeConverter.add(new CsvHttpMessageConverter<>());
        restTemplate.getRestTemplate().setMessageConverters(csvMessgeConverter);
        return restTemplate.exchange(ITestUtils.createURLWithPort(postUrlCsv,
                host,port ), HttpMethod.POST,listingDocEnt, String.class);
    }

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
    public void searchAllTestReturns200OKWithValidDataInserted(){

        // Create test data.
        uploadCsvAndGetResponseEntity();

        TestRestTemplate restTemplate = new TestRestTemplate();

        HttpEntity<VendorListing> searchListingReq = new HttpEntity<>(null,getHttpHeaderJson());
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
    public void searchTestReturns200OKWhenSearchedByColor() throws JSONException, IOException {

        // Create test data.
        uploadCsvAndGetResponseEntity();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort(searchByParam,
                                       host,port)).queryParam(COLOR, WHITE);


        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        Assert.assertNotNull(responseListings.getBody());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(SRCH_BY_COLOR_200RESP_INT_TST)),responseListings.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void searchTestReturns200OKWhenSearchedByColorMakeModelYear() throws JSONException, IOException {

        // Create test data.
        uploadCsvAndGetResponseEntity();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort(SRCH_BY_PARAM_URL,
                host,port)).queryParam(COLOR, WHITE)
                           .queryParam("make","Maruti")
                           .queryParam("model","Swift")
                           .queryParam("year","1987");

        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        // Verify basic assertions
        Assert.assertNotNull(responseListings.getBody());
        Assert.assertFalse(responseListings.getBody().isEmpty());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(SRCH_BY_COLOR_MK_MODEL_YR_RESP_INT_TST)),responseListings.getBody(),JSONCompareMode.LENIENT);

    }

    @Test
    public void searchTestReturns200OKWhenSearchedByColorMakeYear() throws JSONException, IOException {

        // Create test data.
        uploadCsvAndGetResponseEntity();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort("/heycar/searchByParam",
                host,port)).queryParam(COLOR, WHITE)
                .queryParam("make","Maruti")
                .queryParam("year","1987");

        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        // Verify basic assertions
        Assert.assertNotNull(responseListings.getBody());
        Assert.assertFalse(responseListings.getBody().isEmpty());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(SRCH_BY_COLOR_MK_YR_RESP_INT_TST)),responseListings.getBody(),JSONCompareMode.LENIENT);

    }

    @Test
    public void searchTestReturns200OKWhenSearchedByYear() throws JSONException, IOException {

        // Create test data.
        uploadCsvAndGetResponseEntity();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        TestRestTemplate restTemplate = new TestRestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITestUtils.createURLWithPort("/heycar/searchByParam",
                host,port)).queryParam("year","1987");

        ResponseEntity<String> responseListings = restTemplate.exchange
                (builder.toUriString(),HttpMethod.GET,entity,String.class);

        // Verify basic assertions
        Assert.assertNotNull(responseListings.getBody());
        Assert.assertFalse(responseListings.getBody().isEmpty());
        Assert.assertEquals(HttpStatus.OK,responseListings.getStatusCode());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(SRCH_BY_YR_RESP_INT_TST)),responseListings.getBody(),JSONCompareMode.LENIENT);

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

}
