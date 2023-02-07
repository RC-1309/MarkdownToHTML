package md2html.markup;

public class Text implements Elements {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toHtml(StringBuilder text) {
        text.append(this.text);
    }
}
