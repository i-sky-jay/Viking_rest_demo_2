package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка 

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;
    private final Random random = new Random();
    
    
    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }
    
    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }
    public void deleteById(int id) {
        vikingStorage.deleteById(id);
    }

    public List<Viking> findTallerThan(int height) {
        return findAll().stream()
                .filter(v -> v.heightCm() > height)
                .collect(Collectors.toList());
    }

    public List<Viking> findWithLegendaryEquipment() {
        return findAll().stream()
                .filter(v -> v.equipment().stream().anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality())))
                .collect(Collectors.toList());
    }

    public List<Viking> findRedBeardedSortedByAgeDesc() {
        return findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted((v1, v2) -> Integer.compare(v2.age(), v1.age()))
                .collect(Collectors.toList());
    }

    public Viking getRandomTallerThan(int height) {
        List<Viking> vikings = findTallerThan(height);
        if (vikings.isEmpty()) return null;
        return vikings.get(random.nextInt(vikings.size()));
    }
}
