package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingFilterService;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class VikingFilterFrame extends JFrame {

    private final VikingFilterService vikingFilterService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingFilterFrame(VikingFilterService vikingFilterService) {
        this.vikingFilterService = vikingFilterService;

        setTitle("Viking Filters");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1000, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton randomTallerBtn = new JButton("Случайный викинг > 180");
        JButton legendaryBtn = new JButton("Все с легендарным снаряжением");
        JButton redBeardedBtn = new JButton("Рыжебородые по возрасту (desc)");

        randomTallerBtn.addActionListener(e -> {
            Viking v = vikingFilterService.getRandomTallerThan(180);
            if (v != null) {
                tableModel.setVikings(Collections.singletonList(v));
            } else {
                tableModel.setVikings(Collections.emptyList());
                JOptionPane.showMessageDialog(this, "Викингов выше 180 см не найдено");
            }
        });

        legendaryBtn.addActionListener(e -> {
            List<Viking> vikings = vikingFilterService.findWithLegendaryEquipment();
            tableModel.setVikings(vikings);
            if (vikings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Викингов с легендарным снаряжением не найдено");
            }
        });

        redBeardedBtn.addActionListener(e -> {
            List<Viking> vikings = vikingFilterService.findRedBeardedSortedByAgeDesc();
            tableModel.setVikings(vikings);
            if (vikings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Рыжебородых викингов не найдено");
            }
        });

        topPanel.add(randomTallerBtn);
        topPanel.add(legendaryBtn);
        topPanel.add(redBeardedBtn);

        add(topPanel, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);
    }
}
