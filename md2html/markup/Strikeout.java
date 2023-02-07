package md2html.markup;

import java.util.List;

public class Strikeout extends MarkUp {
    protected static final String htmlSymbol = "s";

    public Strikeout(List<Elements> elements) {
        super(elements);
    }

    protected String getHtmlSymbol() {
        return htmlSymbol;
    }
}
