package mouse.univ.ui;

public interface UserIO {
    String getString();
    String getString(String prompt);
    void print(String message);
    void println(String message);
}
