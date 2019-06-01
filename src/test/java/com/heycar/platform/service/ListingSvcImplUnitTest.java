package com.heycar.platform.service;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.exception.ListingProcessingException;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.repository.ListingRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListingSvcImplUnitTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ElasticsearchOperations operations;

    @InjectMocks
    private ListingSvcImpl listingSvcImpl;

    @Test
    public void searchListingTest_Returns200OkWithValidData() throws ListingProcessingException {

        when(listingRepository.findByMakeAndModelAndYearAndColor(Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(popListingDocList());

        List<VendorListing> vendorListingList = this.listingSvcImpl.searchListing(popVendorListing());
        Assert.assertNotNull(vendorListingList);
    }

    private VendorListing popVendorListing(){

        VendorListing vendorListing = new VendorListing();
        vendorListing.setMake("Mercedez");
        vendorListing.setModel("Benz");
        vendorListing.setYear("1989");
        vendorListing.setColor("Blue");
        vendorListing.setCode("13f");
        vendorListing.setkW("kw");
        return vendorListing;
    }

    private List<ListingDocument> popListingDocListForVerifyingSave(){

        ListingDocument listingDoc = new ListingDocument();
        listingDoc.setYear("1989");
        listingDoc.setCode("13f");
        listingDoc.setkW("kw");
        listingDoc.setMake("Mercedez");
        listingDoc.setModel("Benz");
        listingDoc.setColor("Blue");
        List<ListingDocument> listingDocList = new ArrayList<>();
        listingDocList.add(listingDoc);
        return listingDocList;
    }

    private List<ListingDocument> popListingDocList(){

        ListingDocument listingDoc = new ListingDocument();
        listingDoc.setYear("1989");
        listingDoc.setPrice("231");
        listingDoc.setCode("ba");
        listingDoc.setkW("234");
        listingDoc.setMake("Mercedez");
        listingDoc.setModel("Benz");
        List<ListingDocument> listingDocList = new ArrayList<>();
        listingDocList.add(listingDoc);
        return listingDocList;
    }

    @Test
    public void findAllListingTest_Returns200OkWithValidData() throws ListingProcessingException {

        when(listingRepository.findAll()).thenReturn(popListingDocList());
        List<VendorListing> vendorListingList = this.listingSvcImpl.findAllListing();
        Assert.assertNotNull(vendorListingList);
    }

    @Test(expected = ListingProcessingException.class)
    public void addListingInDataStoreTest_ThrowsExceptionWhenPutMapping() throws ListingProcessingException {

        when(operations.putMapping(Mockito.any())).thenThrow(new RuntimeException("Failed to register put mapping."));
        listingSvcImpl.addListingInDataStore("15", Arrays.asList(popVendorListing()));
    }

    @Test(expected = ListingProcessingException.class)
    public void addListingInDataStoreTest_ThrowsExceptionSave() throws ListingProcessingException {

        when(listingRepository.save(Collections.singleton(Mockito.anyObject()))).thenThrow(new RuntimeException("Failed to save data to repository."));
        listingSvcImpl.addListingInDataStore("15", Arrays.asList(popVendorListing()));
    }

    @Test(expected = ListingProcessingException.class)
    public void findAllListingTest_ThrowsExceptionOnFindAll() throws ListingProcessingException {

        when(listingRepository.findAll()).thenThrow(new RuntimeException("Failed to find the data from the repository."));
        listingSvcImpl.findAllListing();
    }

    @Test
    public void addListingInDataStoreTest_VerifiesPutMapping() throws ListingProcessingException {

        listingSvcImpl.addListingInDataStore("15", Arrays.asList(popVendorListing()));
        verify(operations,Mockito.times(1)).putMapping(ListingDocument.class);
    }

    @Test
    public void addListingInDataStoreTest_VerifiesSave() throws ListingProcessingException {

        listingSvcImpl.addListingInDataStore("15", Arrays.asList(popVendorListing()));
        ArgumentCaptor<List> arg = ArgumentCaptor.forClass(List.class);
        verify(listingRepository,Mockito.times(1)).save(arg.capture());
    }
}
