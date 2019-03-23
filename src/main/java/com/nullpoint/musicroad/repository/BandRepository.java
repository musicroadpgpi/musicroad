package com.nullpoint.musicroad.repository;

import com.nullpoint.musicroad.domain.Band;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Band entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BandRepository extends JpaRepository<Band, Long> {

    @Query(value = "select distinct band from Band band left join fetch band.collaborations",
        countQuery = "select count(distinct band) from Band band")
    Page<Band> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct band from Band band left join fetch band.collaborations")
    List<Band> findAllWithEagerRelationships();

    @Query("select band from Band band left join fetch band.collaborations where band.id =:id")
    Optional<Band> findOneWithEagerRelationships(@Param("id") Long id);

}
