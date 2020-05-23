package Client;

/**
 * Haichao Song
 * Description:
 */
public enum Mode {


    LINE('l'),

    RECT('r'),

    OVAL('o'),

    TEXT('t'),

    FREEFORM_LINE('f');

    public final char mode;
    Mode(char val) { this.mode = val; }

    @Override
    public String toString()
    {
        String lowerCase = this.name().toLowerCase();
        lowerCase = lowerCase.replaceAll("_", " ");
        String[] words = lowerCase.split("\\s+");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            name.append(words[i].substring(0, 1).toUpperCase());
            name.append(words[i].substring(1));
            if (i+1 < words.length)
                name.append(" ");
        }

        return name.toString();
    }

}
