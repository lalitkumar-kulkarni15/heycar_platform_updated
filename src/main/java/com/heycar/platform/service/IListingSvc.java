package com.heycar.platform.service;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.exception.ListingProcessingException;
import com.heycar.platform.model.VendorListing;
import java.util.List;

public interface IListingSvc {

    public List<ListingDocument> addListingInDataStore(final String dealerId, final List<VendorListing> listing) throws ListingProcessingException;

    public List<VendorListing> searchListing(VendorListing listingSrchPrms) throws ListingProcessingException;

    public List<VendorListing> findAllListing() throws ListingProcessingException;
}
