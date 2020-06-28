package com.spacetime.tardis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.spacetime.tardis.validations.ValidationRegex.PERSONAL_GALACTIC_IDENTIFIER;
import static com.spacetime.tardis.validations.ValidationRegex.PGI_LENGTH_MESSAGE;
import static com.spacetime.tardis.validations.ValidationRegex.PGI_REGEX_MESSAGE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table
public class Traveller {

    @Size(min=5, max=10, message = PGI_LENGTH_MESSAGE)
    @Pattern(regexp = PERSONAL_GALACTIC_IDENTIFIER, message = PGI_REGEX_MESSAGE)
    @JsonProperty("personalGalacticIdentifier")
    private @Id String personalGalacticIdentifier;

    @Builder.Default
    @OneToMany(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JsonProperty("placesDatesVisited")
    private List<PlaceTime> placeTimes = new ArrayList<>();

}


