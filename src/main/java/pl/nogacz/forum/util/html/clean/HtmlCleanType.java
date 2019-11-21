package pl.nogacz.forum.util.html.clean;

public enum HtmlCleanType {
    PLAIN_TEXT("plain-text"),
    SIMPLE_TEXT("simple-text"),
    BASIC_HTML("basic-html");

    private final String value;

    HtmlCleanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
