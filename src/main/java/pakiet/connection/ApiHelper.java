package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiHelper {

    ApiHelper(){}

    public static ResponseEntity<Void> getResponseLocation(String location){
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("location", location)
                .build();
    }

    public static void setMessageOperationSuccessful(HttpSession session){
        session.setAttribute("message", "Operacja zostala zrealizowana poprawnie");
    }

    public static void setMessageOperationUnSuccessful(HttpSession session){
        session.setAttribute("message", "Operacja nie zostala zrealizowana");
    }
}
