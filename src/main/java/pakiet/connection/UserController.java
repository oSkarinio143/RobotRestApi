package pakiet.connection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/robot")
public class RobotController {
    @GetMapping
    public String enterUserNick(){
        return "Enter nick for user";    
    }

    @GetMapping("/{nick}")
    public String choseUserNick(@PathVariable("nick") String nick) {
        return "Witaj "+nick;
    }
}

