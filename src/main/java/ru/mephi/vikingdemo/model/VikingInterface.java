package ru.mephi.vikingdemo.model;

import java.util.List;

public interface VikingInterface {
    String name();
    int age();
    int heightCm();
    HairColor hairColor();
    BeardStyle beardStyle();
    List<EquipmentItem> equipment();
}
