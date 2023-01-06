package com.example.opas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RangeValues {

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
    @SerializedName("fromDate")
    private Object fromDate;
    @SerializedName("toDate")
    private Object toDate;
    @SerializedName("unit")
    private String unit;
    @SerializedName("values")
    private List<Object> values = null;

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

    public Object getFromDate() {
        return fromDate;
    }

    public void setFromDate(Object fromDate) {
        this.fromDate = fromDate;
    }

    public Object getToDate() {
        return toDate;
    }

    public void setToDate(Object toDate) {
        this.toDate = toDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
