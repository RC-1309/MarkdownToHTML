package md2html.markup;

import java.util.List;

public class Header extends Body {
    private static String symbolHtml;
    private static final int MAX_LEVEL = 6;
    private static final char HEADER_SYMBOL = '#';

    public static int getMaxLevel() {
        return MAX_LEVEL;
    }

    public static char getHeaderSymbol() {
        return HEADER_SYMBOL;
    }


    @Override
    protected String getHtmlSymbol() {
        return symbolHtml;
    }

    public Header(List<Elements> elements, int level) {
        super(elements);
        symbolHtml = "h" + level;
    }
}
