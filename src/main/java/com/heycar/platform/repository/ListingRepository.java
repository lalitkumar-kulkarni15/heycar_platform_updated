package com.heycar.platform.repository;

import com.heycar.platform.document.ListingDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListingRepository extends ElasticsearchRepository<ListingDocument,String> {

    public List<ListingDocument> findByMakeAndModelAndYearAndColor(String make, String model, String year, String color);

}
