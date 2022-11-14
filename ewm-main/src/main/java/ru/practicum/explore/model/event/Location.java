package ru.practicum.explore.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
//@NoArgsConstructor()
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long LocationId;

    @Column(name = "latitude", nullable = false)
    Double lat;

    @Column(name = "longitude", nullable = false)
    Double lon;
}
