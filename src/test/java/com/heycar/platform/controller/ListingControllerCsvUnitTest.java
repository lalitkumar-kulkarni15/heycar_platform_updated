package com.heycar.platform.controller;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.service.IListingSvc;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import static com.heycar.platform.utils.ITestConstants.MEDIA_TYP_TXT_CSV;

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

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenCodeAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvCodeMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

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

    private RequestBuilder getRequestBuilderForPostListingCsvCodeMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenMakeAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvMakeMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingCsvMakeMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
                .contentType(MEDIA_TYP_TXT_CSV);
    }

    @Test
    public void uploadListingCsvTest_Ret400BadReqWhenModelAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvModelMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    private RequestBuilder getRequestBuilderForPostListingCsvModelMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
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

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPowerMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
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

    }

    private RequestBuilder getRequestBuilderForPostListingCsvYearMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"color\": \"Green\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
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

    }

    private RequestBuilder getRequestBuilderForPostListingCsvColorMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("[{\n" +
                        "\"code\": \"TwentyOneCode\",\n" +
                        "\"make\": \"Honda\",\n" +
                        "\"model\": \"City\",\n" +
                        "\"kW\": \"132\",\n" +
                        "\"year\": \"2022\",\n" +
                        "\"price\": \"3443\"\n" +
                        "}\n" +
                        "]")
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

    }

    private RequestBuilder getRequestBuilderForPostListingCsvMakeModelMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,,180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,blue,16990")
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

    private RequestBuilder getRequestBuilderForPostListingCsv() {
        return MockMvcRequestBuilders
                .post("/heycar/upload_csv/15")
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,Maruti/Suzuki,180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,blue,16990")
                .contentType("text/csv");
    }

}
