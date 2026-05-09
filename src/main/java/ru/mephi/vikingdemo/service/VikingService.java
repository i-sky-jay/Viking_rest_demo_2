package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingInterface;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;
    
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

    public void addViking(Viking viking) {
        vikingStorage.save(viking);
    }

    public void deleteViking(String vikingName) {
        vikingStorage.deleteByName(vikingName);
    }

    public Viking findViking(String vikingName) {
        return vikingStorage.findByName(vikingName);
    }

    public void updateViking(Viking viking) {
        vikingStorage.update(viking);
    }
}
