package md2html.markup;

import java.util.List;

public class Paragraph extends Body {
    private static final String symbolHtml = "p";

    @Override
    protected String getHtmlSymbol() {
        return symbolHtml;
    }

    public Paragraph(List<Elements> elements) {
        super(elements);
    }
}
