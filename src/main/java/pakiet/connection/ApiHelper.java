package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiHelper {

    public ResponseEntity<Void> getResponseLocation(String location){
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("location", location)
                .build();
    }

    public void setMessageOperationSuccessful(HttpSession session){
        session.setAttribute("message", "Operacja zostala zrealizowana poprawnie");
    }

    public void setMessageOperationUnSuccessful(HttpSession session){
        session.setAttribute("message", "Operacja nie zostala zrealizowana");
    }
}
