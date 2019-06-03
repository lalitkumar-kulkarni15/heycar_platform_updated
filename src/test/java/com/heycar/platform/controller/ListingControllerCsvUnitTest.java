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
import static com.heycar.platform.constants.ListingTestConstants.CODE_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.CODE_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.POWER_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.POWER_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.YEAR_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.YEAR_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.COLOR_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.COLOR_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.MODEL_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.MAKE_MODEL_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.PRICE_MISSING_IN_CSV_RESP_FILE_NM;
import static com.heycar.platform.constants.ListingTestConstants.PRICE_MODEL_MISSING_IN_CSV_FILE_REQ_NM;
import static com.heycar.platform.constants.ListingTestConstants.POSITIVE_CSV_FILE_REQ_NM;

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
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(CODE_MISSING_IN_CSV_RESP_FILE_NM)),
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
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(CODE_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenPowerAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvPowerMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(POWER_MISSING_IN_CSV_RESP_FILE_NM)),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPowerMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(POWER_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }


    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenYearAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvYearMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(YEAR_MISSING_IN_CSV_RESP_FILE_NM)),
                response.getContentAsString(),JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvYearMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(YEAR_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenColorAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvColorMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(COLOR_MISSING_IN_CSV_RESP_FILE_NM)),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvColorMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(COLOR_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenMakeModelAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvMakeModelMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(MODEL_MISSING_IN_CSV_RESP_FILE_NM)),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvMakeModelMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(MAKE_MODEL_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenPriceAbsent() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvPriceMissing();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals(readFileAsString(RESP_JSON_PREFIX.concat(PRICE_MISSING_IN_CSV_RESP_FILE_NM)),
                response.getContentAsString(), JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPriceMissing() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat(PRICE_MODEL_MISSING_IN_CSV_FILE_REQ_NM)))
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Returns201Created() throws Exception {

        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(getListingDocumentsCsv());
        final RequestBuilder requestBuilder = getRequestBuilderForPostListingCsv();
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingCsv() throws IOException {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrlPositiveUrl)
                .accept(MediaType.APPLICATION_JSON).content(readFileAsString(CSV_PREFIX.concat
                        (POSITIVE_CSV_FILE_REQ_NM))).contentType(MEDIA_TYP_TXT_CSV);
    }

}
