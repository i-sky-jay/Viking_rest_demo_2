package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton openFiltersButton = new JButton("Open Filters");
        openFiltersButton.addActionListener(event -> onOpenFilters());

        JButton massCreateButton = new JButton("Mass create vikings");
        massCreateButton.addActionListener(event -> onMassCreateVikings());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(openFiltersButton);
        bottomPanel.add(massCreateButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onMassCreateVikings() {
        String input = JOptionPane.showInputDialog(this, "Enter number of vikings to create:");
        if (input != null && !input.isEmpty()) {
            try {
                int count = Integer.parseInt(input);
                if (count > 0) {
                    List<Viking> vikings = vikingService.createRandomVikings(count);
                    for (Viking v : vikings) {
                        tableModel.addViking(v);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onOpenFilters() {
        VikingFilterFrame filterFrame = new VikingFilterFrame(vikingService);
        filterFrame.setVisible(true);
    }
    
    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    public void deleteViking(String vikingName) {
        tableModel.removeViking(vikingName);
    }

    public void updateViking(Viking viking) {
        tableModel.removeViking(viking.name());
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }
}
