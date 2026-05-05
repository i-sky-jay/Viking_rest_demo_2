package ru.mephi.vikingdemo.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;


@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    @Transactional
    public Viking save(Viking viking) {
        Integer vikingId = vikingRepository.save(
                vikingMapper.toVikingEntity(viking)
        );

        for (EquipmentItem item : viking.equipment()) {
            equipmentItemRepository.save(
                    vikingMapper.toEquipmentItemEntity(vikingId, item)
            );
        }

        return viking;
    }

    public List<Viking> findAll() {
        return findAllWithIds().values().stream().toList();
    }

    public Map<Integer, Viking> findAllWithIds() {
        List<VikingEntity> vikingEntities = vikingRepository.findAll();
        List<EquipmentItemEntity> equipmentEntities = equipmentItemRepository.findAll();

        Map<Integer, List<EquipmentItemEntity>> equipmentByVikingId = equipmentEntities.stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingEntities.stream()
                .collect(Collectors.toMap(
                        VikingEntity::id,
                        vikingEntity -> vikingMapper.toViking(
                                vikingEntity,
                                equipmentByVikingId.getOrDefault(vikingEntity.id(), List.of())
                        )
                ));
    }

    @Transactional
    public void deleteById(int id) {
        vikingRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String name) {
        findAllWithIds().entrySet().stream()
                .filter(e -> e.getValue().name().equals(name))
                .findFirst()
                .ifPresent(e -> deleteById(e.getKey()));
    }

    public Viking findByName(String name) {
        return findAll().stream()
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void update(Viking viking) {
        deleteByName(viking.name());
        save(viking);
    }

    public long countByAxes() {
        List<Viking> allVikings = findAll();

        return allVikings.stream()
                .filter(viking -> {
                    long axeCount = viking.equipment().stream()
                            .filter(item -> "Axe".equalsIgnoreCase(item.name()))
                            .count();
                    return axeCount == 1 || axeCount == 2;
                })
                .count();
    }
}
