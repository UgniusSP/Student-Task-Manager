package nd;

public class Task {
        private String taskTitle;
        private String subject;
        private String deadline;
        private String description;
        private String additionalInfo;
    
        // Constructors
        public Task(String taskTitle, String subject, String deadline, String description, String additionalInfo) {
            this.taskTitle = taskTitle;
            this.subject = subject;
            this.deadline = deadline;
            this.description = description;
            this.additionalInfo = additionalInfo;
        }
    
        // Getters and setters
        public String getTaskTitle() {
            return taskTitle;
        }
    
        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }
    
        public String getSubject() {
            return subject;
        }
    
        public void setSubject(String subject) {
            this.subject = subject;
        }
    
        public String getdeadline() {
            return deadline;
        }
    
        public void setdeadline(String deadline) {
            this.deadline = deadline;
        }
    
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
    
        public String getAdditionalInfo() {
            return additionalInfo;
        }
    
        public void setAdditionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
        }
        public String toString() {
            return "Užduoties pavadinimas: " + taskTitle + "\n" +
                    "Dalykas: " + subject + "\n" + 
                    "Termino pabaiga: " + deadline + "\n" +
                    "Aprašymas: " + description + "\n" + 
                    "Papildoma info: " + additionalInfo + "\n";
        }
}
