package com.nullpoint.musicroad.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BandSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BandSearchRepositoryMockConfiguration {

    @MockBean
    private BandSearchRepository mockBandSearchRepository;

}
