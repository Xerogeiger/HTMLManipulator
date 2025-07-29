import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlTagTest {
    @Test
    void testAttributeAccessAndWithAttribute() {
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put("int", "5");
        attrs.put("double", "3.14");
        attrs.put("bool", "true");
        HtmlTag tag = HtmlTag.of("div", "", attrs);

        assertEquals("5", tag.getAttribute("int"));
        assertEquals(5, tag.getAttributeAsInt("int"));
        assertEquals(3.14, tag.getAttributeAsDouble("double"));
        assertTrue(tag.getAttributeAsBoolean("bool"));

        assertEquals("", tag.getAttribute("missing"));
        assertEquals(0, tag.getAttributeAsInt("missing"));
        assertEquals(0.0, tag.getAttributeAsDouble("missing"));
        assertFalse(tag.getAttributeAsBoolean("missing"));

        HtmlTag modified = tag.withAttribute("extra", "value");
        assertEquals("value", modified.getAttribute("extra"));
        assertTrue(modified.hasAttribute("extra"));
    }

    @Test
    void testMutationHelpersAndFactories() {
        HtmlTag base = HtmlTag.empty("p");
        HtmlTag withContent = base.withContent("hello");
        assertEquals("hello", withContent.content());

        HtmlTag withChild = base.withChildren(List.of(HtmlTag.of("span", "t")));
        assertTrue(withChild.hasChildren());

        HtmlTag selfClosing = HtmlTag.selfClosing("br");
        assertTrue(selfClosing.selfClosing());
    }
}
