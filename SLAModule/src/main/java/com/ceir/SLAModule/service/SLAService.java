package com.ceir.SLAModule.service;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.SLAModule.App;
import com.ceir.SLAModule.model.constants.ConsignmentStatus;
import com.ceir.SLAModule.model.constants.GrievanceStatus;
import com.ceir.SLAModule.model.constants.RequestType;
import com.ceir.SLAModule.model.constants.StockStatus;

@Service
public class SLAService implements Runnable{

	@Autowired
	ConsignmentService consignmentService;
	
	@Autowired
	GrievanceService grievanceService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	TacApproveService tacApproveService;
	@Autowired
	StolenRecoveryService stolenRecService;
	
	@Autowired
	UserService userService;
	private final static Logger log =Logger.getLogger(App.class);
	
	@Override
	public void run() 
	{
		while(true) {
			log.info("inside sla report");
			consignmentService.consignmentProcess(ConsignmentStatus.PENDING_APPROVAL_FROM_CEIR_AUTHORITY.getCode());
			grievanceService.grievanceProcess(GrievanceStatus.PENDING_WITH_ADMIN.getCode());
			stockService.stockProcess(StockStatus.SUCCESS.getCode());			
			tacApproveService.tacProcess(3);
  // 	Stolen(0, "Stolen"), Recovery(1, "Recovery"), Block(2, "Block"),Unblock(3, "Unblock");
			stolenRecService.stolenRecProcess(3, RequestType.Stolen.getDescription(), 5, RequestType.Stolen.getCode(),"STOLEN_PEN_WITH_CEIR_ADMIN");
			stolenRecService.stolenRecProcess(3, RequestType.Recovery.getDescription(), 5, RequestType.Recovery.getCode(),"RECOVERY_PEN_WITH_CEIR_ADMIN");
			stolenRecService.stolenRecProcess(3, RequestType.Block.getDescription(), 7, RequestType.Block.getCode(),"BLOCK_PEN_WITH_CEIR_ADMIN");
			stolenRecService.stolenRecProcess(3, RequestType.Unblock.getDescription(), 7, RequestType.Unblock.getCode(),"UNBLOCK_PEN_WITH_CEIR_ADMIN");
			userService.userProcess(2);
			log.info("exit from sla report");
           System.exit(0);
//			try {
//				Thread.sleep(3600000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}










