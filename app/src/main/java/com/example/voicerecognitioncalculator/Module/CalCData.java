package com.example.voicerecognitioncalculator.Module;

public class CalCData {
    private String UniqueID,Operation;

    public  CalCData(){

    }

    public CalCData(String uniqueID, String operation) {
        UniqueID = uniqueID;
        Operation = operation;
    }

    public String getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(String uniqueID) {
        UniqueID = uniqueID;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }
}
