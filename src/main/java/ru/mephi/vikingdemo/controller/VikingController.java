package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingStatService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingStatService vikingStatService;
    private VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingStatService vikingStatService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingStatService = vikingStatService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", 
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/stats/count-by-age")
    @Operation(summary = "Подсчитать викингов по возрасту", 
            operationId = "getCountByAge")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество успешно получено")
    })
    public long getCountByAge(
            @RequestParam(required = false) Integer lessThan,
            @RequestParam(required = false) Integer greaterThan,
            @RequestParam(required = false) Integer inRangeStart,
            @RequestParam(required = false) Integer inRangeEnd,
            @RequestParam(required = false) Integer outOfRangeStart,
            @RequestParam(required = false) Integer outOfRangeEnd
    ) {
        System.out.println("GET /api/vikings/stats/count-by-age called");
        return vikingStatService.getCountByAge(lessThan, greaterThan, inRangeStart, inRangeEnd, outOfRangeStart, outOfRangeEnd);
    }

    @GetMapping("/stats/count-by-appearance")
    @Operation(summary = "Подсчитать викингов по внешности", 
            operationId = "getCountByAppearance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество успешно получено")
    })
    public long getCountByAppearance(
            @RequestParam(required = false) BeardStyle beardStyle,
            @RequestParam(required = false) HairColor hairColor
    ) {
        System.out.println("GET /api/vikings/stats/count-by-appearance called");
        return vikingStatService.getCountByAppearance(beardStyle, hairColor);
    }

    @GetMapping("/stats/count-by-axes")
    @Operation(summary = "Подсчитать викингов с 1 или 2 топорами", 
            operationId = "getCountByAxes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество успешно получено")
    })
    public long getCountByAxes() {
        System.out.println("GET /api/vikings/stats/count-by-axes called");
        return vikingStatService.getCountByAxes();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", 
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }
    
    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами", 
            operationId = "post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public void addViking(){
        System.out.println("POST api/vikings/post called");
        vikingListener.testAdd();
    }

    @GetMapping("/ids/max")
    @Operation(summary = "Найти последнюю запись (max ID)", 
            operationId = "getMaxIdViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запись успешно найдена")
    })
    public Viking getMaxIdViking() {
        System.out.println("GET /api/vikings/ids/max called");
        return vikingService.findMaxIdViking();
    }

    @GetMapping("/ids/even")
    @Operation(summary = "Получить все записи с четными ID", 
            operationId = "getEvenIdVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getEvenIdVikings() {
        System.out.println("GET /api/vikings/ids/even called");
        return vikingService.findEvenIdVikings();
    }
}
