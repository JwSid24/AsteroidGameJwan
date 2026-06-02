package dk.sdu.mmmi.cbse.common.data;

import java.util.UUID;

public class Entity {

    public enum Type { PLAYER, ENEMY, ASTEROID, BULLET }

    public enum Size { LARGE, SMALL }

    private final String id = UUID.randomUUID().toString();

    private Type type;
    private double[] polygonCoordinates;
    private double x;
    private double y;
    private double rotation;
    private double dx;
    private double dy;
    private double radius;

    private int hitsTaken = 0;

    private Size size = Size.LARGE;

    private Type firedBy;

    private int shootCooldown = 0;
    private int actionTimer = 0;

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getHitsTaken() {
        return hitsTaken;
    }

    public void addHit() {
        this.hitsTaken++;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Type getFiredBy() {
        return firedBy;
    }

    public void setFiredBy(Type firedBy) {
        this.firedBy = firedBy;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(int shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public int getActionTimer() {
        return actionTimer;
    }

    public void setActionTimer(int actionTimer) {
        this.actionTimer = actionTimer;
    }
}
