package pakiet.connection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {
    @GetMapping("/api/robot")
    public String getRobots(){
        return "Lista robotów";
    }
}
