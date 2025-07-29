import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

public class HtmlWriterTest {
    @Test
    void testWriteSimpleTag() throws Exception {
        HtmlTag br = new HtmlTagBuilder("br").selfClosing(true).build();
        HtmlTag tag = new HtmlTagBuilder("div")
                .addAttribute("class", "container")
                .addChild(br)
                .content("Hello")
                .build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlWriter writer = new HtmlWriter(out, "  ");
        writer.write(tag);

        String expected = "<div class=\"container\">\n" +
                "  Hello\n" +
                "  <br />\n" +
                "</div>\n";
        String result = out.toString(StandardCharsets.UTF_8);
        assertEquals(expected, result);
    }
}
