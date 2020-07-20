package Client.Modes;

/**
 * Haichao Song
 * Description:
 * The enum class for all modes users can choose
 */
public enum Mode {

    LINE('l'),
    RECT('r'),
    OVAL('o'),
    TEXT('t'),
    FREEFORM_LINE('f');

    public final char mode;

    Mode(char val) { this.mode = val; }

}
