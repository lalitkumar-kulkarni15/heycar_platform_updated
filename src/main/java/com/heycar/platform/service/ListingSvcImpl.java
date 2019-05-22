package com.heycar.platform.service;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.model.ListingList;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.repository.ListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @since 19-May-2019
 * @author Lalit Kulkarni
 * @version 1.0
 */
@Service
public class ListingSvcImpl implements IListingSvc {

    private final Logger logger = LoggerFactory.getLogger(ListingSvcImpl.class);

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ListingRepository listingRepository;

    /*@Transactional
    public String addListing(final String dealerId,final VendorListing listing){

        final ListingDocument listingDocument = mapListingDocument(dealerId, listing);
        operations.putMapping(ListingDocument.class);
        listingRepository.save(listingDocument);
        return listingDocument.getDealerIdCode();
    }

    private ListingDocument mapListingDocument(final String dealerId,final VendorListing listing) {

        ListingDocument listingDocument = new ListingDocument(dealerId);
        BeanUtils.copyProperties(listing,listingDocument);
        listingDocument.setDealerIdCode(dealerId.concat(listingDocument.getCode()));
        return listingDocument;
    }*/

    @Transactional
    public List<ListingDocument> addListing(final String dealerId, final List<VendorListing> listing){

        final List<ListingDocument> listingDocument = mapListingDocument(dealerId, listing);
        operations.putMapping(ListingDocument.class);
        listingRepository.save(listingDocument);
        return listingDocument;
    }

    private  List<ListingDocument> mapListingDocument(final String dealerId,final List<VendorListing> listing) {

        List<ListingDocument> docListing = listing.stream().map(listng -> new ListingDocument(dealerId.concat(listng.getCode())
                                                                            ,dealerId,listng.getCode(),
                                                                            listng.getMake(),
                                                                            listng.getModel(),
                                                                            listng.getkW(),
                                                                            listng.getYear(),
                                                                            listng.getColor(),
                                                                            listng.getPrice())).collect(Collectors.toList());

       /* ListingDocument listingDocument = new ListingDocument(dealerId);
        BeanUtils.copyProperties(listing,listingDocument);
        listingDocument.setDealerIdCode(dealerId.concat(listingDocument.getCode()));*/
        return docListing;
    }


    @Transactional(readOnly = true)
    public List<ListingDocument> searchListing(VendorListing listingSrchPrms){

        List<ListingDocument> listingDoc = this.listingRepository.findByMakeAndModelAndYearAndColor(listingSrchPrms.getMake()
                                            ,listingSrchPrms.getModel(),
                                            listingSrchPrms.getYear(),
                                            listingSrchPrms.getColor());
        return listingDoc;
    }

    @Transactional(readOnly = true)
    public List<ListingDocument> findAllListing(){

       Iterable<ListingDocument> listingDoc = this.listingRepository.findAll();
       List<ListingDocument> list = new ArrayList<>();
        listingDoc.forEach(list::add);
        return list;
    }
}
