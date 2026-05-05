package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.repository.VikingRepository;

@Service
public class VikingStatService {

    private final VikingRepository vikingRepository;

    public VikingStatService(VikingRepository vikingRepository) {
        this.vikingRepository = vikingRepository;
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
}
