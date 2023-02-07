package md2html.markup;

import java.util.List;

public class Code extends MarkUp {
    protected static final String htmlSymbol = "code";

    public Code(List<Elements> elements) {
        super(elements);
    }

    protected String getHtmlSymbol() {
        return htmlSymbol;
    }
}
