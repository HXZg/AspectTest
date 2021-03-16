package com.xz.lib;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.lib  AspectTest
 * @Des Destination
 * @DATE 2020/8/7  15:05 ÐÇÆÚÎå
 */
public class Destination {

    private String className;
    private String intercept;
    private String pageUrl;
    private String control;
    private boolean isStart;
    private int id;
    private int type;

    public Destination() {}

    public Destination(String className, String intercept, String pageUrl, String control, boolean isStart, int id, int type) {
        this.className = className;
        this.intercept = intercept;
        this.pageUrl = pageUrl;
        this.control = control;
        this.isStart = isStart;
        this.id = id;
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIntercept() {
        return intercept;
    }

    public void setIntercept(String intercept) {
        this.intercept = intercept;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Destination) {
            Destination newDestination = (Destination) o;
            return newDestination.getId() == this.getId();
        }
        return super.equals(o);
    }
}
