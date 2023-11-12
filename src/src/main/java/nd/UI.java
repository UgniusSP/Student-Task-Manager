package nd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.LinkedList;

public class UI extends JFrame { 
    private static JTextField positionDel;
    private static JTextField positionAdd;
    private static JTextField taskTitleFieldAdd;
    private static JTextField taskTitleFieldDel;
    private static JTextField taskTitleFieldFindByTitle;
    private static JTextField taskTitleFieldFindByDeadline;
    private static JTextField subjectField;
    private static JTextField deadlineField;
    private static JTextField descriptionField;
    private static JTextField additionalInfoField;
    private static JTextField changeTaskField;
    private static JTextField newSubject;
    private static JTextField newDeadline;
    private static JTextField newDescription;
    private static JTextField newAdditionalInfo;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    JPanel addTaskPanel = new JPanel();
    JPanel deleteTaskPanel = new JPanel();
    JPanel deleteTaskByTitlePanel = new JPanel();
    JPanel printTaskPanel = new JPanel();
    JPanel findByDeadlinePanel = new JPanel();
    JPanel findByTitlePanel = new JPanel();
    JPanel changeTaskPanel = new JPanel();
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel sortTaskPanel = new JPanel(new BorderLayout());
    JPanel wipePanel = new JPanel();
    JTextArea boxText = new JTextArea(7,40);
    private static LinkedList <Task> list;

