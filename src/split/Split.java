package split;

import java.io.*;

public class Split {

    public enum TypeNum {Strings, Chars, Files}

    private final String ofile;
    private final boolean flagD;
    private final TypeNum typeNum;
    private long num;

    private int lenPrefix = 0;
    private final char lastChar;
    private final char preLastChar;
    private final char firstChar;

    public Split(String ofile, boolean flagD, TypeNum typeNum, long num) {
        this.ofile = ofile;
        this.flagD = flagD;
        this.typeNum = typeNum;
        this.num = num;

        lastChar = flagD ? '9' : 'z';
        preLastChar = flagD ? '8' : 'y';
        firstChar = flagD ? '0' : 'a';
    }


    private char nextChar(char ch) {
        return (char) ((int) ch + 1);
    }

    public String nextPostfix(String str) {
        int lastIndex = str.length() - 1;
        if (typeNum != TypeNum.Files) {
            String mainSlice = str.substring(lenPrefix, lastIndex + 1);
            String needForPlus = preLastChar + (lastChar + "").repeat(str.length() - lenPrefix - 1);
            if (mainSlice.equals(needForPlus)) {
                String newStr = str.substring(0, lenPrefix) + lastChar +
                        (firstChar + "").repeat(str.length() - lenPrefix + 1);
                lenPrefix++;
                return newStr;
            }
        }
        int j = lastIndex;
        while (str.charAt(j) == lastChar) j--;
        return str.substring(0, j) + nextChar(str.charAt(j)) + (firstChar + "").repeat(lastIndex - j);
    }

    public long splitFile(String inputFileName) throws IOException {
        long numReads = num;
        long numRemainingRead = 0;

        if (typeNum == TypeNum.Files) {
            try (FileInputStream in = new FileInputStream(inputFileName)) {
                long inputCountChars = 0;
                try (InputStreamReader reader = new InputStreamReader(in)) {
                    int sym = reader.read();
                    while (sym != -1) {
                        inputCountChars++;
                        sym = reader.read();
                    }
                }
                numReads = inputCountChars / num;
                numRemainingRead = inputCountChars % num;
            }
        }
        int len = 2;
        if (typeNum == TypeNum.Files) {
            len = 1;
            int lenS = lastChar - firstChar + 1;
            long maxLen = lenS;
            while (num > maxLen) {
                maxLen *= lenS;
                len++;
            }
        }
        String postfix = ("" + firstChar).repeat(len);
        String outFileName = ofile + postfix;
        int countFiles = 0;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFileName))) {
            int sym = reader.read();
            while (sym != -1) {
                int i = 0;
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFileName))) {
                    while (i < numReads + (numRemainingRead > 0 ? 1 : 0) && sym != -1) {
                        writer.write(sym);
                        if (typeNum == TypeNum.Chars || typeNum == TypeNum.Files) i++;
                        else if (sym == '\n' || sym == '\r') i++;
                        sym = reader.read();
                    }
                    if (numRemainingRead > 0) numRemainingRead--;
                }
                countFiles++;
                postfix = nextPostfix(postfix);
                outFileName = ofile + postfix;
            }
        }
        return countFiles;
    }
}
