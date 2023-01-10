package com.example.opas;

public class Sensor {
    String Name;
    String Zone;
    String SingleValueId;
    String RangeValuesId;
    String Type;

    public Sensor() {}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getSingleValueId() {return SingleValueId;}

    public void setSingleValueId(String singleValueId) {SingleValueId = singleValueId;}

    public String getRangeValuesId() {return RangeValuesId;}

    public void setRangeValuesId(String rangeValuesId) {RangeValuesId = rangeValuesId;}

    public String getType() {return Type;}

    public void setType(String type) {Type = type;}
}
