package nd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.fasterxml.jackson.databind.util.JSONPObject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
//import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Iterator;



public class Nuskaityti {

    public static LinkedList<Task> taskList = new LinkedList<>();
    public static LinkedList <Task> queue = new LinkedList<>();
    private static LinkedList <Task> taskListCopy = new LinkedList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final File file = new File("src/main/java/nd/tasks.json");
    public static String deadlineToReturn;

    public static void main(String[] args) throws ParseException {
        //sortTasksByDeadline();
    }

    public static void readTasks(){
        try {
            JsonNode jsonNode = objectMapper.readTree(new File("src\\main\\java\\nd\\tasks.json"));

            if (jsonNode.isArray() && jsonNode != null) {
                for (JsonNode taskNode : jsonNode) {
                    Task task = new Task(
                            taskNode.get("taskTitle").asText(),
                            taskNode.get("subject").asText(),
                            taskNode.get("deadline").asText(),
                            taskNode.get("description").asText(),
                            taskNode.get("additionalInfo").asText()
                    );
                    taskList.add(task);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTasks(){
        ArrayNode tasksArray = JsonNodeFactory.instance.arrayNode();
        taskListCopy = (LinkedList<Task>) taskList.clone();
        wipeList(taskList);
            for(Task task : taskListCopy){
                ObjectNode taskNode = JsonNodeFactory.instance.objectNode();
                taskNode.put("taskTitle", task.getTaskTitle());
                taskNode.put("subject", task.getSubject());
                taskNode.put("deadline", task.getdeadline());
                taskNode.put("description", task.getDescription());
                taskNode.put("additionalInfo", task.getAdditionalInfo());
                tasksArray.add(taskNode);
            }  

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasksArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTaskInFileByPos(int position){
        try {
            ArrayNode tasksArray = objectMapper.readValue(file, ArrayNode.class);
    
            if(position >= 0 && tasksArray.size() > position){
                tasksArray.remove(position);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasksArray);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    public static void deleteTaskInFileTitle(String title){
        try {
            ArrayNode tasksArray = objectMapper.readValue(file, ArrayNode.class);
    
            for (int i = 0; i < tasksArray.size(); i++) {
                ObjectNode taskNode = (ObjectNode) tasksArray.get(i);
                if (taskNode.get("taskTitle").asText().equals(title)) {
                    tasksArray.remove(i);
                    break; 
                }
            }
    
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasksArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public static void printTasks(){
    //     readTasks();
    //     wipeList(taskList);
    // }

    public static void addTask(int pos, String title, String subject, String deadline, String description, String additional) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        readTasks();
        //NAUJU UZDUOCIU IDEJIMAS I LINKED LISTA

        Task task = new Task(
            title,
            subject,
            deadline,
            description,
            additional
            );
        Date date1 = simpleDateFormat.parse(deadline); //deadline
        Date date2 = simpleDateFormat.parse("2023-11-08"); //siandien

        if(date2.before(date1)){ //jeigu deadline pvz 2023-11-01, neleisti prideti
            taskList.add(pos, task);
        } else {
            System.out.println("Uzduoties terminas jau pasibaiges! Uzduotis neprideta.");
        }
        
        writeTasks();
    }

    public static void deleteTaskByTitle(String title){
        Iterator <Task> iterator = taskList.iterator();

            while (iterator.hasNext()) {
                Task task = iterator.next();
                
                if (title.equals(task.getTaskTitle())) {
                    iterator.remove(); 
                    System.out.println("\nUzduotis " + title + " sekmingai pasalinta!\n");
                    break;
                } else {
                    System.out.println("\nBlogai ivesta uzduotis" + title + "\n");
                }
            }
    }


    public static void deleteTaskByPos(int pos){
        readTasks();
        taskList.remove(pos);
        wipeList(taskList);
    }

    public static void findTaskByTitle(String title){
        readTasks();
        Iterator <Task> iterator = taskList.iterator();

            while(iterator.hasNext()){
                Task task = iterator.next();
                if(title.equals(task.getTaskTitle())){
                Task temp = new Task(
                        task.getTaskTitle(),
                        task.getSubject(),
                        task.getdeadline(),
                        task.getDescription(),
                        task.getAdditionalInfo()
                        );
                queue.add(temp);
                } 
            }
        wipeList(taskList);
    }

    public static void findTaskByDeadline(String deadline){
        readTasks();
        Iterator <Task> iterator = taskList.iterator();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        //Date date1 = simpleDateFormat.parse(deadline);

            while(iterator.hasNext()){
                Task task = iterator.next();
                //Date date2 = simpleDateFormat.parse(task.getdeadline());
                //System.out.println(deadline);
                if(deadline.equals(task.getdeadline())){
                Task temp = new Task(
                        task.getTaskTitle(),
                        task.getSubject(),
                        task.getdeadline(),
                        task.getDescription(),
                        task.getAdditionalInfo()
                        );
                queue.add(temp);
                } else {
                    System.out.println("aaaaaaaaaaaaaaaaaa");
                }
            }
            wipeList(taskList);
    }

    public static String closestDeadline(){
        readTasks();
        Date smallestDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        
        for(Task task : taskList){
            Date taskDate;
            try {
                taskDate = simpleDateFormat.parse(task.getdeadline());
            } catch (ParseException e) {
                e.printStackTrace();
                taskDate = null;
            }
            if(taskDate != null){
                if(smallestDate == null || taskDate.before(smallestDate)){
                    smallestDate = taskDate;
                } 
            }
            
        }
        wipeList(taskList);
        System.out.println(new SimpleDateFormat("yyyy-mm-dd").format(smallestDate));
        return new SimpleDateFormat("yyyy-mm-dd").format(smallestDate);
    }

    public static void wipeList(LinkedList <Task> list){
        taskList.removeAll(list);
    }

    public static Task taskInQueue = null;
    public static Task taskInList = null;

    public static void changeTask(String taskToUpdateTitle, String newSubject, String newDeadline, String newDescription, String newAdditionalInfo){
        readTasks();
        
        for (Task task : queue) {
            if (task.getTaskTitle().equals(taskToUpdateTitle)) {
                taskInQueue = task;
                break;
            }
        }

        for (Task task : taskList) {
            if (task.getTaskTitle().equals(taskToUpdateTitle)) {
                taskInList = task;
                break;
            }
        }

        if (taskInQueue != null && taskInList != null) {
            taskInQueue.setSubject(newSubject);
            taskInQueue.setdeadline(newDeadline);
            taskInQueue.setDescription(newDescription);
            taskInQueue.setAdditionalInfo(newAdditionalInfo);

            taskInList.setSubject(newSubject);
            taskInList.setdeadline(newDeadline);
            taskInList.setDescription(newDescription);
            taskInList.setAdditionalInfo(newAdditionalInfo);
        } else {
            System.out.println("Uzduotis nerasta.");
        }
        writeTasks();
    }
    
    public static void sortTasksByDeadline(){
        readTasks();
        
        Collections.sort(taskList, Comparator.comparing(task -> LocalDate.parse(task.getdeadline(), DateTimeFormatter.ISO_DATE)));
        //System.out.println(taskList);
        
    }
     
}   
