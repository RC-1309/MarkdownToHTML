package md2html;
import md2html.markup.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {
    private static final String LINE_FEED = System.lineSeparator();
    private static final String FICTITIOUS_ELEMENT = "#";
    private static final char DASH = '\\';
    private static final int MAX_SIZE_MARK_UP_SYMBOL = 2;
    private static final Set<String> MARK_UP_SYMBOLS = Set.of(
            "*", "--", "_", "`", "__", "**", Character.toString(DASH),
            Link.getStartSymbolForName(), Link.getEndSymbolForName()
    );
    private static final Map<String, String> ACCORDANCE = Map.of(
            "*", "*",
            "--", "--",
            "_", "_",
            "`", "`",
            "__", "__",
            "**", "**",
            Link.getEndSymbolForName(), Link.getStartSymbolForName()
    );

    private static final Map<String, String> SPECIAL_SYMBOLS = Map.of(
            "<", "&lt;",
            ">", "&gt;",
            "&", "&amp;"
    );

    private static String getMarkUpSymbol(String text) {
        for (int i = Integer.min(MAX_SIZE_MARK_UP_SYMBOL, text.length()); i > 0; i--) {
            String symbols = text.substring(0, i);
            if (checkMarkUpSymbol(symbols)) {
                return symbols;
            }
        }
        return "";
    }

    private static boolean checkMarkUpSymbol(String symbol) {
        return MARK_UP_SYMBOLS.contains(symbol);
    }

    private static String checkAndReplaceSpecialSymbol(String symbol) {
        String newSymbol = SPECIAL_SYMBOLS.get(symbol);
        return newSymbol == null ? symbol : newSymbol;
    }

    private static Elements getClass(List<Elements> list, String src, String markUp) {
        return switch (markUp) {
            case "*", "_" -> new Emphasis(list);
            case "__", "**" -> new Strong(list);
            case "--" -> new Strikeout(list);
            case "`" -> new Code(list);
            case "[" -> new Link(list, src);
            default -> null;
        };
    }

    private static int getHeaderLevel(String text) {
        int posInText = 0;
        while (posInText < text.length() && text.charAt(posInText) == Header.getHeaderSymbol()) {
            posInText++;
        }

        if (posInText < text.length() && Character.isWhitespace(text.charAt(posInText)) && posInText > 0) {
            return posInText;
        } else {
            return -1;
        }
    }

    private static int getPosPairSymbol(String text, int startPos, String symbol) {
        int pos = startPos;
        while (pos < text.length() && !symbol.equals(text.substring(pos, pos + symbol.length()))) {
            pos++;
        }
        return pos;
    }

    private static List<Elements> parse(String text, int posStart) {
        Stack<List<Elements>> html = new Stack<>();
        html.add(new ArrayList<>());
        Stack<String> markUpSymbols = new Stack<>();
        markUpSymbols.add(FICTITIOUS_ELEMENT);
        StringBuilder currentText = new StringBuilder();

        for (int i = posStart; i < text.length() - LINE_FEED.length(); i++) {
            String markUp = getMarkUpSymbol(text.substring(i,
                    Integer.min(i + MAX_SIZE_MARK_UP_SYMBOL, text.length())));
            while (i < text.length() - LINE_FEED.length() && markUp.isEmpty()) {
                currentText.append(checkAndReplaceSpecialSymbol(Character.toString(text.charAt(i))));
                i++;
                markUp = getMarkUpSymbol(text.substring(i, Integer.min(i + MAX_SIZE_MARK_UP_SYMBOL, text.length())));
            }

            if (markUp.equals(Character.toString(DASH)) && checkMarkUpSymbol(Character.toString(text.charAt(i + 1)))) {
                currentText.append(text.charAt(i + 1));
                i++;
                continue;
            }

            html.peek().add(new Text(currentText.toString()));
            currentText.setLength(0);
            i += markUp.length() - 1;

            if (markUpSymbols.peek().equals(ACCORDANCE.get(markUp))) {
                String src = "";
                if (markUp.equals(Link.getEndSymbolForName())) {
                    int posStartLinkSrc = i + 2;
                    i = getPosPairSymbol(text, posStartLinkSrc, Link.getEndSymbolForSrc());
                    src = text.substring(posStartLinkSrc, i);
                }

                Elements newElem = getClass(html.peek(), src, markUpSymbols.peek());
                html.pop();
                if (html.isEmpty()) {
                    html.add(new ArrayList<>());
                }
                html.peek().add(newElem);
                markUpSymbols.pop();
            } else {
                markUpSymbols.add(markUp);
                html.add(new ArrayList<>());
            }
        }

        while (html.size() > 1) {
            List<Elements> elements = html.pop();
            html.peek().add(new Text(markUpSymbols.peek()));
            html.peek().addAll(elements);
            markUpSymbols.pop();
        }
        return html.peek();
    }

    private static String stringToHtml(String text) throws IOException {
        if (text.isEmpty()) {
            return "";
        }
        int headerLevel = getHeaderLevel(text.substring(0, Integer.min(text.length(), Header.getMaxLevel() + 1)));
        List<Elements> elements = parse(text, headerLevel + 1);

        StringBuilder htmlText = new StringBuilder();
        if (headerLevel > 0) {
            new Header(elements, headerLevel).toHtml(htmlText);
        } else {
            new Paragraph(elements).toHtml(htmlText);
        }
        return htmlText.append(LINE_FEED).toString();
    }

    public static void main(String[] args) {
        try {
            List<String> htmlText = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(args[0]),
                    StandardCharsets.UTF_8
            ))) {
                String line = reader.readLine();
                StringBuilder currentText = new StringBuilder();
                while (line != null) {
                    while (line != null && !line.isEmpty()) {
                        currentText.append(line).append(LINE_FEED);
                        line = reader.readLine();
                    }
                    htmlText.add(stringToHtml(currentText.toString()));
                    currentText.setLength(0);
                    line = reader.readLine();
                }
            }
            try {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                        StandardCharsets.UTF_8
                ))) {
                    for (String text : htmlText) {
                        writer.write(text);
                    }
                }
            } catch (IOException e) {
                System.out.println("Output file: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }
    }
}
