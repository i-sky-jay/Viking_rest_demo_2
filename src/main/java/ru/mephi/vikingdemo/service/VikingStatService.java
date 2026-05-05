package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.repository.VikingRepository;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingStatService {

    private final VikingRepository vikingRepository;
    private final VikingStorage vikingStorage;

    public VikingStatService(VikingRepository vikingRepository, VikingStorage vikingStorage) {
        this.vikingRepository = vikingRepository;
        this.vikingStorage = vikingStorage;
    }

    public long getCountByAge(
            Integer lessThan,
            Integer greaterThan,
            Integer inRangeStart,
            Integer inRangeEnd,
            Integer outOfRangeStart,
            Integer outOfRangeEnd
    ) {
        return vikingRepository.countByAge(
                lessThan,
                greaterThan,
                inRangeStart,
                inRangeEnd,
                outOfRangeStart,
                outOfRangeEnd
        );
    }

    public long getCountByAppearance(
            BeardStyle beardStyle,
            HairColor hairColor
    ) {
        return vikingRepository.countByAppearance(beardStyle, hairColor);
    }

    public long getCountByAxes() {
        return vikingStorage.countByAxes();
    }
}
