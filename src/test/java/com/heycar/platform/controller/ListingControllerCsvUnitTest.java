package com.heycar.platform.controller;

import com.heycar.platform.document.ListingDocument;
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
import static com.heycar.platform.constants.VehicleListingConstants.MEDIA_TYP_TXT_CSV;
import static org.mockito.Mockito.when;
import static com.heycar.platform.utils.ITestUtils.readFileAsString;
import static com.heycar.platform.constants.ListingTestConstants.CSV_PREFIX;
import static com.heycar.platform.constants.ListingTestConstants.RESP_JSON_PREFIX;
import static com.heycar.platform.constants.ListingTestConstants.CODE_MISSING_IN_CSV_FILE_NM;

@RunWith(SpringRunner.class)
@WebMvcTest(ListingController.class)
@TestPropertySource(locations = "classpath:application-test-csv.properties")
public class ListingControllerCsvUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IListingSvc listingSvc;

    @Value("${application.test.postUrlCsv}")
    private String uploadListingCsvUrl;

    @Value("${application.test.postUrlCsvPositive}")
    private String uploadListingCsvUrlPositiveUrl;

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenCodeAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvCodeMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(CODE_MISSING_IN_CSV_FILE_NM)),
                response.getContentAsString(), JSONCompareMode.LENIENT);
    }

    private List<ListingDocument> getListingDocumentsCsv() {
        List<ListingDocument> listingDocLst = new ArrayList<>();
        ListingDocument listingDoc = new ListingDocument();
        listingDoc.setModel("Hyundai");
        listingDoc.setMake("Verna");
        listingDoc.setkW("1200");
        listingDoc.setCode("va");
        listingDoc.setPrice("123");
        listingDoc.setYear("1989");
        listingDocLst.add(listingDoc);
        return listingDocLst;
    }

    private RequestBuilder getRequestBuilderForPostListingCsvCodeMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingCodeMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenPowerAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvPowerMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat("JsonRespWhenPowerMissingInCsvReq.json")),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPowerMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingPowerMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }


    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenYearAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvYearMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat("JsonRespWhenYearMissingInCsvReq.json")),
                response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvYearMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingYearMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenColorAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvColorMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        String respContent = response.getContentAsString();
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat("JsonRespWhenColorMissingInCsvReq.json")),response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvColorMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingColorMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenMakeModelAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvMakeModelMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat("JsonRespWhenMakeModelMissingInCsv.json")),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvMakeModelMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingMakeModelMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenPriceAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvPriceMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat("JsonRespWhenPriceMissingInCsvReq.json")),response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPriceMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat("CsvListingPriceMissing.csv")))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Returns201Created() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsv();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingCsv() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrlPositiveUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat
                        ("CsvListingPostPositiveVehLstngData.csv"))).contentType(MEDIA_TYP_TXT_CSV);
    }

}
