package com.heycar.platform.service;

import com.heycar.platform.document.ListingDocument;
import com.heycar.platform.exception.ListingProcessingException;
import com.heycar.platform.model.VendorListing;
import com.heycar.platform.repository.ListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>This is the service layer class of vehicle listings. It mainly contains the service level business logic
 * code which interacts with the repository layer to interact with the backend elastic search.
 * </p>
 *
 * @since   19-May-2019
 * @author  Lalit Kulkarni
 * @version 1.0
 */
@Service
public class ListingSvcImpl implements IListingSvc {

    private final Logger logger = LoggerFactory.getLogger(ListingSvcImpl.class);

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ListingRepository listingRepository;

    // Exception messages.
    private static final String INSERTION_FAILED_MSG = "Failed while inserting the vendor listing in the data store.";
    private static final String SEARCHING_FAILED_MSG = "Failed while searching the vendor listing from the data store.";

    /**
     * <p>
     *  This method adds the vehicle listing in the elastic search repository.
     * </p>
     * @param dealerId                    This is the dealer id of dealer of the vehicle.
     * @param listing                     This is the actual vehicle listing.
     * @return                            {@link List<ListingDocument>}
     * @throws ListingProcessingException
     */
    @Override
    @Transactional
    public List<ListingDocument> addListingInDataStore(final String dealerId, final List<VendorListing> listing)
            throws ListingProcessingException {

        logger.info("Entering addListingInDataStore()");

        try{

            final List<ListingDocument> listingDocument = mapListingDocumentRequest(dealerId, listing);
            operations.putMapping(ListingDocument.class);
            listingRepository.save(listingDocument);
            return listingDocument;
        } catch(Exception exception){
            throw new ListingProcessingException(INSERTION_FAILED_MSG,exception);
        }

    }

    private List<ListingDocument> mapListingDocumentRequest(final String dealerId, final List<VendorListing> listing) {

        return listing.stream().map(listng -> ListingDocument.builder()
                                              .dealerIdCode(dealerId.concat(listng.getCode()))
                                              .dealerId(dealerId)
                                              .code(listng.getCode())
                                              .make(listng.getMake())
                                              .model(listng.getModel())
                                              .kW(listng.getkW())
                                              .year(listng.getYear())
                                              .color(listng.getColor())
                                              .price(listng.getPrice())
                                              .build()).collect(Collectors.toList());

    }

    /**
     * <p>
     *  This method searches the vehicle listing from the vehicle listing elastic search repository
     *  on the basis of the search parameters :-
     *  <ul>
     *   <li>Make</li>
     *   <li>Model</li>
     *   <li>Color</li>
     *   <li>Year</li>
     *  </ul>
     * </p>
     * @param listingSrchPrms             {@link VendorListing} This object contains the input search parameters.
     * @return                            {@link List<VendorListing>}
     * @throws ListingProcessingException
     */
    @Override
    @Transactional(readOnly = true)
    public List<VendorListing> searchListing(VendorListing listingSrchPrms) throws ListingProcessingException {

        try {

            List<ListingDocument> listingDoc = this.listingRepository.findByMakeAndModelAndYearAndColor(listingSrchPrms.getMake()
                    , listingSrchPrms.getModel(),
                    listingSrchPrms.getYear(),
                    listingSrchPrms.getColor());

            return getVendorListingList(listingDoc);

        } catch(Exception exception){
            throw new ListingProcessingException(SEARCHING_FAILED_MSG,exception);
        }

    }

    private List<VendorListing> getVendorListingList(List<ListingDocument> listingDoc) {

        return listingDoc.stream().map(vendorListing -> VendorListing.builder()
                                                      .code(vendorListing.getCode())
                                                      .year(vendorListing.getYear())
                                                      .color(vendorListing.getColor())
                                                      .price(vendorListing.getPrice())
                                                      .make(vendorListing.getMake())
                                                      .model(vendorListing.getModel())
                                                      .kW(vendorListing.getkW())
                                                      .build()).collect(Collectors.toList());

    }

    /**
     * <p>
     * This method searches all the vehicle listings from the elastic search repository.
     * </p>
     *
     * @return {@link List<VendorListing>}
     * @throws ListingProcessingException
     */
    @Override
    @Transactional(readOnly = true)
    public List<VendorListing> findAllListing() throws ListingProcessingException {

        try{
            Iterable<ListingDocument> listingDoc = this.listingRepository.findAll();
            List<ListingDocument> list = new ArrayList<>();
            listingDoc.forEach(list::add);
            return getVendorListingList(list);

        } catch(Exception exception){
            throw new ListingProcessingException(SEARCHING_FAILED_MSG,exception);
        }

    }
}
