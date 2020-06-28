package com.spacetime.tardis;

import com.spacetime.tardis.exceptions.ParadoxException;
import com.spacetime.tardis.exceptions.TravellerNotFoundException;
import com.spacetime.tardis.model.PlaceTime;
import com.spacetime.tardis.model.Traveller;
import com.spacetime.tardis.repository.TravellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class TardisServiceTest {

    @MockBean
    private TravellerRepository travellerRepository;

    @Autowired
    private TardisService tardisService;

    @Test
    public void test_findAllTravellers() {
        Traveller expectedTraveller = Traveller.builder()
                .personalGalacticIdentifier("abc123")
                .placeTimes(List.of(PlaceTime.builder()
                        .place("London")
                        .date(LocalDateTime.of(2020, 01, 01, 0, 0))
                        .build()))
                .build();

        when(travellerRepository.findAll()).thenReturn(List.of(expectedTraveller));

        List<Traveller> travellers = tardisService.findAllTravellers();
        assertThat(travellers.size()).isEqualTo(1);
    }

    @Test
    public void test_findTravellerById() {

        when(travellerRepository.findById(eq("abc123")))
                .thenReturn(ofNullable(expectedTraveller()));

        assertThat(tardisService.findTravellerById("abc123")).isEqualTo(expectedTraveller());
    }

    @Test
    public void test_findTravellerById_TravellerNotFoundException() {

        when(travellerRepository.findById(eq("garbage")))
                .thenThrow(new TravellerNotFoundException("garbage"));

        assertThatThrownBy(() -> tardisService.findTravellerById("garbage"))
                .isInstanceOf(TravellerNotFoundException.class)
                .hasMessage("Could not find traveller garbage");
    }

    @Test
    public void test_deleteTravellerById() {
        tardisService.deleteTravellerById("abc123");
        verify(travellerRepository).deleteById("abc123");
    }

    @Test
    public void test_handleTravelRequest() {
        PlaceTime placeTime = PlaceTime.builder()
                .place("Paris")
                .date(LocalDateTime.of(2020, 01, 01, 0, 0))
                .build();

        Traveller expectedTraveller = expectedTraveller();

        when(travellerRepository.findById(eq("abc123")))
                .thenReturn(ofNullable(expectedTraveller));
        tardisService.handleTravelRequest("abc123", placeTime);
        expectedTraveller.getPlaceTimes().add(placeTime);
        verify(travellerRepository).save(expectedTraveller);
    }


    @Test
    public void test_handleTravelRequest_paradox() {
        when(travellerRepository.findById(eq("abc123")))
                .thenReturn(ofNullable(expectedTraveller()));

        assertThatThrownBy(() -> tardisService.handleTravelRequest("abc123", expectedTraveller().getPlaceTimes().get(0)))
                .isInstanceOf(ParadoxException.class)
                .hasMessage("Could not allow traveller abc123 to visit requested place and " +
                        "time due to already being present!");
    }

    private Traveller expectedTraveller() {
        List<PlaceTime> placeTimes = new ArrayList<>();
        placeTimes.add(PlaceTime.builder()
                .place("London")
                .date(LocalDateTime.of(2020, 01, 01, 0, 0))
                .build());

        return Traveller.builder()
                .personalGalacticIdentifier("abc123")
                .placeTimes(placeTimes)
                .build();
    }
}
