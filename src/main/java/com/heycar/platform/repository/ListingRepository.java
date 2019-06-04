package com.heycar.platform.repository;

import com.heycar.platform.document.ListingDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  This is the vehicle listing repository which extends the elastic search repository and
 *  houses the methods to interact with the elastic search storage.
 * </p>
 *
 * @author  Lalitkumar Kulkarni
 * @since   02-06-2019
 * @version 1.0
 */
@Repository
public interface ListingRepository extends ElasticsearchRepository<ListingDocument,String> {

    /**
     * <p>
     *  This method is used to search the vehicle listing on the basis of :-
     *  <ul>
     *    <li>Make</li>
     *    <li>Model</li>
     *    <li>Year</li>
     *    <li>Color</li>
     *  </ul>
     * </p>
     *
     * @param  make  This is te make of the given vehicle.
     * @param  model This is the model of the given make of the vehicle.
     * @param  year  This is the year of the given vehicle.
     * @param  color This is the color of the given vehicle.
     * @return {@link List<ListingDocument>}
     */
    public List<ListingDocument> findByMakeAndModelAndYearAndColor(String make, String model, String year, String color);

}
