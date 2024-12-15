package pakiet.connection;

public class MessageHelper {
    private String message;

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void addBeforeMessage(String beforeMessage){
        this.message = beforeMessage + message;
    }

    public void addAfterMessage(String addMessage){
        this.message = message + addMessage;
    }
}
