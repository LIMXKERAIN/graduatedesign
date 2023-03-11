package com.example.graduatedesign;

public class Line {
    // 直线的起点
    private Point startPoint;

    // 直线的终点
    private Point endPoint;

    // 方向
    private String direction;

    public Line(Point startPoint, Point endPoint, String direction) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Line{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", direction='" + direction + '\'' +
                '}';
    }
}

