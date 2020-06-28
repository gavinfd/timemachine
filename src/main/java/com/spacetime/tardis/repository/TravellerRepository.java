package com.spacetime.tardis.repository;

import com.spacetime.tardis.model.Traveller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TravellerRepository extends JpaRepository<Traveller, String> {
}
