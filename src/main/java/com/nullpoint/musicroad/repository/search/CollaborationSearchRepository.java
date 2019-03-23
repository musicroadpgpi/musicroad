package com.nullpoint.musicroad.repository.search;

import com.nullpoint.musicroad.domain.Collaboration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Collaboration entity.
 */
public interface CollaborationSearchRepository extends ElasticsearchRepository<Collaboration, Long> {
}
