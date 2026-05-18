package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;
import ru.mephi.vikingdemo.repository.VikingRepository;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Сервис для фильтрации, поиска и подсчета викингов по различным критериям.
 * Название отражает суть: выполнение запросов (queries) и фильтрации данных.
 */
@Service
public class VikingFilterService {

    private final VikingRepository vikingRepository;
    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingFilterService(VikingRepository vikingRepository, VikingStorage vikingStorage) {
        this.vikingRepository = vikingRepository;
        this.vikingStorage = vikingStorage;
    }

    /**
     * Получает количество викингов в заданных возрастных диапазонах.
     */
    public long getCountByAge(
            Integer lessThan,
            Integer greaterThan,
            Integer inRangeStart,
            Integer inRangeEnd,
            Integer outOfRangeStart,
            Integer outOfRangeEnd
    ) {
        List<VikingEntity> allVikings = vikingRepository.findAll();

        Predicate<VikingEntity> ageFilter = viking -> true;

        if (lessThan != null) {
            ageFilter = ageFilter.and(v -> v.age() < lessThan);
        }
        if (greaterThan != null) {
            ageFilter = ageFilter.and(v -> v.age() > greaterThan);
        }
        if (inRangeStart != null && inRangeEnd != null) {
            ageFilter = ageFilter.and(v -> v.age() >= inRangeStart && v.age() <= inRangeEnd);
        }
        if (outOfRangeStart != null && outOfRangeEnd != null) {
            ageFilter = ageFilter.and(v -> v.age() < outOfRangeStart || v.age() > outOfRangeEnd);
        }

        return allVikings.stream()
                .filter(ageFilter)
                .count();
    }

    /**
     * Получает количество викингов по стилю бороды и цвету волос.
     */
    public long getCountByAppearance(
            BeardStyle beardStyle,
            HairColor hairColor
    ) {
        List<VikingEntity> allVikings = vikingRepository.findAll();

        Predicate<VikingEntity> appearanceFilter = viking -> true;

        if (beardStyle != null) {
            appearanceFilter = appearanceFilter.and(v -> v.beardStyle() == beardStyle);
        }
        if (hairColor != null) {
            appearanceFilter = appearanceFilter.and(v -> v.hairColor() == hairColor);
        }

        return allVikings.stream()
                .filter(appearanceFilter)
                .count();
    }

    /**
     * Получает количество викингов с одним или двумя топорами.
     * Использует лямбда-функции для фильтрации в VikingStorage.countByAxes.
     */
    public long getCountByAxes() {
        return vikingStorage.countByAxes();
    }

    /**
     * Находит викингов выше заданного роста.
     * Лямбда-функция: v -> v.heightCm() > height
     */
    public List<Viking> findTallerThan(int height) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.heightCm() > height)
                .collect(Collectors.toList());
    }

    /**
     * Находит викингов с легендарным снаряжением.
     * Лямбда-функция: e -> "Legendary".equalsIgnoreCase(e.quality())
     */
    public List<Viking> findWithLegendaryEquipment() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.equipment().stream().anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality())))
                .collect(Collectors.toList());
    }

    /**
     * Находит рыжебородых викингов (рыжих и с бородой) и сортирует их по возрасту (по убыванию).
     * Лямбда-функции: v -> v.hairColor() == HairColor.Red && v.beardStyle() != BeardStyle.CLEAN_SHAVEN
     * и (v1, v2) -> Integer.compare(v2.age(), v1.age())
     */
    public List<Viking> findRedBeardedSortedByAgeDesc() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red && v.beardStyle() != BeardStyle.CLEAN_SHAVEN)
                .sorted((v1, v2) -> Integer.compare(v2.age(), v1.age()))
                .collect(Collectors.toList());
    }

    public Viking getRandomTallerThan(int height) {
        List<Viking> vikings = findTallerThan(height);
        if (vikings.isEmpty()) return null;
        return vikings.get(random.nextInt(vikings.size()));
    }

    /**
     * Находит викинга с максимальным ID.
     * Лямбда-функции: Map.Entry.comparingByKey() и Map.Entry::getValue
     */
    public Viking findMaxIdViking() {
        return vikingStorage.findAllWithIds().entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    /**
     * Находит всех викингов с четными ID.
     * Лямбда-функции: entry -> entry.getKey() % 2 == 0 и Map.Entry::getValue
     */
    public List<Viking> findEvenIdVikings() {
        return vikingStorage.findAllWithIds().entrySet().stream()
                .filter(entry -> entry.getKey() % 2 == 0)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
