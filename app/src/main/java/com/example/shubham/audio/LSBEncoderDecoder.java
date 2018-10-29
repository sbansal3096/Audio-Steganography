package com.example.shubham.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import android.util.Base64;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class LSBEncoderDecoder {

    public String encode(String s, String key) {
        return new String(Base64.encode(xorWithKey(s.getBytes(), key.getBytes()), Base64.DEFAULT));
    }

    public String decode(String s, String key) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    private byte[] base64Decode(String s) {
        return Base64.decode(s,Base64.DEFAULT);
    }

    private String base64Encode(byte[] bytes) {
        return new String(Base64.encode(bytes,Base64.DEFAULT));

    }

    public void Audioencrypt(String message, InputStream ins, File file, int key) throws
            Exception {
        byte b[] = new byte[1];
        BigInteger Abi, Mbi;
        int k, k1;
        //ins= new FileInputStream(file);
        OutputStream outs = new FileOutputStream(file);

        System.out.println(message);
        String encoded = encode(message, String.valueOf(key));
        message = encoded;
        int len = message.length();
        System.out.println(message);
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
//                System.out.println(Blen.bitLength());
                Slen.getChars(0, Blen.bitLength(), Clen, 0);
                for (int j = 0; j <= 7; j++) {
                    if (j == 0) {
                        for (k = 0; k < 8 - Blen.bitLength(); k++) {
                            int n = ins.read(b);
                            Abi = new BigInteger(b);
                            String Aby = Abi.toString(2);
                            int Alen = Abi.bitLength();
//                            System.out.println("Alen"+Alen);
//                            System.out.println("N"+ n);
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
//                                        System.out.println("Big"+big +"Abi "+Abi +"Bi "+ bi) ;
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
                            if (Clen[j - k] == '1'){
                                BigInteger bi = new BigInteger("1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            }
                        }
                        else{
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
//                System.out.println("Message is "+ slen+"Blen "+Blen +"Slen " +Slen);
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
                            if(Clen[j - k1] == '1'){
                                BigInteger bi = new BigInteger("1", 2);
                                BigInteger big = Abi.add(bi);
                                b = big.toByteArray();
                            }

                        }
                        else {
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
                        }
                        outs.write(b);
                    } // end else

                } // for loop j
            } // end of else

        } // for loop i

        while (true) {
            int i = ins.read(b);
            if (i == -1) {
                break;
            }
            outs.write(b);
            System.out.println(outs);
        }
        ins.close();
        outs.close();
    }

    public String Audiodecrypt(InputStream ins, int key) throws Exception {
        byte b[] = new byte[1];
        BigInteger bb1;
        Log.d(TAG, "ok1");
        char mess[] = new char[8];
        int c = 0;

        for (int i = 0; i < 8; i++) {
            ins.read(b);
            bb1 = new BigInteger(b);
            String str = bb1.toString(2);
            int len = bb1.bitLength();
            if (b[0] < 0) {
                len++;
            }
            char ch[] = new char[len + 1];
            str.getChars(0, len, ch, 0);
            if (b[0] == 0) {
                mess[i] = '0';
            } else {
                mess[i] = ch[len - 1];
            }
        }
        String dd = new String(mess);
        BigInteger bb = new BigInteger(dd, 2);
        String s = bb.toString(2);
        int l = bb.intValue();

        char me[] = new char[l];
        int count = 0;

        for (int m = 0; m < l; m++) {
            for (int i = 0; i < 8; i++) {
                ins.read(b);
                bb1 = new BigInteger(b);
                String str = bb1.toString(2);
                int len = bb1.bitLength();
                if (b[0] < 0) {
                    len++;
                }
                char ch[] = new char[len + 1];
                str.getChars(0, len, ch, 0);
                if (b[0] == 0) {
                    mess[i] = '0';
                } else {
                    mess[i] = ch[len - 1];
                }
            }
            String dd1 = new String(mess);
            BigInteger bb2 = new BigInteger(dd1, 2);
            String s1 = bb2.toString(2);
            int l1 = bb2.intValue();
            me[count] = (char) l1;
            System.out.println(me[count]);
            count++;
        }

        String message = new String(me);
        System.out.println(message);
        String finalmsg = message;
        String decoded = decode(finalmsg, String.valueOf(key));
        finalmsg= decoded;
//        byte[] a= Base64.decode(finalmsg,Base64.DEFAULT);
//        System.out.println(a);
//        byte[] key1= String.valueOf(key).getBytes();
//        System.out.println(key1);
//        byte[] out = new byte[a.length];
//        System.out.println(a.length);
//        for (int i = 0; i < a.length; i++) {
//            out[i] = (byte) (a[i] ^ key1[i%key1.length]);
//            System.out.println(out);
//        }
//        System.out.println(out);
//        String decoded = new String(out);
        System.out.println(finalmsg);
        //      Amessage.setText(finalmsg);
        Log.d(TAG, "okok");
        ins.close();
        return finalmsg;
    }
}