    public UI() {
        setTitle("Studento uzduociu tvarkytuvas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        boxText.setText("");

        createMainScreen();
        addTaskUI();
        printTasksUI();
        deleteByPosUI();
        deleteByTitleUI();
        findTaskByTitleUI();
        findTaskByDeadlineUI();
        wipeListUI();
        changeTaskUI();
        sortTasksByDeadlineUI();
        goBackButton(addTaskPanel, "AddTaskScreen");
        goBackButton(printTaskPanel, "ShowTaskScreen");
        goBackButton(deleteTaskPanel, "DelByPosScreen");
        goBackButton(deleteTaskByTitlePanel, "DelByTitleScreen");
        goBackButton(findByTitlePanel, "FindByTitleTaskScreen");
        goBackButton(findByDeadlinePanel, "FindByDeadTaskScreen");
        goBackButton(changeTaskPanel, "ChangeTaskScreen");
        goBackButton(wipePanel, "WipeTaskScreen");
        goBackButton(sortTaskPanel, "SortTaskScreen");
        
        add(cardPanel);

        setVisible(true);
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI();
            }
        });
    }

    private void createMainScreen() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(200,200));
        
        JButton[] buttons = {
            createButton("Pridėti užduotį", Color.GREEN, "AddTaskScreen"),
            createButton("Ištrinti užduotį pagal poziciją", Color.RED, "DelByPosScreen"),
            createButton("Ištrinti užduotį pagal pavadinimą", Color.BLUE, "DelByTitleScreen"),
            createButton("Rasti užduotį pagal terminą", Color.ORANGE, "FindByDeadTaskScreen"),
            createButton("Rodyti visas uzduotis", Color.YELLOW, "ShowTaskScreen"),
            createButton("Rasti užduotį pagal pavadinimą", Color.MAGENTA, "FindByTitleTaskScreen"),
            createButton("Pakeisti surastą užduotį", Color.MAGENTA, "ChangeTaskScreen"),
            createButton("Surūšiuoti užduotis pagal terminą ir jas parodyti ekrane", Color.BLUE, "SortTaskScreen"),
            createButton("Išvalyti sąrašą", Color.MAGENTA, "WipeTaskScreen")
        };
    
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        boxText.setLineWrap(true);
        boxText.setWrapStyleWord(true);
        boxText.setEditable(false);

        JPanel textAreaContainer = new JPanel(new GridBagLayout());
        textAreaContainer.setPreferredSize(new Dimension(500,500));
        textAreaContainer.add(new JScrollPane(boxText));

        boxText.setText(showClosestDeadlineUI());
        
        mainPanel.add(textAreaContainer, BorderLayout.CENTER);
    
        cardPanel.add(mainPanel, "MainScreen");
    }
    
    

    private String showClosestDeadlineUI() {
        StringBuilder output = new StringBuilder();
        
        Nuskaityti.sortTasksByDeadline(list);

            for (Task task : Nuskaityti.taskList) {
                output.append("Pavadinimas: " + task.getTaskTitle() + "\n");
                output.append("Dalykas: " + task.getSubject() + "\n");
                output.append("Termino pabaiga: " + task.getdeadline() + "\n");
                output.append("Aprasymas: " + task.getDescription() + "\n");
                output.append("Papildoma informacija: " + task.getAdditionalInfo() + "\n\n");
                break;
            }
        return output.toString();
    }

    
    
    private JButton createButton(String text, Color backgroundColor, String cardName) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
    
        button.addActionListener(e -> cardLayout.show(cardPanel, cardName));
    
        return button;
    }
    

    private void printTasksUI() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
    
        JButton printTaskButton = new JButton("Rodyti");
        printTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                Nuskaityti.readTasks();
                for (Task task : Nuskaityti.taskList) {
                    textArea.append("Pavadinimas: " + task.getTaskTitle() + "\n");
                    textArea.append("Dalykas: " + task.getSubject() + "\n");
                    textArea.append("Termino pabaiga: " + task.getdeadline() + "\n");
                    textArea.append("Aprasymas: " + task.getDescription() + "\n");
                    textArea.append("Papildoma informacija: " + task.getAdditionalInfo() + "\n\n");
                }
                Nuskaityti.wipeList(Nuskaityti.taskList);
    
                JScrollPane scrollPane = new JScrollPane(textArea);
  
                JOptionPane.showMessageDialog(printTaskPanel, scrollPane, "Rastos uzduotys", JOptionPane.PLAIN_MESSAGE);
            }
        });

        printTaskPanel.add(printTaskButton);
    
        cardPanel.add(printTaskPanel, "ShowTaskScreen");
    }

    private void deleteByPosUI(){
        deleteTaskPanel.setLayout(new GridLayout(2, 1));
        
        positionDel = new JTextField(20);
        deleteTaskPanel.add(new JLabel("Pozicija:"));
        deleteTaskPanel.add(positionDel);

        JButton deleteByPos = new JButton("Istrinti");
        deleteByPos.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String temp = positionDel.getText();

            if (!temp.isEmpty()) {
                try {
                    int pos = Integer.parseInt(temp);
                    
                    Nuskaityti.deleteTaskByPos(pos);
                    Nuskaityti.deleteTaskInFileByPos(pos);
                    System.out.println("success");

                } catch (NumberFormatException ex) {
                    System.err.println("Invalid position input: " + temp);
                }
            } else {
                System.err.println("Position field is empty");
            }
        }
        });

        deleteTaskPanel.add(deleteByPos);

        cardPanel.add(deleteTaskPanel, "DelByPosScreen");
    }

    private void deleteByTitleUI(){
        deleteTaskByTitlePanel.setLayout(new GridLayout(2, 1));
        
        taskTitleFieldDel = new JTextField(20);

        deleteTaskByTitlePanel.add(new JLabel("Uzduoties pavadinimas:"));
        deleteTaskByTitlePanel.add(taskTitleFieldDel);

        JButton deleteByTitle = new JButton("Istrinti");
        deleteByTitle.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = taskTitleFieldDel.getText();
            
            if(Nuskaityti.deltemp > 1){

            } else {
                Nuskaityti.deleteTaskByTitle(title);
                Nuskaityti.deleteTaskInFileTitle(title);
            }

            
            System.out.println("success");
        }
        });

        deleteTaskByTitlePanel.add(deleteByTitle);

        cardPanel.add(deleteTaskByTitlePanel, "DelByTitleScreen");
    }

    private void addTaskUI(){
        addTaskPanel.setLayout(new GridLayout(7, 2));

        positionAdd = new JTextField(20);
        taskTitleFieldAdd = new JTextField(20);
        subjectField = new JTextField(20);
        deadlineField = new JTextField(20);
        descriptionField = new JTextField(20);
        additionalInfoField = new JTextField(20);

        addTaskPanel.add(new JLabel("Pozicija:"));
        addTaskPanel.add(positionAdd);
        addTaskPanel.add(new JLabel("Uzduoties pavadinimas:"));
        addTaskPanel.add(taskTitleFieldAdd);
        addTaskPanel.add(new JLabel("Dalykas:"));
        addTaskPanel.add(subjectField);
        addTaskPanel.add(new JLabel("Terminas:"));
        addTaskPanel.add(deadlineField);
        addTaskPanel.add(new JLabel("Aprasymas:"));
        addTaskPanel.add(descriptionField);
        addTaskPanel.add(new JLabel("Papildoma informacija:"));
        addTaskPanel.add(additionalInfoField);

        JButton addButton = new JButton("Prideti uzduoti");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int posStr = Integer.parseInt(positionAdd.getText());
                    String title = taskTitleFieldAdd.getText();
                    String subject = subjectField.getText();
                    String deadline = deadlineField.getText();
                    String description = descriptionField.getText();
                    String additional = additionalInfoField.getText();
                    
                    Nuskaityti.addTask(posStr, title, subject, deadline, description, additional);
                    
                    positionAdd.setText("");
                    taskTitleFieldAdd.setText("");
                    subjectField.setText("");
                    deadlineField.setText("");
                    descriptionField.setText("");
                    additionalInfoField.setText("");

                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });

        addTaskPanel.add(addButton);

        cardPanel.add(addTaskPanel, "AddTaskScreen");

    }

    private void findTaskByTitleUI(){
        findByTitlePanel.setLayout(new GridLayout(2, 1));
            
        taskTitleFieldFindByTitle = new JTextField(20);
    
        findByTitlePanel.add(new JLabel("Uzduoties pavadinimas:"));
        findByTitlePanel.add(taskTitleFieldFindByTitle);
    
        JButton findByTitle = new JButton("Rasti");
        findByTitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = taskTitleFieldFindByTitle.getText();
               
                Nuskaityti.findTaskByTitle(title);
    
                JTextArea resultTextArea = new JTextArea(10, 30);
                resultTextArea.setEditable(false);
    
                for (Task result : Nuskaityti.queue) {
                    resultTextArea.append(result + "\n");
                }
    
                JScrollPane scrollPane = new JScrollPane(resultTextArea);
  
                JOptionPane.showMessageDialog(findByTitlePanel, scrollPane, "Rastos uzduotys", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        findByTitlePanel.add(findByTitle);
    
        cardPanel.add(findByTitlePanel, "FindByTitleTaskScreen");
    }
    

    private void findTaskByDeadlineUI(){
        findByDeadlinePanel.setLayout(new GridLayout(2, 1));
        
        taskTitleFieldFindByDeadline = new JTextField(20);

        findByDeadlinePanel.add(new JLabel("Termino data:"));
        findByDeadlinePanel.add(taskTitleFieldFindByDeadline);

        JButton findByDeadline = new JButton("Rasti");
        findByDeadline.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = taskTitleFieldFindByDeadline.getText();
           
            Nuskaityti.findTaskByDeadline(title);
            System.out.println(Nuskaityti.queue);

            JTextArea resultTextArea = new JTextArea(10, 30);
            resultTextArea.setEditable(false);
    
            for (Task result : Nuskaityti.queue) {
                resultTextArea.append(result + "\n");
            }
    
            JScrollPane scrollPane = new JScrollPane(resultTextArea);
  
            JOptionPane.showMessageDialog(findByTitlePanel, scrollPane, "Rastos uzduotys", JOptionPane.PLAIN_MESSAGE);
        }
        });
        
        findByDeadlinePanel.add(findByDeadline);

        cardPanel.add(findByDeadlinePanel, "FindByDeadTaskScreen");
    }

    private void changeTaskUI(){
        changeTaskPanel.setLayout(new GridLayout(7,2));

        changeTaskField = new JTextField(20);
        newSubject = new JTextField(20);
        newDeadline = new JTextField(20);
        newDescription = new JTextField(20);
        newAdditionalInfo = new JTextField(20);

        changeTaskPanel.add(new JLabel("Užduoties pavadinimas:"));
        changeTaskPanel.add(changeTaskField);
        changeTaskPanel.add(new JLabel("Naujas dalykas:"));
        changeTaskPanel.add(newSubject);
        changeTaskPanel.add(new JLabel("Naujas terminas:"));
        changeTaskPanel.add(newDeadline);
        changeTaskPanel.add(new JLabel("Naujas aprašymas:"));
        changeTaskPanel.add(newDescription);
        changeTaskPanel.add(new JLabel("Nauja papildoma info:"));
        changeTaskPanel.add(newAdditionalInfo);

        JButton changeButton = new JButton("Keisti");
        changeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if(Nuskaityti.taskInList != null && Nuskaityti.taskInQueue != null){
                
            // } else {
            //     JOptionPane.showMessageDialog(changeTaskField, "Nerasta ne viena uzduotis!", "Rastos uzduotys", JOptionPane.PLAIN_MESSAGE);
            // }   
            Nuskaityti.changeTask(changeTaskField.getText(), newSubject.getText(), newDeadline.getText(), newDescription.getText(), newAdditionalInfo.getText());
        } 
        });

        changeTaskPanel.add(changeButton);

        cardPanel.add(changeTaskPanel, "ChangeTaskScreen");
    }

    private void goBackButton(JPanel panel, String cardName) {
        JButton goBackButton = new JButton("Grįžti");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "MainScreen");
            boxText.setText(showClosestDeadlineUI());
        });
        panel.add(goBackButton);
        cardPanel.add(panel, cardName);
    }

    private void wipeListUI() {
        JButton wipeButton = new JButton("Išvalyti");
        wipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Nuskaityti.readTasks();
                Nuskaityti.wipeList(Nuskaityti.taskList);
                Nuskaityti.writeTasks();
            }
        });

        wipePanel.add(wipeButton);
    
        cardPanel.add(wipePanel, "WipeTaskScreen");
    }

    public void sortTasksByDeadlineUI(){
        sortTaskPanel.setLayout(new GridLayout(2,1));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JButton sortButton = new JButton("Rūšiuoti");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                Nuskaityti.sortTasksByDeadline(Nuskaityti.taskList);
                for (Task task : Nuskaityti.taskList) {
                    textArea.append("Pavadinimas: " + task.getTaskTitle() + "\n");
                    textArea.append("Dalykas: " + task.getSubject() + "\n");
                    textArea.append("Termino pabaiga: " + task.getdeadline() + "\n");
                    textArea.append("Aprasymas: " + task.getDescription() + "\n");
                    textArea.append("Papildoma informacija: " + task.getAdditionalInfo() + "\n\n");
                }
                Nuskaityti.wipeList(Nuskaityti.taskList);
                JScrollPane scrollPane = new JScrollPane(textArea);

                JOptionPane.showMessageDialog(sortTaskPanel, scrollPane, "Surūšiuotos užduotys", JOptionPane.PLAIN_MESSAGE);
                textArea.setText("");
            }
        });

        sortTaskPanel.add(sortButton);

        cardPanel.add(sortTaskPanel, "SortTaskScreen");
    }
}