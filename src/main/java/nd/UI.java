package nd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

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

    public UI() {
        setTitle("Studento uzduociu tvarkytuvas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        createMainScreen();
        addTaskUI();
        printTasksUI();
        deleteByPosUI();
        deleteByTitleUI();
        findTaskByTitleUI();
        findTaskByDeadlineUI();
        changeTaskUI();
        goBackButton(addTaskPanel, "AddTaskScreen");
        goBackButton(printTaskPanel, "ShowTaskScreen");
        goBackButton(deleteTaskPanel, "DelByPosScreen");
        goBackButton(deleteTaskByTitlePanel, "DelByTitleScreen");
        goBackButton(findByTitlePanel, "FindByTitleTaskScreen");
        goBackButton(findByDeadlinePanel, "FindByDeadTaskScreen");
        goBackButton(changeTaskPanel, "ChangeTaskScreen");

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
    
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        JButton[] buttons = {
            createButton("Pridėti užduotį", Color.GREEN, "AddTaskScreen"),
            createButton("Ištrinti užduotį pagal poziciją", Color.RED, "DelByPosScreen"),
            createButton("Ištrinti užduotį pagal pavadinimą", Color.BLUE, "DelByTitleScreen"),
            createButton("Parodyti visas užduotis", Color.PINK, "ShowTaskScreen"),
            createButton("Rasti užduotį pagal terminą", Color.ORANGE, "FindByDeadTaskScreen"),
            createButton("Rasti užduotį pagal pavadinimą", Color.MAGENTA, "FindByTitleTaskScreen"),
            createButton("Pakeisti surastą užduotį", Color.MAGENTA, "ChangeTaskScreen")
        };
    
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
    
        cardPanel.add(mainPanel, "MainScreen");
    }
    
    private JButton createButton(String text, Color backgroundColor, String cardName) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
    
        button.addActionListener(e -> cardLayout.show(cardPanel, cardName));
    
        return button;
    }
    

    public void printTasksUI() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
    
        JScrollPane scrollPane = new JScrollPane(textArea);
    
        printTaskPanel.add(scrollPane, BorderLayout.CENTER);
    
        textArea.setText("");
    
        for (Task task : Nuskaityti.taskList) {
            textArea.append("Pavadinimas: " + task.getTaskTitle() + "\n");
            textArea.append("Dalykas: " + task.getSubject() + "\n");
            textArea.append("Termino pabaiga: " + task.getdeadline() + "\n");
            textArea.append("Aprasymas: " + task.getDescription() + "\n");
            textArea.append("Papildoma informacija: " + task.getAdditionalInfo() + "\n\n");
        }
    
        cardPanel.add(printTaskPanel, "ShowTaskScreen");
    }

    public void deleteByPosUI(){
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

    public void deleteByTitleUI(){
        deleteTaskByTitlePanel.setLayout(new GridLayout(2, 1));
        
        taskTitleFieldDel = new JTextField(20);

        deleteTaskByTitlePanel.add(new JLabel("Uzduoties pavadinimas:"));
        deleteTaskByTitlePanel.add(taskTitleFieldDel);

        JButton deleteByTitle = new JButton("Istrinti");
        deleteByTitle.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = taskTitleFieldDel.getText();
            
            Nuskaityti.deleteTaskByTitle(title);
            
        }
        });

        deleteTaskByTitlePanel.add(deleteByTitle);

        cardPanel.add(deleteTaskByTitlePanel, "DelByTitleScreen");
    }

    public void addTaskUI(){
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
                    Nuskaityti.writeTasks();
                    
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

    public void findTaskByTitleUI(){
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
    

    public void findTaskByDeadlineUI(){
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

    public void changeTaskUI(){
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
            
            Nuskaityti.changeTask(changeTaskField.getText(), newSubject.getText(), newDeadline.getText(), newDescription.getText(), newAdditionalInfo.getText());
            Nuskaityti.writeTasks();
        }
        });

        changeTaskPanel.add(changeButton);

        cardPanel.add(changeTaskPanel, "ChangeTaskScreen");
    }

    private void goBackButton(JPanel panel, String cardName) {
        JButton goBackButton = new JButton("Grizti");
        goBackButton.addActionListener(e -> cardLayout.show(cardPanel, "MainScreen"));
        panel.add(goBackButton);
        cardPanel.add(panel, cardName);
    }


}