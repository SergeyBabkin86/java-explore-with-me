package ru.practicum.main.service.location;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main.model.location.dto.LocationDto;
import ru.practicum.main.utilities.GetCoordinateParam;

import java.util.Collection;
import java.util.Set;

public interface LocationService {

    LocationDto save(LocationDto locationDto);

    LocationDto update(LocationDto locationDto);

    void delete(Long locationId);

    LocationDto findById(Long locationId);

    Collection<LocationDto> findByParamsInArea(String address,
                                               Set<String> tags,
                                               GetCoordinateParam coordinateParam,
                                               PageRequest pageRequest);
}
