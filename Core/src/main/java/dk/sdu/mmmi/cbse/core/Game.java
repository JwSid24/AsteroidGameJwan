package dk.sdu.mmmi.cbse.core;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.ScoreSPI;
import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final List<IGamePluginService> plugins;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final ScoreSPI scoreSPI;

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new HashMap<>();

    private Pane gameWindow;
    private AnimationTimer loop;
    private boolean gameOver = false;
    private final List<Node> overlay = new ArrayList<>();

    public Game(List<IGamePluginService> plugins,
                List<IEntityProcessingService> entityProcessors,
                List<IPostEntityProcessingService> postProcessors,
                ScoreSPI scoreSPI) {
        this.plugins = plugins;
        this.entityProcessors = entityProcessors;
        this.postProcessors = postProcessors;
        this.scoreSPI = scoreSPI;
    }

    public void start(Stage stage) {
        gameData.setDisplayWidth(WIDTH);
        gameData.setDisplayHeight(HEIGHT);

        gameWindow = new Pane();
        gameWindow.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(gameWindow, WIDTH, HEIGHT, Color.BLACK);

        scene.setOnKeyPressed(e -> setKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> setKey(e.getCode(), false));

        for (IGamePluginService plugin : plugins) {
            plugin.start(gameData, world);
        }
        draw();

        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (IEntityProcessingService p : entityProcessors) {
                    p.process(gameData, world);
                }
                for (IPostEntityProcessingService p : postProcessors) {
                    p.process(gameData, world);
                }
                draw();

                if (playerIsDead()) {
                    stop();
                    showScoreboard();
                }
            }
        };

        stage.setOnCloseRequest(e -> {
            loop.stop();
            for (IGamePluginService plugin : plugins) {
                plugin.stop(gameData, world);
            }
        });

        stage.setTitle("AsteroidsFX");
        stage.setScene(scene);
        stage.show();
        loop.start();
    }

    private boolean playerIsDead() {
        for (Entity e : world.getEntities()) {
            if (e.getType() == Entity.Type.PLAYER) {
                return false;
            }
        }
        return true;
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygon.setFill(colorFor(entity.getType()));
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
        polygons.entrySet().removeIf(entry -> {
            boolean gone = !world.getEntities().contains(entry.getKey());
            if (gone) {
                gameWindow.getChildren().remove(entry.getValue());
            }
            return gone;
        });
    }

    private void showScoreboard() {
        if (gameOver) {
            return;
        }
        gameOver = true;

        double myScore = gameData.getScore();
        if (scoreSPI != null) {
            scoreSPI.submit(myScore);
        }

        VBox board = new VBox(14);
        board.setAlignment(Pos.CENTER);
        board.setPrefWidth(WIDTH);
        board.setLayoutX(0);
        board.setLayoutY(HEIGHT / 2.0 - 150);

        Text title = robotic("GAME OVER", 64, FontWeight.BOLD);
        title.setFill(Color.RED);

        board.getChildren().addAll(
                title,
                statRow("Enemies destroyed:", String.valueOf(gameData.getEnemiesKilled())),
                statRow("Asteroids destroyed:", String.valueOf(gameData.getAsteroidsKilled())),
                statRow("Your score:", String.format(Locale.US, "%.1f", myScore)),
                restartButton());

        showOverlay(board, scoringLegend());
    }

    private Text restartButton() {
        Text button = robotic("[ RESTART ]", 28, FontWeight.BOLD);
        button.setFill(Color.LIMEGREEN);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setFill(Color.WHITE));
        button.setOnMouseExited(e -> button.setFill(Color.LIMEGREEN));
        button.setOnMouseClicked(e -> restart());
        return button;
    }

    private void showOverlay(Node... nodes) {
        for (Node node : nodes) {
            overlay.add(node);
            gameWindow.getChildren().add(node);
        }
    }

    private void restart() {
        loop.stop();

        gameWindow.getChildren().removeAll(overlay);
        overlay.clear();

        for (Entity e : new ArrayList<>(world.getEntities())) {
            world.removeEntity(e);
        }
        gameWindow.getChildren().removeAll(polygons.values());
        polygons.clear();
        gameData.resetStats();

        for (IGamePluginService plugin : plugins) {
            plugin.start(gameData, world);
        }

        gameOver = false;
        draw();
        loop.start();
    }

    private VBox scoringLegend() {
        VBox legend = new VBox(4);
        legend.setLayoutX(20);
        legend.setLayoutY(20);
        legend.getChildren().addAll(
                legendLine("small asteroid  0.5 p"),
                legendLine("big asteroid    1 p"),
                legendLine("enemy           2 p"));
        return legend;
    }

    private Text legendLine(String text) {
        Text node = robotic(text, 16, FontWeight.NORMAL);
        node.setFill(Color.YELLOW);
        return node;
    }

    private HBox statRow(String label, String value) {
        Text labelNode = robotic(label + " ", 24, FontWeight.NORMAL);
        labelNode.setFill(Color.YELLOW);
        Text valueNode = robotic(value, 24, FontWeight.BOLD);
        valueNode.setFill(Color.LIMEGREEN);
        HBox row = new HBox(labelNode, valueNode);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    private Text robotic(String text, double size, FontWeight weight) {
        Text node = new Text(text);
        node.setFont(Font.font("Monospaced", weight, size));
        return node;
    }

    private Color colorFor(Entity.Type type) {
        return switch (type) {
            case PLAYER -> Color.web("#0041C2");
            case ENEMY -> Color.RED;
            case ASTEROID -> Color.GRAY;
            case BULLET -> Color.WHITE;
        };
    }

    private void setKey(KeyCode code, boolean pressed) {
        switch (code) {
            case LEFT -> gameData.getKeys().setKey(GameKeys.LEFT, pressed);
            case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, pressed);
            case UP -> gameData.getKeys().setKey(GameKeys.UP, pressed);
            case DOWN -> gameData.getKeys().setKey(GameKeys.DOWN, pressed);
            case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, pressed);
            default -> {  }
        }
    }
}
