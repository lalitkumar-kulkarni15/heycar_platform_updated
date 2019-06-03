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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static com.heycar.platform.constants.ListingTestConstants.JSON_PREFIX;
import static com.heycar.platform.constants.ListingTestConstants.RESP_JSON_PREFIX;
import static com.heycar.platform.utils.ITestUtils.readFileAsString;
import static com.heycar.platform.constants.ListingTestConstants.YEAR_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.YEAR_MISSING_IN_JSON_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.POWER_MISSING_IN_JSON_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.POWER_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.MODEL_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.MAKE_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.CODE_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.COLOR_MISSING_IN_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.POSITIVE_JSON_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.POSITIVE_JSON_RESP_FILE_NM_FR_SRCH_BY_PARAMS;
import static com.heycar.platform.constants.ListingTestConstants.POSITIVE_JSON_RESP_FILE_NM_FR_SRCH_ALL;
import static com.heycar.platform.constants.ListingTestConstants.COLOR_MISSING_IN_JSON_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.CODE_MISSING_IN_JSON_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.MAKE_MISSING_IN_JSON_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.MODEL_MISSING_IN_JSON_RESP_FILE_NM;

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
    public void searchListingByParamsTestReturns200OkWithListingData() throws Exception {

        when(listingSvc.searchListing(Mockito.any())).thenReturn(getVendorListingLst());
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(srchByParamUrl+"color=Blue").accept(
                MediaType.APPLICATION_JSON);

        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(POSITIVE_JSON_RESP_FILE_NM_FR_SRCH_BY_PARAMS)),
                                                response.getContentAsString(),
                                                JSONCompareMode.LENIENT);
    }

    @Test
    public void searchAllListingsTestReturns200OkWithListingData() throws Exception {

        when(listingSvc.findAllListing()).thenReturn(getVendorListingLst());

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(srchAllParamUrl).accept(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(POSITIVE_JSON_RESP_FILE_NM_FR_SRCH_ALL)),
                response.getContentAsString(), JSONCompareMode.LENIENT);
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
    public void uploadListingJsonTestReturns201Created() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJson();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingJson() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(POSITIVE_JSON_FILE_REQ_NM)))
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
    public void uploadListingJsonTestRet400BadReqWhenColorAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonColorMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(COLOR_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonColorMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(COLOR_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTestRet400BadReqWhenCodeAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonCodeMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(CODE_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonCodeMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(CODE_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTestRet400BadReqWhenMakeAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonMakeMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(MAKE_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingJsonMakeMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(MAKE_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTestRet400BadReqWhenModelAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonModelMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(MODEL_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingJsonModelMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(MODEL_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadListingJsonTestRet400BadReqWhenPowerAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonPowerMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(POWER_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonPowerMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(POWER_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }


    @Test
    public void uploadListingJsonTestRet400BadReqWhenYearAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocuments());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingJsonYearMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(YEAR_MISSING_IN_JSON_RESP_FILE_NM))
                ,response.getContentAsString(),JSONCompareMode.LENIENT);
    }

    private RequestBuilder getRequestBuilderForPostListingJsonYearMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(vehListingUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(JSON_PREFIX.concat(YEAR_MISSING_IN_JSON_FILE_REQ_NM)))
                .contentType(MediaType.APPLICATION_JSON);
    }

}
