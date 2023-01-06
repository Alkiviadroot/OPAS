package com.example.opas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleValue {
    @SerializedName("widgetName")
    private String widgetName;
    @SerializedName("widgetType")
    private Integer widgetType;
    @SerializedName("widgetsId")
    private Integer widgetsId;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private Object icon;
    @SerializedName("value")
    private Double value;
    @SerializedName("imageIcon")
    private String imageIcon;
    @SerializedName("unit")
    private String unit;
    @SerializedName("lastUpdate")
    private String lastUpdate;
    @SerializedName("rules")
    private List<Object> rules = null;

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public Integer getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(Integer widgetType) {
        this.widgetType = widgetType;
    }

    public Integer getWidgetsId() {
        return widgetsId;
    }

    public void setWidgetsId(Integer widgetsId) {
        this.widgetsId = widgetsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Object> getRules() {
        return rules;
    }

    public void setRules(List<Object> rules) {
        this.rules = rules;
    }
}
