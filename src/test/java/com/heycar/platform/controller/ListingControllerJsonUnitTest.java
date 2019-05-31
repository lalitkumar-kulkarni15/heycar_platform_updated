package com.heycar.platform.controller;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.service.IListingSvc;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(ListingController.class)
@TestPropertySource(locations = "classpath:application-test-json.properties")
public class ListingControllerJsonUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IListingSvc listingSvc;

    @Value("${application.test.postUrlJson}")
    private String vehListingUrl;

    @Value("${application.test.searchByParamUrl}")
    private String srchByParamUrl;

    @Value("${application.test.searchAllListingsUrl}")
    private String srchAllParamUrl;

    @Test
    public void searchListingByParamsTest_Returns200OkWithListingData() throws Exception {

        Mockito.when(listingSvc.searchListing(Mockito.any())).thenReturn(getVendorListingLst());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                srchByParamUrl+"color=Blue").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals("[{\"code\":null,\"year\":\"1989\",\"color\":\"Blue\",\"price\":\"200\"," +
                "\"make\":\"X1\",\"model\":\"Mercedes\",\"kW\":\"4000\"}]",response.getContentAsString(),
                JSONCompareMode.LENIENT);
    }

    @Test
    public void searchAllListingsTest_Returns200OkWithListingData() throws Exception {

        Mockito.when(listingSvc.findAllListing()).thenReturn(getVendorListingLst());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                srchAllParamUrl).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals("[{\"code\":null,\"year\":\"1989\",\"color\":\"Blue\",\"price\":\"200\",\"make\"" +
                ":\"X1\",\"model\":\"Mercedes\",\"kW\":\"4000\"}]",response.getContentAsString(), JSONCompareMode.LENIENT);
    }

    private List<VendorListing> getVendorListingLst(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setYear("1989");
        vendorListing.setModel("Mercedes");
        vendorListing.setMake("X1");
        vendorListing.setColor("Blue");
        vendorListing.setPrice("200");
        vendorListing.setkW("4000");
        List<VendorListing> vendorListingLst = new ArrayList<>();
        vendorListingLst.add(vendorListing);
        return vendorListingLst;
    }

    @Test
    public void uploadListingJsonTest_Returns201Created() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJson();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingJson() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

    private List<ListingDocument> getListingDocuments() {
        List<ListingDocument> listingDocLst = new ArrayList<>();
        ListingDocument listingDoc = new ListingDocument();
        listingDoc.setModel("Mercedez");
        listingDoc.setMake("Benz");
        listingDoc.setkW("1200");
        listingDoc.setCode("va");
        listingDoc.setPrice("123");
        listingDoc.setYear("1989");
        listingDocLst.add(listingDoc);
        return listingDocLst;
    }

    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenColorAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonColorMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Color cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonColorMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenCodeAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonCodeMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Code cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonCodeMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenMakeAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonMakeMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Make cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingJsonMakeMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenModelAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonModelMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Model cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingJsonModelMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenPowerAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonPowerMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Power cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonPowerMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }


    @Test
    public void uploadListingJsonTest_Ret400BadReqWhenYearAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocuments();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonYearMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Year cannot be null - \",\"details\":\"uri=/heycar/vehicle_listings/16\"}"
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonYearMissing() {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);
    }

}
