package md2html.markup;

import java.util.List;

public class Strong extends MarkUp {
    protected static final String htmlSymbol = "strong";

    public Strong(List<Elements> elements) {
        super(elements);
    }

    protected String getHtmlSymbol() {
        return htmlSymbol;
    }
}
