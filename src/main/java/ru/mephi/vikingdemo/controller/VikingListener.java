/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

/**
 *
 * @author test2023
 */
@Component
public class VikingListener {
    private VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }
    
    public void setGui(VikingDesktopFrame gui){
        this.gui = gui;
    }

    void testAdd() {
        gui.addNewViking(service.createRandomViking());
    }

    void addViking(Viking viking) {
        service.addViking(viking);
        gui.addNewViking(viking);
    }

    Viking findViking(String vikingName) {
        return service.findViking(vikingName);
    }

    public void deleteViking(String vikingName) {
        service.deleteViking(vikingName);
        gui.deleteViking(vikingName);
    }

    public void updateViking(Viking viking) {
        service.updateViking(viking);
        gui.updateViking(viking);
    }
}
