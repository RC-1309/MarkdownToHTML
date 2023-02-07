package md2html.markup;

import java.util.List;

public class Emphasis extends MarkUp {
    protected static final String htmlSymbol = "em";

    public Emphasis(List<Elements> elements) {
        super(elements);
    }

    protected String getHtmlSymbol() {
        return htmlSymbol;
    }
}
