package triangle.service;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TriangleRequest {

    private String separator;
    private String input;

    public TriangleRequest(String input, String separator) {
        this.input = input;
        this.separator = separator;
    }

    public TriangleRequest(String input) {
        this(input, null);
    }

    public String getInput() {
        return input;
    }
    public String getSeparator() {
        return separator;
    }

    @Override
    public String toString() {
        return '{' +
                "input='" + input + "\', " +
                "separator='" + separator + '\'' +
                '}';
    }
}
