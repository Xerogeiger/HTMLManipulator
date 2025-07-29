import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlWriter {
    private final OutputStream outputStream;
    private final String indentStep;

    /**
     * Create a writer that pretty‐prints with two spaces per level.
     */
    public HtmlWriter(OutputStream outputStream) {
        this(outputStream, "  ");
    }

    /**
     * Create a writer with a custom indent string (e.g. "\t" or "    ").
     */
    public HtmlWriter(OutputStream outputStream, String indentStep) {
        this.outputStream = outputStream;
        this.indentStep = indentStep;
    }

    /**
     * Write the tag (and its content) to the stream, then flush.
     */
    public void write(HtmlTag tag) throws IOException {
        writeTag(tag, 0);
        outputStream.flush();
    }

    private void writeTag(HtmlTag tag, int indent) throws IOException {
        // write opening tag with attributes
        writeIndent(indent);
        StringBuilder open = new StringBuilder("<").append(tag.tagName());
        for (Map.Entry<String, String> attr : tag.attributes().entrySet()) {
            open.append(" ")
                .append(attr.getKey())
                .append("=\"")
                .append(attr.getValue())
                .append("\"");
        }
        if (tag.selfClosing()) {
            open.append(" />\n");
            outputStream.write(open.toString().getBytes(StandardCharsets.UTF_8));
            return;
        } else {
            open.append(">");
            outputStream.write(open.toString().getBytes(StandardCharsets.UTF_8));
        }

        // write content (if any), each line indented one level deeper
        if (!tag.content().isBlank()) {
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            for (String line : tag.content().split("\\R")) {
                writeIndent(indent + 1);
                outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            }
            writeIndent(indent);
        }

        // write children, each indented one level deeper
        for (HtmlTag child : tag.children()) {
            writeTag(child, indent + 1);
        }

        // write closing tag
        String close = "</" + tag.tagName() + ">\n";
        outputStream.write(close.getBytes(StandardCharsets.UTF_8));
    }

    private void writeIndent(int level) throws IOException {
        for (int i = 0; i < level; i++) {
            outputStream.write(indentStep.getBytes(StandardCharsets.UTF_8));
        }
    }
}