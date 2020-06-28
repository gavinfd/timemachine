package com.spacetime.tardis.repository;

import com.spacetime.tardis.model.Traveller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceTimeRepository extends JpaRepository<Traveller, String> {
}
