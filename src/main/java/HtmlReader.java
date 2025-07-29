import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HtmlReader {
    private final String input;
    private final int length;
    private int pos = 0;

    private HtmlReader(String input) {
        this.input = input;
        this.length = input.length();
    }

    public static List<HtmlTag> read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int read;
        while ((read = in.read(buf)) != -1) {
            baos.write(buf, 0, read);
        }
        return read(baos.toByteArray());
    }

    public static List<HtmlTag> read(byte[] htmlBytes) {
        return read(new String(htmlBytes, StandardCharsets.UTF_8));
    }

    public static List<HtmlTag> read(String html) {
        HtmlReader reader = new HtmlReader(html);
        return reader.parseNodes();
    }

    private List<HtmlTag> parseNodes() {
        List<HtmlTag> nodes = new ArrayList<>();
        while (!eof()) {
            skipWhitespace();
            if (eof() || peekStartsWith("</")) {
                break;
            }
            if (peek() == '<') {
                nodes.add(parseTag());
            } else {
                // Skip stray text
                pos++;
            }
        }
        return nodes;
    }

    private HtmlTag parseTag() {
        expect('<');
        String name = parseTagName();
        HashMap<String, String> attrs = parseAttributes();
        skipWhitespace();

        boolean selfClosing = false;
        if (peekStartsWith("/>")) {
            selfClosing = true;
            pos += 2;
        } else {
            expect('>');
        }

        StringBuilder textContent = new StringBuilder();
        List<HtmlTag> children = new ArrayList<>();

        if (!selfClosing) {
            while (!eof() && !peekStartsWith("</")) {
                if (peek() == '<') {
                    // start of a child
                    children.add(parseTag());
                } else {
                    textContent.append(consumeUntil('<'));
                }
            }
            // consume closing tag
            expect('<');
            expect('/');
            String endName = parseTagName();
            // optionally check matching tags
            skipWhitespace();
            expect('>');
        }

        String content = children.isEmpty() ? textContent.toString().trim() : "";
        return new HtmlTagBuilder(name)
            .attributes(attrs)
            .selfClosing(selfClosing)
            .content(content)
            .children(children)
            .build();
    }

    private String parseTagName() {
        skipWhitespace();
        int start = pos;
        while (!eof()) {
            char c = peek();
            if (Character.isLetterOrDigit(c) || c == '-' || c == ':') {
                pos++;
            } else {
                break;
            }
        }
        return input.substring(start, pos);
    }

    private HashMap<String, String> parseAttributes() {
        HashMap<String, String> attrs = new HashMap<>();
        while (true) {
            skipWhitespace();
            if (eof() || peek() == '/' || peek() == '>') {
                break;
            }
            String key = parseTagName();
            String value = "";
            skipWhitespace();
            if (!eof() && peek() == '=') {
                pos++;
                skipWhitespace();
                if (peek() == '"' || peek() == '\'') {
                    char quote = peek();
                    pos++;
                    int start = pos;
                    while (!eof() && peek() != quote) {
                        pos++;
                    }
                    value = input.substring(start, pos);
                    expect(quote);
                } else {
                    int start = pos;
                    while (!eof() && !Character.isWhitespace(peek()) && peek() != '>' && peek() != '/') {
                        pos++;
                    }
                    value = input.substring(start, pos);
                }
            }
            attrs.put(key, value);
        }
        return attrs;
    }

    private String consumeUntil(char endChar) {
        int start = pos;
        while (!eof() && peek() != endChar) {
            pos++;
        }
        return input.substring(start, pos);
    }

    private void skipWhitespace() {
        while (!eof() && Character.isWhitespace(peek())) {
            pos++;
        }
    }

    private boolean peekStartsWith(String s) {
        return input.startsWith(s, pos);
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void expect(char c) {
        if (eof() || input.charAt(pos) != c) {
            throw new IllegalStateException("Expected '" + c + "' at position " + pos);
        }
        pos++;
    }

    private boolean eof() {
        return pos >= length;
    }
}