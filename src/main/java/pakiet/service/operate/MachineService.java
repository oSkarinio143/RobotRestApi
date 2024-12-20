package pakiet.service.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pakiet.modules.robot.Machine;

import javax.crypto.Mac;

@Service
public class MachineService {
    private UserMenager userMenager;
    private OperationMenager operationMenager;

    @Autowired
    public MachineService(UserMenager userMenager, OperationMenager operationMenager){
        this.userMenager = userMenager;
        this.operationMenager = operationMenager;
    }

    public Machine createMachine(){
        return new Machine(userMenager, operationMenager);
    }
}
