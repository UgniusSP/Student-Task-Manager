package nd;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.fasterxml.jackson.databind.util.JSONPObject;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import com.fasterxml.jackson.databind.JsonNode;
//import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Iterator;



public class Nuskaityti {

    public static LinkedList<Task> taskList = new LinkedList<>();
    public static LinkedList <Task> queue = new LinkedList<Task>();
    public static void main(String[] args) throws ParseException {
        
            readTasks();
            //addTask();
            //deleteTask();
            //findTask();
            //wipeList();
            //changeTask();   
            //printTasks(taskList);
            writeTasks();
        
    }

    public static void writeTasks(){
        ArrayNode tasksArray = JsonNodeFactory.instance.arrayNode();
            for(Task task : taskList){
                ObjectNode taskNode = JsonNodeFactory.instance.objectNode();
                taskNode.put("taskTitle", task.getTaskTitle());
                taskNode.put("subject", task.getSubject());
                taskNode.put("dueDate", task.getdeadline());
                taskNode.put("description", task.getDescription());
                taskNode.put("additionalInfo", task.getAdditionalInfo());
                tasksArray.add(taskNode);
            }
            
            ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("src\\main\\java\\nd\\tasks.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, tasksArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTaskInFile(int position){
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src\\main\\java\\nd\\tasks.json");
    
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
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src\\main\\java\\nd\\tasks.json");
    
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

    public static void readTasks(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("src\\main\\java\\nd\\tasks.json"));

            if (jsonNode.isArray() && jsonNode != null) {
                for (JsonNode taskNode : jsonNode) {
                    Task task = new Task(
                            taskNode.get("taskTitle").asText(),
                            taskNode.get("subject").asText(),
                            taskNode.get("dueDate").asText(),
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

    public static void printTasks(LinkedList <Task> list){
        for (Task task : list) {
            System.out.println("Pavadinimas: " + task.getTaskTitle());
            System.out.println("Dalykas: " + task.getSubject());
            System.out.println("Termino pabaiga: " + task.getdeadline());
            System.out.println("Aprasymas: " + task.getDescription());
            System.out.println("Papildoma informacija: " + task.getAdditionalInfo());
            System.out.println();
        }
    }

    public static void addTask(int pos, String title, String subject, String deadline, String description, String additional) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        
        //IDEJIMAS I LINKED LISTA

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
        taskList.remove(pos);
    }

    public static void findTaskByTitle(String title){
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
    }

    public static void findTaskByDeadline(String deadline){
        Iterator <Task> iterator = taskList.iterator();

            while(iterator.hasNext()){
                Task task = iterator.next();
                if(deadline.equals(task.getdeadline())){
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
    }

    public static void wipeList(){
        taskList.removeAll(taskList);
    }

    public static void changeTask(String taskToUpdateTitle, String newSubject, String newDeadline, String newDescription, String newAdditionalInfo){

        Task taskInQueue = null;
        for (Task task : queue) {
            if (task.getTaskTitle().equals(taskToUpdateTitle)) {
                taskInQueue = task;
                break;
            }
        }

        Task taskInList = null;
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
        
    }
    
    public static void sortTasksByDeadline(){
        
    }
     
}   
