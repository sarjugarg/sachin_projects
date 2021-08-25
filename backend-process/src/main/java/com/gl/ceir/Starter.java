package com.gl.ceir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.factory.service.Service;
import com.gl.ceir.factory.service.impl.BlockEndUserDevice;
import com.gl.ceir.factory.service.impl.CloseGrievance;
import com.gl.ceir.factory.service.impl.ConsignmentRevenueService;
import com.gl.ceir.factory.service.impl.DeviceTaxReminder;
import com.gl.ceir.factory.service.impl.FindUserReg;
import com.gl.ceir.factory.service.impl.RemoveFromBlacklist;
import com.gl.ceir.factory.service.impl.RemoveIncompleteUser;
import com.gl.ceir.factory.service.impl.VisaExpire;

@Component
public class Starter {

	@Autowired
	DeviceTaxReminder deviceTaxReminder;
	
	@Autowired
	VisaExpire visaExpire;
	
	@Autowired
	ConsignmentRevenueService consignmentRevenueService;
	
	@Autowired
	CloseGrievance closeGrievance;
	
	@Autowired
	BlockEndUserDevice blockEndUserDevice;
	
	@Autowired
	RemoveIncompleteUser removeIncompleteUser;
	
	@Autowired
	FindUserReg findUserReg;
   
   @Autowired
	RemoveFromBlacklist removeFromBlacklist;
   
	
	public Service start(String name) {
		
		switch (name) {
		case ProcessName.REGISTERED_DEVICE_TAX_NOT_PAID_REMINDER:
			return deviceTaxReminder;
		case ProcessName.VISA_EXPIRE:
			return visaExpire;
		case ProcessName.CONSIGNMENT_REVENUE:
			return consignmentRevenueService;
		case ProcessName.CLOSE_GRIEVANCE:
			return closeGrievance;
		case ProcessName.BLOCK_END_USER_DEVICE:
			return blockEndUserDevice;
		case ProcessName.REMOVE_IN_COMPLETE_USER:
			return removeIncompleteUser;
		case ProcessName.FIND_USER_REG:
			return findUserReg;
         case ProcessName.REMOVE_FROM_BLACKLIST:
			return removeFromBlacklist;
		default:
			return null;
		}
	}
}






