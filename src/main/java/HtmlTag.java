import java.util.HashMap;
import java.util.List;

public record HtmlTag(
    String tagName,
    String content,
    List<HtmlTag> children,
    HashMap<String, String> attributes,
    boolean selfClosing
) {
    public String getAttribute(String key) {
        return attributes.getOrDefault(key, "");
    }

    public int getAttributeAsInt(String key) {
        String value = attributes.get(key);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public double getAttributeAsDouble(String key) {
        String value = attributes.get(key);
        return value != null ? Double.parseDouble(value) : 0.0;
    }

    public boolean getAttributeAsBoolean(String key) {
        String value = attributes.get(key);
        return value != null && (value.equalsIgnoreCase("true") || value.equals("1"));
    }

    public HtmlTag withAttribute(String key, String value) {
        HashMap<String, String> newAttributes = new HashMap<>(attributes);
        newAttributes.put(key, value);
        return new HtmlTag(tagName, content, children, newAttributes, selfClosing);
    }

    public HtmlTag withAttribute(String key, int value) {
        return withAttribute(key, String.valueOf(value));
    }

    public HtmlTag withAttribute(String key, double value) {
        return withAttribute(key, String.valueOf(value));
    }

    public HtmlTag withAttribute(String key, boolean value) {
        return withAttribute(key, value ? "true" : "false");
    }

    public HtmlTag withAttributes(HashMap<String, String> newAttributes) {
        HashMap<String, String> combinedAttributes = new HashMap<>(attributes);
        combinedAttributes.putAll(newAttributes);
        return new HtmlTag(tagName, content, children, combinedAttributes, selfClosing);
    }

    public HtmlTag withContent(String newContent) {
        return new HtmlTag(tagName, newContent, children, attributes, selfClosing);
    }

    public HtmlTag withChildren(List<HtmlTag> newChildren) {
        return new HtmlTag(tagName, content, newChildren, attributes, selfClosing);
    }

    public HtmlTag withSelfClosing(boolean selfClosing) {
        return new HtmlTag(tagName, content, children, attributes, selfClosing);
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public static HtmlTag empty(String tagName) {
        return new HtmlTag(tagName, "", List.of(), new HashMap<>(), false);
    }

    public static HtmlTag selfClosing(String tagName) {
        return new HtmlTag(tagName, "", List.of(), new HashMap<>(), true);
    }

    public static HtmlTag selfClosing(String tagName, HashMap<String, String> attributes) {
        return new HtmlTag(tagName, "", List.of(), attributes, true);
    }

    public static HtmlTag of(String tagName, String content) {
        return new HtmlTag(tagName, content, List.of(), new HashMap<>(), false);
    }

    public static HtmlTag of(String tagName, String content, HashMap<String, String> attributes) {
        return new HtmlTag(tagName, content, List.of(), attributes, false);
    }

    public static HtmlTag of(String tagName, String content, List<HtmlTag> children) {
        return new HtmlTag(tagName, content, children, new HashMap<>(), false);
    }

    public static HtmlTag of(String tagName, String content, List<HtmlTag> children, HashMap<String, String> attributes) {
        return new HtmlTag(tagName, content, children, attributes, false);
    }
}
