package split;

import java.io.*;

public class Split {

    public enum TypeNum {Strings, Chars, Files}

    private final String ofile;
    private final boolean flagD;
    private final TypeNum typeNum;
    private long num;

    public Split(String ofile, boolean flagD, TypeNum typeNum, long num) {
        this.ofile = ofile;
        this.flagD = flagD;
        this.typeNum = typeNum;
        this.num = num;
    }

    private String nextChars(String lastChars) {
        if (lastChars.charAt(1) == 'z') {
            return "" + (char) ((int) lastChars.charAt(0) + 1) + 'a';
        }
        return "" + lastChars.charAt(0) + (char) ((int) lastChars.charAt(1) + 1);
    }

    public int splitFile(String inputFileName) throws IOException {
        try (FileInputStream in = new FileInputStream(inputFileName)) {
            int countFiles = 0;
            int lastNum = 1;
            String lastChars = "aa", outFileName = flagD ? ofile + lastNum : ofile + lastChars;
            long numReads = num;
            long numRemainingRead = 0;
            if (typeNum == TypeNum.Files) {
                long inputFileSize = new File(inputFileName).length();
                numReads = inputFileSize / num;
                numRemainingRead = inputFileSize % num;
            }
            try (InputStreamReader reader = new InputStreamReader(in)) {
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
                    lastNum++;
                    if (!flagD) lastChars = nextChars(lastChars);
                    outFileName = flagD ? ofile + lastNum : ofile + lastChars;
                }
            }
            return countFiles;
        }
    }
}
