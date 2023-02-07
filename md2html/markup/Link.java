package md2html.markup;

import java.util.List;

public class Link extends MarkUp {
    private final String src;
    private static final String tegEnd = "a";
    private static final String tegStart = "a href";
    private static final String startSymbolForName = "[";
    private static final String endSymbolForName = "]";
    private static final String startSymbolForSrc = "(";
    private static final String endSymbolForSrc = ")";

    public static String getStartSymbolForName() {
        return startSymbolForName;
    }

    public static String getEndSymbolForName() {
        return endSymbolForName;
    }

    public static String getStartSymbolForSrc() {
        return startSymbolForSrc;
    }

    public static String getEndSymbolForSrc() {
        return endSymbolForSrc;
    }

    public Link(List<Elements> name, String src) {
        super(name);
        this.src = src;
    }

    @Override
    protected void makeHtml(StringBuilder text, char type) {
        if (type == 'b') {
            text.append('<').append(tegStart).append("='").append(src).append("'>");
        } else {
            text.append("</").append(tegEnd).append('>');
        }
    }
}
