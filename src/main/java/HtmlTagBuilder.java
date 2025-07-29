import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HtmlTagBuilder {
    private String name;
    private HashMap<String, String> attributes;
    private String content;
    private List<HtmlTag> children;
    private boolean selfClosing;

    public HtmlTagBuilder(String name) {
        this.name = name;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        this.content = "";
        this.selfClosing = false;
    }

    public HtmlTagBuilder() {
        this.name = "";
        this.attributes = new HashMap<>();
        this.content = "";
        this.selfClosing = false;
    }

    public HtmlTagBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HtmlTagBuilder attributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public HtmlTagBuilder addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public <T> HtmlTagBuilder addAttribute(String key, T value) {
        this.attributes.put(key, String.valueOf(value));
        return this;
    }

    public HtmlTagBuilder children(List<HtmlTag> children) {
        this.children = children;
        return this;
    }

    public HtmlTagBuilder addChild(HtmlTag child) {
        this.children.add(child);
        return this;
    }

    public HtmlTagBuilder content(String content) {
        this.content = content;
        return this;
    }

    public HtmlTagBuilder selfClosing(boolean selfClosing) {
        this.selfClosing = selfClosing;
        return this;
    }

    public HtmlTag build() {
        return new HtmlTag(name, content, children, attributes, selfClosing);
    }
}
