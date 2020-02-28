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

    public long splitFile(String inputFileName) throws IOException {
        try (FileInputStream in = new FileInputStream(inputFileName)) {
            long countFiles = 0;
            int lastNum = 1;
            String lastChars = "aa", outFileName = flagD ? ofile + lastNum : ofile + lastChars;
            if (typeNum == TypeNum.Files) {
                try (BufferedInputStream reader = new BufferedInputStream(in)) {
                    long sourceSize = new File(inputFileName).length();
                    long bytesPerSplit = sourceSize / num;
                    long remainingBytes = sourceSize % num;

                    int maxReadBufferSize = 8 * 1024; //8KB
                    for (int file = 0; file < num; file++) {
                        try (BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(outFileName))) {
                            if (bytesPerSplit + (remainingBytes > 0 ? 1 : 0) > maxReadBufferSize) {
                                long numReads = (bytesPerSplit + (remainingBytes > 0 ? 1 : 0)) / maxReadBufferSize;
                                long numRemainingRead =
                                        (bytesPerSplit + (remainingBytes > 0 ? 1 : 0)) % maxReadBufferSize;
                                for (int i = 0; i < numReads; i++) {
                                    readWrite(reader, bw, maxReadBufferSize);
                                }
                                if (numRemainingRead > 0) {
                                    readWrite(reader, bw, numRemainingRead);
                                }
                            } else {
                                readWrite(reader, bw, bytesPerSplit + (remainingBytes > 0 ? 1 : 0));
                            }
                        }
                        if (remainingBytes > 0) remainingBytes--;
                        lastNum++;
                        if (!flagD) lastChars = nextChars(lastChars);
                        outFileName = flagD ? ofile + lastNum : ofile + lastChars;
                    }
                    countFiles = num;
                }
            } else {
                try (InputStreamReader reader = new InputStreamReader(in)) {
                    int sym = reader.read();
                    while (sym != -1) {
                        int i = 0;
                        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFileName))) {
                            while (i < num && sym != -1) {
                                writer.write(sym);
                                if (typeNum == TypeNum.Chars) i++;
                                else if (sym == '\n' || sym == '\r') i++;
                                sym = reader.read();
                            }
                        }
                        countFiles++;
                        lastNum++;
                        if (!flagD) lastChars = nextChars(lastChars);
                        outFileName = flagD ? ofile + lastNum : ofile + lastChars;
                    }
                }
            }
            return countFiles;
        }
    }

    private void readWrite(BufferedInputStream in, BufferedOutputStream out, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = in.read(buf);
        if(val != -1) {
            out.write(buf);
        }
    }
}
