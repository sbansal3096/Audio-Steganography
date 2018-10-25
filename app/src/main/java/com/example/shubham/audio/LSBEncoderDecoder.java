package com.example.shubham.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class LSBEncoderDecoder {

    public void encrypt(String message, File inputFile, File outputFile, int key) throws
            Exception {
        byte b[] = new byte[1];
        BigInteger Abi, Mbi;
        int k, k1;
        InputStream ins = new FileInputStream(inputFile);
        OutputStream outs = new FileOutputStream(outputFile);
        for (int c = 0; c < key; c++) {
            int ch = ins.read();
            outs.write(ch);
        }
        int len = message.length();
        byte mess[] = new byte[1];
        char chmess[] = new char[len + 1];
        k = k1 = 0;
        for (int i = 0; i <= len; i++) {
            message.getChars(0, len, chmess, 0);
            if (i == 0) {
                BigDecimal bd = new BigDecimal(len);
                BigInteger Blen = bd.toBigInteger();
                String Slen = Blen.toString(2);
                char Clen[] = new char[Blen.bitLength()];
                System.out.println(Blen.bitLength());
                Slen.getChars(0, Blen.bitLength(), Clen, 0);
                for (int j = 0; j <= 7; j++) {
                    if (j == 0) {
                        for (k = 0; k < 8 - Blen.bitLength(); k++) {
                            int n = ins.read(b);
                            Abi = new BigInteger(b);
                            String Aby = Abi.toString(2);
                            int Alen = Abi.bitLength();
                            if (b[0] < 0) {
                                Alen++;
                            }
                            char Ach[] = new char[Alen + 1];
                            Aby.getChars(0, Alen, Ach, 0);

                            if (b[0] == 0) {
                            } else {
                                if (Ach[Alen - 1] == '1') {
                                    if (Alen == Abi.bitLength()) {
                                        BigInteger bi = new BigInteger("11111110", 2);
                                        BigInteger big = Abi.and(bi);
                                        b = big.toByteArray();
                                    } else {
                                        BigInteger bi = new BigInteger("-1", 2);
                                        BigInteger big = Abi.subtract(bi);
                                        b = big.toByteArray();
                                    }
                                }
                                outs.write(b);
                            }
                        }  //for loop k
                        j = j + k - 1;
                    } // if of j
                    else {
                        int n = ins.read(b);
                        Abi = new BigInteger(b);
                        String Aby = Abi.toString(2);
                        int Alen = Abi.bitLength();
                        if (b[0] < 0) {
                            Alen++;
                        }
                        char Ach[] = new char[Alen + 1];
                        Aby.getChars(0, Alen, Ach, 0);
                        if (b[0] == 0) {
                            Alen = 1;
                        }
                        if (Clen[j - k] == '0' && Ach[Alen - 1] == '1') {
                            if (Alen == Abi.bitLength()) {
                                BigInteger bi = new BigInteger("11111110", 2);
                                BigInteger big = Abi.and(bi);
                                b = big.toByteArray();
                            } else {
                                BigInteger bi = new BigInteger("-1", 2);
                                BigInteger big = Abi.subtract(bi);
                                b = big.toByteArray();
                            }
                        } else if (Clen[j - k] == '1' && Ach[Alen - 1] == '0') {
                            if (Alen == Abi.bitLength()) {
                                BigInteger bi = new BigInteger("1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            } else {
                                BigInteger bi = new BigInteger("-1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            }

                        }
                        outs.write(b);
                    } // end else

                } // for loop j

            } // end of if
            else {
                String slen = String.valueOf(chmess[i - 1]);
                byte blen[] = slen.getBytes();
                BigInteger Blen = new BigInteger(blen);
                String Slen = Blen.toString(2);
                char Clen[] = new char[Blen.bitLength()];
                Slen.getChars(0, Blen.bitLength(), Clen, 0);
                for (int j = 0; j <= 7; j++) {
                    if (j == 0) {
                        for (k1 = 0; k1 < 8 - Blen.bitLength(); k1++) {
                            int n = ins.read(b);
                            Abi = new BigInteger(b);
                            String Aby = Abi.toString(2);
                            int Alen = Abi.bitLength();
                            if (b[0] < 0) {
                                Alen++;
                            }
                            char Ach[] = new char[Alen + 1];
                            Aby.getChars(0, Alen, Ach, 0);
                            if (b[0] == 0) {
                            } else {
                                if (Ach[Alen - 1] == '1') {
                                    if (Alen == Abi.bitLength()) {
                                        BigInteger bi = new BigInteger("11111110", 2);
                                        BigInteger big = Abi.and(bi);
                                        b = big.toByteArray();
                                    } else {
                                        BigInteger bi = new BigInteger("-1", 2);
                                        BigInteger big = Abi.subtract(bi);
                                        b = big.toByteArray();
                                    }
                                }
                            }
                            outs.write(b);

                        }  //for loop k

                        j = j + k1 - 1;

                    } // if of j
                    else {
                        int n = ins.read(b);
                        Abi = new BigInteger(b);
                        String Aby = Abi.toString(2);
                        int Alen = Abi.bitLength();
                        if (b[0] < 0) {
                            Alen++;
                        }
                        char Ach[] = new char[Alen + 1];
                        Aby.getChars(0, Alen, Ach, 0);
                        if (b[0] == 0) {
                            Alen = 1;
                        }

                        if (Clen[j - k1] == '0' && Ach[Alen - 1] == '1') {
                            if (Alen == Abi.bitLength()) {
                                BigInteger bi = new BigInteger("11111110", 2);
                                BigInteger big = Abi.and(bi);
                                b = big.toByteArray();
                            } else {
                                BigInteger bi = new BigInteger("-1", 2);
                                BigInteger big = Abi.subtract(bi);
                                b = big.toByteArray();
                            }
                        } else if (Clen[j - k1] == '1' && Ach[Alen - 1] == '0') {
                            if (Alen == Abi.bitLength()) {
                                BigInteger bi = new BigInteger("1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            } else {
                                BigInteger bi = new BigInteger("-1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            }
                        }
                        outs.write(b);
                    } // end else

                } // for loop j
            } // end of else

        } // for loop i

        while (true) {
            int i = ins.read();
            if (i == -1) {
                break;
            }
            outs.write(i);
        }
        ins.close();
        outs.close();
    }
}
