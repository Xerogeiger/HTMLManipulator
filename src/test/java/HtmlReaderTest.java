import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HtmlReaderTest {
    @Test
    void testReadHtmlString() {
        String html = "<div id=\"main\"><span>text</span></div><img src=\"x\" />";
        List<HtmlTag> tags = HtmlReader.read(html);
        assertEquals(2, tags.size());

        HtmlTag div = tags.get(0);
        assertEquals("div", div.tagName());
        assertEquals("main", div.getAttribute("id"));
        assertEquals(1, div.children().size());
        assertEquals("span", div.children().get(0).tagName());
        assertEquals("text", div.children().get(0).content());

        HtmlTag img = tags.get(1);
        assertEquals("img", img.tagName());
        assertTrue(img.selfClosing());
        assertEquals("x", img.getAttribute("src"));
    }
}
