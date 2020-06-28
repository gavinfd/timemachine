package com.spacetime.tardis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacetime.tardis.model.PlaceTime;
import com.spacetime.tardis.model.Traveller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TardisControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TardisService tardisService;

    @Test
    void test_getTraveller() throws Exception {

        given(tardisService.findTravellerById("abc123")).willReturn(expectedTraveller());

        mvc.perform(get("/api/v1/traveller/" + "abc123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalGalacticIdentifier").value(expectedTraveller().getPersonalGalacticIdentifier()))
                .andExpect(jsonPath("$.placesDatesVisited[*].place").value("London"))
                .andExpect(jsonPath("$.placesDatesVisited[*].date").value("2020-01-01T00:00:00"));

    }

    @Test
    void test_getTravellers() throws Exception {

        given(tardisService.findAllTravellers()).willReturn(List.of(expectedTraveller()));

        mvc.perform(get("/api/v1/travellers")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalGalacticIdentifier").value(expectedTraveller().getPersonalGalacticIdentifier()))
                .andExpect(jsonPath("$[0].placesDatesVisited[*].place").value("London"))
                .andExpect(jsonPath("$[0].placesDatesVisited[*].date").value("2020-01-01T00:00:00"));
    }

    @Test
    void test_travelRequest() throws Exception {
        PlaceTime placeTime = PlaceTime.builder()
                .place("London")
                .date(LocalDateTime.of(2020, 01, 01, 0, 0))
                .build();

        Traveller expectedTraveller = Traveller.builder()
                .personalGalacticIdentifier("abc123")
                .placeTimes(List.of(placeTime))
                .build();

        given(tardisService.handleTravelRequest("abc123", placeTime)).willReturn(expectedTraveller);

        mvc.perform(post("/api/v1/travel/abc123")
                .content(asJsonString(placeTime))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalGalacticIdentifier").value(expectedTraveller.getPersonalGalacticIdentifier()))
                .andExpect(jsonPath("$.placesDatesVisited[*].place").value("London"))
                .andExpect(jsonPath("$.placesDatesVisited[*].date").value("2020-01-01T00:00:00"));
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

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
