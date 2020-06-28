package com.spacetime.tardis;

import com.spacetime.tardis.exceptions.ParadoxException;
import com.spacetime.tardis.model.PlaceTime;
import com.spacetime.tardis.model.Traveller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.spacetime.tardis.validations.ValidationRegex.PERSONAL_GALACTIC_IDENTIFIER;
import static com.spacetime.tardis.validations.ValidationRegex.PGI_LENGTH_MESSAGE;
import static com.spacetime.tardis.validations.ValidationRegex.PGI_REGEX_MESSAGE;

@RestController
@Validated
@RequestMapping("/api/v1")
public class TardisController {

    private TardisService tardisService;

    @Autowired
    TardisController(TardisService tardisService) {
        this.tardisService = tardisService;
    }

    @PostMapping("/travel/{id}")
    ResponseEntity<?> travelRequest(@Size(min=5, max=10, message = PGI_LENGTH_MESSAGE)
                                    @Pattern(regexp = PERSONAL_GALACTIC_IDENTIFIER, message = PGI_REGEX_MESSAGE)
                                    @PathVariable String id,
                                    @RequestBody @Valid PlaceTime placeTime){
        try {
            return ResponseEntity.ok(tardisService.handleTravelRequest(id, placeTime));
        }
        catch (ParadoxException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/traveller/{id}")
    ResponseEntity<Traveller> getTraveller(@Size(min=5, max=10, message = PGI_LENGTH_MESSAGE)
                                  @Pattern(regexp = PERSONAL_GALACTIC_IDENTIFIER, message = PGI_REGEX_MESSAGE)
                                  @PathVariable String id) {
        return ResponseEntity.ok(tardisService.findTravellerById(id));
    }

    @DeleteMapping("/traveller/{id}")
    void deleteTraveller(@Size(min=5, max=10, message = PGI_LENGTH_MESSAGE)
                         @Pattern(regexp = PERSONAL_GALACTIC_IDENTIFIER, message = PGI_REGEX_MESSAGE)
                         @PathVariable String id) {
        tardisService.deleteTravellerById(id);
    }

    @GetMapping("/travellers")
    ResponseEntity<List<Traveller>> all() {
        return ResponseEntity.ok(tardisService.findAllTravellers());
    }
}
