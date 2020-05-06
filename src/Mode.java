/**
 * Haichao Song
 * Description:
 */
public enum Mode {

    LINE('l'),
    TEXT('t'),
    FREE('f');

    public char mode;
    Mode(char val) { this.mode = val; }

    public static Mode parseChar(char c) {
        for (Mode m : Mode.values()) {
            if (m.mode == c)
                return m;
        }
        System.out.printf("Unknown DrawType '%c'%n", c);
        return null;
    }

}
