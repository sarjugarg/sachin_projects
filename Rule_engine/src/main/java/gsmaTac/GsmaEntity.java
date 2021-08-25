/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gsmaTac;

/**
 *
 * @author user
 */
import java.util.Arrays;



  
//@Entity    
public class  GsmaEntity {    

//@Id
//@GeneratedValue
private int id ;

//@Column
private int status;


//@Column
private String statusCode,  statusMessage, deviceId, brandName ,modelName,internalModelName,marketingName,equipmentType,simSupport,nfcSupport ,wlanSupport, blueToothSupport ,lpwan  ,manufacturer ,tacApprovedDate ,gsmaApprovedTac  ,created_on , updated_on ;

private String [] deviceCertifybody,radioInterface,operatingSystem;

    public String getDeviceCertifybody() {
        return  Arrays.toString(deviceCertifybody);
    }

    public void setDeviceCertifybody(String[] deviceCertifybody) {
        this.deviceCertifybody = deviceCertifybody;
    }

    public String getRadioInterface() {
        return     Arrays.toString(radioInterface);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setRadioInterface(String[] radioInterface) {
        this.radioInterface = radioInterface;
    }

    public String getOperatingSystem() {
        return     Arrays.toString(operatingSystem);
    }

    public void setOperatingSystem(String[] operatingSystem) {
        this.operatingSystem = operatingSystem;
    }




public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}



public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

public String getStatusMessage() {
	return statusMessage;
}

public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}

public String getDeviceId() {
	return deviceId;
}

public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
}

public String getBrandName() {
	return brandName;
}

public void setBrandName(String brandName) {
	this.brandName = brandName;
}

public String getModelName() {
	return modelName;
}

public void setModelName(String modelName) {
	this.modelName = modelName;
}

public String getInternalModelName() {
	return internalModelName;
}

public void setInternalModelName(String internalModelName) {
	this.internalModelName = internalModelName;
}

public String getMarketingName() {
	return marketingName;
}

public void setMarketingName(String marketingName) {
	this.marketingName = marketingName;
}

public String getEquipmentType() {
	return equipmentType;
}

public void setEquipmentType(String equipmentType) {
	this.equipmentType = equipmentType;
}

public String getSimSupport() {
	return simSupport;
}

public void setSimSupport(String simSupport) {
	this.simSupport = simSupport;
}

public String getNfcSupport() {
	return nfcSupport;
}

public void setNfcSupport(String nfcSupport) {
	this.nfcSupport = nfcSupport;
}

public String getWlanSupport() {
	return wlanSupport;
}

public void setWlanSupport(String wlanSupport) {
	this.wlanSupport = wlanSupport;
}

public String getBlueToothSupport() {
	return blueToothSupport;
}

public void setBlueToothSupport(String blueToothSupport) {
	this.blueToothSupport = blueToothSupport;
}

public String getLpwan() {
	return lpwan;
}

public void setLpwan(String lpwan) {
	this.lpwan = lpwan;
}

public String getManufacturer() {
	return manufacturer;
}

public void setManufacturer(String manufacturer) {
	this.manufacturer = manufacturer;
}

public String getTacApprovedDate() {
	return tacApprovedDate;
}

public void setTacApprovedDate(String tacApprovedDate) {
	this.tacApprovedDate = tacApprovedDate;
}

public String getGsmaApprovedTac() {
	return gsmaApprovedTac;
}

public void setGsmaApprovedTac(String gsmaApprovedTac) {
	this.gsmaApprovedTac = gsmaApprovedTac;
}

public String getCreated_on() {
	return created_on;
}

public void setCreated_on(String created_on) {
	this.created_on = created_on;
}

public String getUpdated_on() {
	return updated_on;
}

public void setUpdated_on(String updated_on) {
	this.updated_on = updated_on;
}






}   



//public String getOperatingSystem() {    //[]
//	return Arrays.toString(operatingSystem);
//}
////Arrays.toString(boolArr));
//
//public void setOperatingSystem(String[] operatingSystem) {
//	this.operatingSystem = operatingSystem;
//}
//
//public String getRadioInterface() {   //[]
////	return radioInterface;
//	return Arrays.toString(radioInterface);
//}
//
//public void setRadioInterface(String[] radioInterface) {
//	this.radioInterface = radioInterface;
//}
//
//public String getDeviceCertifybody() {   //[]
////	return deviceCertifybody;
//	return Arrays.toString(deviceCertifybody);
//}
//
//public void setDeviceCertifybody(String[] deviceCertifybody) {
//	this.deviceCertifybody = deviceCertifybody;
//}
//
//

