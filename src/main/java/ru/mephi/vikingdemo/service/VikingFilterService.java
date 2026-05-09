package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingRepository;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;
import java.util.Map;
import java.util.Random;
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
     * Использует лямбда-функции для фильтрации в VikingRepository.countByAge.
     */
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

    /**
     * Получает количество викингов по стилю бороды и цвету волос.
     * Использует лямбда-функции для фильтрации в VikingRepository.countByAppearance.
     */
    public long getCountByAppearance(
            BeardStyle beardStyle,
            HairColor hairColor
    ) {
        return vikingRepository.countByAppearance(beardStyle, hairColor);
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
     * Находит рыжих викингов и сортирует их по возрасту (по убыванию).
     * Лямбда-функции: v -> v.hairColor() == HairColor.Red и (v1, v2) -> Integer.compare(v2.age(), v1.age())
     */
    public List<Viking> findRedBeardedSortedByAgeDesc() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red)
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
