package pakiet.connection;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pakiet.service.operate.OperationMenager;
import pakiet.service.operate.SellerMenager;

@RestController
@RequestMapping("/robot/")
public class SellerOptionController {
    private int chosedOperation;

    private final OperationMenager operationMenager;
    private final ApiHelper apiHelper;
    private final StringHelper stringHelper;
    private final SellerMenager sellerMenager;

    public SellerOptionController(OperationMenager operationMenager, ApiHelper apiHelper, StringHelper stringHelper, SellerMenager sellerMenager){
        this.operationMenager = operationMenager;
        this.apiHelper = apiHelper;
        this.stringHelper = stringHelper;
        this.sellerMenager = sellerMenager;
    }

    @GetMapping("{nick}/seller/")
    public String showSellerOperation (@PathVariable String nick){
        return stringHelper.getSellerOperationChoice();
    }

    @GetMapping("{nick}/seller/{choiceOperation}")
    public ResponseEntity<Void> choseSellerOperation (@PathVariable String nick, @PathVariable int choiceOperation, HttpSession session){
        chosedOperation = choiceOperation;
        switch (choiceOperation){
            case 1,3,4:
                return apiHelper.getResponseLocation("/robot/" + nick + "/seller/id/");
            case 2:
                operationMenager.earnGold();
                apiHelper.setMessageOperationSuccessful(session);
                return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "/seller/");
        }
    }

    @GetMapping("{nick}/seller/id/")
    public String showSellerId(){
        return "Podaj id sellera: " + sellerMenager.returnIdsList();
    }

    @GetMapping("{nick}/seller/id/{userId}")
    public ResponseEntity<Void> choseSellerIdOperation (@PathVariable String nick, @PathVariable int userId, HttpSession session){
        if(sellerMenager.findSellerById(userId).isEmpty()){
            return apiHelper.getResponseLocation("/robot/" + nick + "/seller/");
        }
        switch (chosedOperation){
            case 1:
                String message = stringHelper.getShowSeller(userId);
                session.setAttribute("message", message);
                break;

            case 3:
                if(operationMenager.upgradeSeller(userId))
                    apiHelper.setMessageOperationSuccessful(session);
                else
                    apiHelper.setMessageOperationUnSuccessful(session);

                break;

            case 4:
                operationMenager.sellSeller(userId);
                apiHelper.setMessageOperationSuccessful(session);
                break;
            default:
                return apiHelper.getResponseLocation("/robot/" + nick + "/seller/");
        }
        return apiHelper.getResponseLocation("/robot/" + nick + "/type/");
    }
}
