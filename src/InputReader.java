import java.util.InputMismatchException;
import java.util.StringTokenizer;

public class InputReader {
    StringTokenizer st;

    public InputReader(String str) {
        eat(str);
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public String next() {
        return st.nextToken();
    }

    public void eat(String str) {
        if (str == null) throw new InputMismatchException();
        st = new StringTokenizer(str);
    }
}