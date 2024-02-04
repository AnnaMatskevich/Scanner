import java.io.*;
import java.nio.charset.StandardCharsets;

class Scanner {
    Reader r;
    private final int MAX_BUFFER_SIZE = 5000;
    private int bufferSize = 0;
    private int posI = 0;
    private StringBuilder nextEl = new StringBuilder();

    //private StringBuilder skippedWhiteSpaces =  new StringBuilder("");
    private final char[] buffer = new char[MAX_BUFFER_SIZE];
    private int endOfLine = 0;
    private int posSep = 0;
    private boolean flag = true;

    private static final String OURSEP = System.lineSeparator();


    public Scanner(File file) throws IOException {
        r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    public Scanner(File file, String encoding) throws IOException {
        r = new InputStreamReader(new FileInputStream(file), encoding);
    }

    private boolean readable(char a) {
        if (flag) {
            return !Character.isWhitespace(a);
        }
        return ((Character.DASH_PUNCTUATION == Character.getType(a) || Character.isLetter(a) || a == '\''));
    }

    public Scanner() {
        r = new InputStreamReader(System.in);
    }

    public Scanner(String st, boolean flag) {
        this.flag = flag;
        r = new StringReader(st);
    }

    public Scanner(File file, boolean flag) throws IOException {
        this.flag = flag;
        r = new InputStreamReader(new FileInputStream(file), "UTF-8");
    }

    public Scanner(File file, String encoding, boolean flag) throws IOException {
        this.flag = flag;
        r = new InputStreamReader(new FileInputStream(file), encoding);
    }

    public Scanner(boolean flag) {
        this.flag = flag;
        r = new InputStreamReader(System.in);
    }

    public Scanner(String st) {
        r = new StringReader(st);
    }

    public void close() throws IOException {
        r.close();
    }


    public boolean readBuffer() {
        while (posI < bufferSize) {
            if (readable(buffer[posI])) {
                return true;
            }
            if (buffer[posI] == OURSEP.charAt(posSep)) {
                posSep++;
                if (posSep == OURSEP.length()) {
                    endOfLine++;
                    posSep = 0;
                }
            } else {
                posSep = 0;
            }
            posI++;
        }
        return false;
    }

    public boolean findNotReadableInBuffer(int prevI) {
        while (posI < bufferSize) {
            if (!readable(buffer[posI])) {
                nextEl.append(String.copyValueOf(buffer, prevI, posI - prevI));
                return true;
            }
            posI++;
        }
        return false;
    }

    public boolean hasNext() throws IOException {
        String ans = nextEl.toString();
        if (!ans.equals("")) {
            return true;
        }
        if (readBuffer()) {
            return true;
        }
        bufferSize = r.read(buffer);
        while (bufferSize != -1) {
            posI = 0;
            if (readBuffer()) {
                return true;
            }
            bufferSize = r.read(buffer);
        }
        return false;
    }

    private void takeNext() throws IOException {
        if (!nextEl.toString().equals("")) {
            return;
        }
        if (!hasNext()) {
            return;
        }
        int prevI = posI;
        if (findNotReadableInBuffer(posI)) {
            return;
        }
        nextEl.append(String.copyValueOf(buffer, prevI, posI - prevI));
        try {
            bufferSize = r.read(buffer);
            while (bufferSize != -1) {
                posI = 0;
                if (findNotReadableInBuffer(0)) {

                    return;
                }
                nextEl.append(String.copyValueOf(buffer, 0, posI));
            }
        } catch (IOException e) {
            System.err.print("can't read");
            System.exit(0);
        }

    }

    public String next() throws IOException {

        takeNext();
        String ans = nextEl.toString();
        nextEl = new StringBuilder();
        endOfLine = 0;
        if (!ans.equals("")) {
            return ans;
        }
        System.err.print("we haven't got next");
        System.exit(0);
        return "";
    }

    public boolean hasNextInt() throws IOException {
        takeNext();
        String ans = nextEl.toString();
        if (ans.equals("")) {
            return false;
        }
        try {
            Integer.parseInt(ans, 10);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public int nextInt() throws IOException {
        if (hasNextInt()) {
            int otvv = Integer.parseInt(nextEl.toString(), 10);
            nextEl = new StringBuilder();
            endOfLine = 0;
            return otvv;
        } else {
            System.err.print("we haven't got nex int");
            System.exit(0);
        }
        return 0;
    }


    public int endsOfLine() throws IOException {
        hasNext();
        return endOfLine;
    }

    public void scipEndOfLine() {
        endOfLine -= 1;
        if (endOfLine < 0) {
            System.err.print("it wasn't end of line");
            System.exit(0);
        }
    }


    public String nextLine() throws IOException {
        if (!hasNext() && (endsOfLine() == 0)) {
            System.err.print("no next line");
            System.exit(0);
        }
        StringBuilder ans = new StringBuilder();

        while (hasNext() && (endOfLine == 0)) {
            ans.append(next()).append(" ");
        }
        if (endsOfLine() > 0) {
            scipEndOfLine();
        }
        return ans.toString();


    }

    public boolean hasNextLine() throws IOException {
        return hasNext() || (endsOfLine() > 0);
    }

}
