import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HtmlTagBuilderTest {
    @Test
    void testBuilderCreatesTag() {
        HtmlTag child = new HtmlTagBuilder("span").content("child").build();
        HtmlTag tag = new HtmlTagBuilder("div")
                .addAttribute("id", "main")
                .addChild(child)
                .content("text")
                .build();

        assertEquals("div", tag.tagName());
        assertEquals("text", tag.content());
        assertEquals("main", tag.getAttribute("id"));
        assertEquals(1, tag.children().size());
        assertEquals("span", tag.children().get(0).tagName());
    }
}
