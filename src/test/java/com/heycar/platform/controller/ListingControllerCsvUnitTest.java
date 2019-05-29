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
import java.util.ArrayList;
import java.util.List;
import static com.heycar.platform.constants.IVehicleListingConstants.MEDIA_TYP_TXT_CSV;
import static org.mockito.Mockito.when;

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
        JSONAssert.assertEquals("{\"message\":\"Field 'code' is mandatory but no value was provided.  at line" +
                " number 1 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);
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
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        ",Maruti/Suzuki,180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,blue,16990")
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
        JSONAssert.assertEquals("{\"message\":\"Field 'kW' is mandatory but no value was provided.  at line" +
                        " number 2 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPowerMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,Maruti/Suzuki,180,123,2014,black,15950\n" +
                        "2,audi/a3,,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,blue,16990")
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
        JSONAssert.assertEquals("{\"message\":\"Field 'year' is mandatory but no value was provided.  at line" +
                        " number 4 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvYearMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,Maruti/Suzuki,180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,,blue,16990")
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
        JSONAssert.assertEquals("{\"message\":\"Field 'color' is mandatory but no value was provided.  at line" +
                        " number 4 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvColorMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,Maruti/Suzuki,180,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,,16990")
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
        JSONAssert.assertEquals("{\"message\":\"Field 'makeModel' is mandatory but no value was provided.  at line" +
                        " number 1 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);

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
    public void uploadListingCsvTest_Ret400BadReqWhenPriceAbsent() throws Exception {

        List<ListingDocument> listingDocLst = getListingDocumentsCsv();
        when(listingSvc.addListingInDataStore(Mockito.anyString(),Mockito.any())).thenReturn(listingDocLst);
        RequestBuilder requestBuilder = getRequestBuilderForPostListingCsvPriceMissing();
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JSONAssert.assertEquals("{\"message\":\"Field 'price' is mandatory but no value was provided.  at line" +
                        " number 4 in the csv file.\",\"details\":\"uri=/heycar/upload_csv/1190\"}",response.getContentAsString(),
                JSONCompareMode.LENIENT);

    }

    private RequestBuilder getRequestBuilderForPostListingCsvPriceMissing() {
        return MockMvcRequestBuilders
                .post(uploadListingCsvUrl)
                .accept(MediaType.APPLICATION_JSON).content("code,make/model,power-in-ps,year,color,price\n" +
                        "1,Merc/benz,180,123,2014,black,15950\n" +
                        "2,audi/a3,111,2016,white,17210\n" +
                        "3,vw/golf,86,2018,green,14980\n" +
                        "4,skoda/octavia,86,2018,blue,")
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
                .contentType(MEDIA_TYP_TXT_CSV);
    }

}
