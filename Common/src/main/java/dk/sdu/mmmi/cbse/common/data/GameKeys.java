package dk.sdu.mmmi.cbse.common.data;

public class GameKeys {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int SPACE = 4;

    private static final int KEY_COUNT = 5;

    private final boolean[] pressed = new boolean[KEY_COUNT];

    public void setKey(int key, boolean isPressed) {
        pressed[key] = isPressed;
    }

    public boolean isDown(int key) {
        return pressed[key];
    }
}
