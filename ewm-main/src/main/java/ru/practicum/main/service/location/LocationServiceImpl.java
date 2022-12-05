package ru.practicum.main.service.location;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.model.location.Location;
import ru.practicum.main.model.location.QLocation;
import ru.practicum.main.model.location.dto.LocationDto;
import ru.practicum.main.repository.LocationRepository;
import ru.practicum.main.utilities.GetCoordinateParam;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.main.mapper.LocationMapper.*;
import static ru.practicum.main.utilities.Checker.*;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public LocationDto save(LocationDto locationDto) {
        var location = locationRepository.save(toLocation(locationDto));
        return toLocationDto(location);
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        var location = checkLocationExists(locationDto.getId(), locationRepository);

        Optional.ofNullable(locationDto.getAddress()).ifPresent(location::setAddress);
        Optional.ofNullable(locationDto.getLat()).ifPresent(location::setLat);
        Optional.ofNullable(locationDto.getLon()).ifPresent(location::setLon);
        Optional.ofNullable(locationDto.getRad()).ifPresent(location::setRad);

        if (locationDto.getTags() != null && !locationDto.getTags().isEmpty()) {
            location.setTags(locationDto.getTags());
        }
        return toLocationDto(locationRepository.save(location));
    }

    @Override
    public void delete(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    @Override
    public LocationDto findById(Long locationId) {
        var location = checkLocationExists(locationId, locationRepository);
        return toLocationDto(location);
    }

    @Override
    public Collection<LocationDto> findByParamsInArea(String address,
                                                      Set<String> tags,
                                                      GetCoordinateParam coordinateParam,
                                                      PageRequest pageRequest) {

        var locationsByParams = getLocationsByParams(address, tags, pageRequest);

        if (coordinateParam == null) {
            return toLocationDtos(locationsByParams);
        } else {
            return toLocationDtos(getLocationsInArea(coordinateParam, pageRequest, locationsByParams));
        }
    }

    private Set<Location> getLocationsByParams(String address, Set<String> tags, PageRequest pageRequest) {
        var location = QLocation.location;
        var conditions = new ArrayList<BooleanExpression>();

        if (address != null && !address.isBlank()) {
            conditions.add(location.address.containsIgnoreCase(address));
        }
        if (tags != null && !tags.isEmpty()) {
            conditions.add(location.tags.any().in(tags));
        }
        if (conditions.isEmpty()) {
            conditions.add(location.id.isNotNull());
        }

        var finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        return locationRepository.findAll(finalCondition, pageRequest).stream()
                .collect(Collectors.toSet());
    }

    private List<Location> getLocationsInArea(GetCoordinateParam coordinateParam,
                                              PageRequest pageRequest,
                                              Set<Location> locationsByParams) {
        var locationsInArea = new HashSet<>(locationRepository.findInArea(coordinateParam.getLat(),
                coordinateParam.getLon(),
                coordinateParam.getRadius(),
                pageRequest));

        return locationsByParams.stream()
                .filter(locationsInArea::contains)
                .collect(Collectors.toList());
    }
}
