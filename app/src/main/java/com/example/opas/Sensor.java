package com.example.opas;

public class Sensor {
    String Name;
    String Zone;
    String Id;
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

    public String getId() {return Id;}

    public void setId(String id) {Id = id;}

    public String getType() {return Type;}

    public void setType(String type) {Type = type;}
}
