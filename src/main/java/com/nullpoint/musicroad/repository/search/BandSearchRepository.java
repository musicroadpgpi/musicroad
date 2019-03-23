package com.nullpoint.musicroad.repository.search;

import com.nullpoint.musicroad.domain.Band;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Band entity.
 */
public interface BandSearchRepository extends ElasticsearchRepository<Band, Long> {
}
