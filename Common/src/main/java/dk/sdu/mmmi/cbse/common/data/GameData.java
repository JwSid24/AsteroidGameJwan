package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private final GameKeys keys = new GameKeys();
    private int displayWidth = 800;
    private int displayHeight = 600;

    private int enemiesKilled = 0;
    private int asteroidsKilled = 0;
    private double score = 0;

    public GameKeys getKeys() {
        return keys;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public void addEnemyKill(double points) {
        enemiesKilled++;
        score += points;
    }

    public void addAsteroidKill(double points) {
        asteroidsKilled++;
        score += points;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getAsteroidsKilled() {
        return asteroidsKilled;
    }

    public double getScore() {
        return score;
    }

    public void resetStats() {
        enemiesKilled = 0;
        asteroidsKilled = 0;
        score = 0;
    }
}
