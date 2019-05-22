package com.heycar.platform.service;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.model.ListingList;
import com.heycar.platform.model.VendorListing;
import java.util.List;

public interface IListingSvc {

    public List<ListingDocument> addListing(final String dealerId, final List<VendorListing> listing);

    public List<ListingDocument> searchListing(VendorListing listingSrchPrms);

    public List<ListingDocument> findAllListing();
}
