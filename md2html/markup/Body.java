package md2html.markup;

import java.util.List;

public abstract class Body {
    protected final List<Elements> elements;

    protected String getHtmlSymbol() {
        return "";
    }

    public Body(List<Elements> elements) {
        this.elements = List.copyOf(elements);
    }

    private void makeHtml(StringBuilder text, char type) {
        if (type == 'b') {
            text.append('<').append(getHtmlSymbol()).append('>');
        } else {
            text.append("</").append(getHtmlSymbol()).append('>');
        }
    }

    public void toHtml(StringBuilder text) {
        makeHtml(text, 'b');
        for (Elements object : this.elements) {
            object.toHtml(text);
        }
        makeHtml(text, 'e');
    }
}
