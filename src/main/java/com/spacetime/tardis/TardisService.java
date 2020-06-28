package com.spacetime.tardis;

import com.spacetime.tardis.exceptions.ParadoxException;
import com.spacetime.tardis.exceptions.TravellerNotFoundException;
import com.spacetime.tardis.model.PlaceTime;
import com.spacetime.tardis.model.Traveller;
import com.spacetime.tardis.repository.TravellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TardisService {

    private TravellerRepository travellerRepository;

    @Autowired
    TardisService(TravellerRepository travellerRepository) {
        this.travellerRepository = travellerRepository;
    }

    public List<Traveller> findAllTravellers() {
        return travellerRepository.findAll();
    }

    public Traveller findTravellerById(String id) {
        return travellerRepository.findById(id).orElseThrow(() -> new TravellerNotFoundException(id));
    }

    public void deleteTravellerById(String id) {
        travellerRepository.deleteById(id);
    }

    public Traveller handleTravelRequest(String id, PlaceTime placeTime) {
        PlaceTime parsedPlaceTime = cleanPlaceFormatting(placeTime);
        Traveller traveller = travellerRepository.findById(id)
                .orElse(Traveller.builder()
                        .personalGalacticIdentifier(id)
                        .build());
        boolean paradoxDetected = traveller.getPlaceTimes().stream().anyMatch(pt -> detectParadox(pt, parsedPlaceTime));
        if (paradoxDetected) {
            throw new ParadoxException(id);
        }
        traveller.getPlaceTimes().add(parsedPlaceTime);
        return travellerRepository.save(traveller);
    }

    private PlaceTime cleanPlaceFormatting(PlaceTime placeTime) {
        return placeTime.toBuilder().place(placeTime.getPlace().toLowerCase().trim()).build();
    }

    private boolean detectParadox(PlaceTime pt1, PlaceTime pt2) {
        if (pt1.getPlace().toLowerCase().equals(pt2.getPlace().toLowerCase())) {
            int year_diff = pt2.getDate().getYear() - pt2.getDate().getYear();
            int day_diff = pt2.getDate().getDayOfYear() - pt2.getDate().getDayOfYear();
            if (year_diff == 0 && day_diff == 0) {
                return true;
            }
        }
        return false;
    }
}
