package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingInterface;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {

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

    public Map<Integer, Viking> findAllWithIds() {
        return vikingStorage.findAllWithIds();
    }

    public Viking createRandomViking() {
        VikingInterface viking = vikingFactory.createRandomViking();
        return vikingStorage.save((Viking) viking);
    }

    public List<Viking> createRandomVikings(int count) {
        List<Viking> created = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            created.add(createRandomViking());
        }
        return created;
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

    public Viking findMaxIdViking() {
        return findAllWithIds().entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public List<Viking> findEvenIdVikings() {
        return findAllWithIds().entrySet().stream()
                .filter(entry -> entry.getKey() % 2 == 0)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
