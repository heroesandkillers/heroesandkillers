/*
 * This file is part of AuthDB.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
 * AuthDB is licensed under the GNU Lesser General Public License.
 *
 * AuthDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.authdb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class Util {
    static int schedule = 1;
    
    public static long timeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static long timeMS() {
        return System.currentTimeMillis();
    }

    public static String forumCache(String cache, String player, int userid, String nummember, String activemembers, String newusername, String newuserid, String extrausername, String lastvalue) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            array.add(st.nextToken() + ":");
        }
        StringBuffer newcache = new StringBuffer();
        while (array.size() > i) {
            if (array.get(i).equals("\"" + nummember + "\";i:") && nummember != null) {
                String temp = array.get(i + 1);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                if (lastvalue.equalsIgnoreCase(nummember)) {
                    temp = tempnum + ";}";
                } else {
                    temp = tempnum + ";s:";
                }
                array.set(i + 1, temp);
            } else if (array.get(i).equals("\"" + newusername + "\";s:") && newusername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(newusername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + extrausername + "\";s:") && extrausername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(extrausername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + activemembers + "\";s:") && activemembers != null) {
                String temp = array.get(i + 2);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                String templength = "" + tempnum;
                if (lastvalue.equalsIgnoreCase(activemembers)) {
                    temp = "\"" + tempnum + "\"" + ";}";
                } else {
                    temp = "\"" + tempnum + "\"" + ";s:";
                }
                array.set(i + 1, templength.length() + ":");
                array.set(i + 2, temp);
            } else if (array.get(i).equals("\"" + newuserid + "\";s:") && newuserid != null) {
                String dupe = "" + userid;
                array.set(i + 1, dupe.length() + ":");
                if (lastvalue.equalsIgnoreCase(newuserid)) {
                    array.set(i + 2, "\"" + userid + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + userid + "\"" + ";s:");
                }
            }
            newcache.append(array.get(i));
            i++;
        }
        return newcache.toString();
    }

    public static String forumCacheValue(String cache, String value) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            array.add(st.nextToken() + ":");
        }
        while (array.size() > i) {
            if (array.get(i).equals("\"" + value + "\";s:") && value != null) {
                String temp = array.get(i + 2);
                temp = removeChar(temp, '"');
                temp = removeChar(temp, ':');
                temp = removeChar(temp, 's');
                temp = removeChar(temp, ';');
                temp = temp.trim();
                return temp;
            }
            i++;
        }
        return "no";
    }

    public static int toTicks(String time, String length) {
        ////logging.debug("Launching function: toTicks(String time, String length) - " + time + ":" + length);
        time = time.toLowerCase();
        int lengthint = Integer.parseInt(length);
        if (time.equalsIgnoreCase("days") || time.equalsIgnoreCase("day") || time.equalsIgnoreCase("d")) {
            return lengthint * 1728000;
        } else if (time.equalsIgnoreCase("hours") || time.equalsIgnoreCase("hour") || time.equalsIgnoreCase("hr") || time.equalsIgnoreCase("hrs") || time.equalsIgnoreCase("h")) {
            return lengthint * 72000;
        } else if (time.equalsIgnoreCase("minute") || time.equalsIgnoreCase("minutes") || time.equalsIgnoreCase("min") || time.equalsIgnoreCase("mins") || time.equalsIgnoreCase("m")) {
            return lengthint * 1200;
        } else if (time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("sec") || time.equalsIgnoreCase("s")) {
            return lengthint * 20;
        }
        return 0;
    }

    public static int toSeconds(String time, String length) {
        ////logging.debug("Launching function: toSeconds(String time, String length) - " + time + ":" + length);
        time = time.toLowerCase();
        int lengthint = Integer.parseInt(length);
        if (time.equalsIgnoreCase("days") || time.equalsIgnoreCase("day") || time.equalsIgnoreCase("d")) {
            return lengthint * 86400;
        } else if (time.equalsIgnoreCase("hours") || time.equalsIgnoreCase("hour") || time.equalsIgnoreCase("hr") || time.equalsIgnoreCase("hrs") || time.equalsIgnoreCase("h")) {
            return lengthint * 3600;
        } else if (time.equalsIgnoreCase("minute") || time.equalsIgnoreCase("minutes") || time.equalsIgnoreCase("min") || time.equalsIgnoreCase("mins") || time.equalsIgnoreCase("m")) {
            return lengthint * 60;
        } else if (time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("sec") || time.equalsIgnoreCase("s")) {
            return lengthint;
        }
        return 0;
    }

    public static int stringToTicks(String string) {
        String[] split = string.split(" ");
        String length = split[0];
        String time = split[1].toLowerCase();
        int lengthint = Integer.parseInt(length);
        ////logging.debug("Launching function: FullStringToSeconds(String time, String length) - " + time + ":" + length);
        if (time.equalsIgnoreCase("days") || time.equalsIgnoreCase("day") || time.equalsIgnoreCase("d")) {
            return lengthint * 1728000;
        } else if (time.equalsIgnoreCase("hours") || time.equalsIgnoreCase("hour") || time.equalsIgnoreCase("hr") || time.equalsIgnoreCase("hrs") || time.equalsIgnoreCase("h")) {
            return lengthint * 72000;
        } else if (time.equalsIgnoreCase("minute") || time.equalsIgnoreCase("minutes") || time.equalsIgnoreCase("min") || time.equalsIgnoreCase("mins") || time.equalsIgnoreCase("m")) {
            return lengthint * 1200;
        } else if (time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("sec") || time.equalsIgnoreCase("s")) {
            return lengthint * 20;
        }
        return 0;
    }

    public static int stringToSeconds(String string) {
        String[] split = string.split(" ");
        String length = split[0];
        String time = split[1].toLowerCase();
        int lengthint = Integer.parseInt(length);
        ////logging.debug("Launching function: StringToSeconds(String time, String length) - " + time + ":" + length);
        if (time.equalsIgnoreCase("days") || time.equalsIgnoreCase("day") || time.equalsIgnoreCase("d")) {
            return lengthint * 86400;
        } else if (time.equalsIgnoreCase("hours") || time.equalsIgnoreCase("hour") || time.equalsIgnoreCase("hr") || time.equalsIgnoreCase("hrs") || time.equalsIgnoreCase("h")) {
            return lengthint * 3600;
        } else if (time.equalsIgnoreCase("minute") || time.equalsIgnoreCase("minutes") || time.equalsIgnoreCase("min") || time.equalsIgnoreCase("mins") || time.equalsIgnoreCase("m")) {
            return lengthint * 60;
        } else if (time.equalsIgnoreCase("second") || time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("sec") || time.equalsIgnoreCase("s")) {
            return lengthint;
        }
        return 0;
    }

    public static String toLoginMethod(String method) {
        method = method.toLowerCase();
        if (method.equalsIgnoreCase("prompt")) {
            return method;
        } else {
            return "normal";
        }
    }

    public static long ip2Long(String ip) {
        //logging.debug("Launching function: IP2Long(String IP)");
        long f1, f2, f3, f4;
        String tokens[] = ip.split("\\.");
        if (tokens.length != 4) {
            return -1;
        }
        try {
            f1 = Long.parseLong(tokens[0]) << 24;
            f2 = Long.parseLong(tokens[1]) << 16;
            f3 = Long.parseLong(tokens[2]) << 8;
            f4 = Long.parseLong(tokens[3]);
            return f1 + f2 + f3 + f4;
        } catch (Exception e) {
            return -1;
        }

    }

    public static String fixCharacters(String string) {
        int lengtha = string.length();
        int lengthb = "`~!@#$%^&*()-= + {[]}|\\:;\"<, >.?/".length();
        int i = 0;
        char thechar1, thechar2;
        StringBuffer tempstring = new StringBuffer();
        while (i < lengtha) {
            thechar1 = string.charAt(i);
            int a = 0;
            while (a < lengthb) {
                thechar2 = "`~!@#$%^&*()-= + {[]}|\\:;\"<, >.?/".charAt(a);
                if (thechar1 == thechar2 || thechar1 == '\'' || thechar1 == '\"') {
                    thechar1 = thechar2;
                }
                a++;
            }
            tempstring.append(thechar1);
            i++;
        }
        return tempstring.toString();
    }

    public static String removeColors(String toremove) {
        long start = Util.timeMS();
        //logging.debug("Launching function: removeColors");
        toremove = toremove.replace("?0", "");
        toremove = toremove.replace("?2", "");
        toremove = toremove.replace("?3", "");
        toremove = toremove.replace("?4", "");
        toremove = toremove.replace("?5", "");
        toremove = toremove.replace("?6", "");
        toremove = toremove.replace("?7", "");
        toremove = toremove.replace("?8", "");
        toremove = toremove.replace("?9", "");
        toremove = toremove.replace("?a", "");
        toremove = toremove.replace("?b", "");
        toremove = toremove.replace("?c", "");
        toremove = toremove.replace("?d", "");
        toremove = toremove.replace("?e", "");
        toremove = toremove.replace("?f", "");

        long stop = Util.timeMS();
        //Util.//logging.debug("Took " + ((stop - start) / 1000) + " seconds (" + (stop - start) + "ms) to replace colors.");

        return toremove;
    }

    public static String removeChar(String s, char c) {
        //logging.debug("Launching function: removeChar(String s, char c)");
        StringBuffer r = new StringBuffer(s.length());
        r.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) r.setCharAt(current++, cur);
        }
        return r.toString();
    }

    public static String getRandomString(int length) {
        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    public static String getRandomString2(int length, String charset) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static String getAction(String action) {
        if (action.toLowerCase().equalsIgnoreCase("kick")) {
            return "kick";
        } else if (action.toLowerCase().equalsIgnoreCase("ban")) {
            return "ban";
        } else if (action.toLowerCase().equalsIgnoreCase("rename")) {
            return "rename";
        }
        return "kick";
    }

    public static int hexToInt(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        ch = Character.toUpperCase(ch);
        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 0xA;
        }
        throw new IllegalArgumentException("Not a hex character: " + ch);
    }

    public static String hexToString(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    public static String checkSessionStart (String string) {
        if (string.equalsIgnoreCase("login")) {
            return "login";
        } else if (string.equalsIgnoreCase("logoff")) {
            return "logoff";
        } else {
            return "login";
        }
    }

    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            }
            while (twoHalfs++< 1);
        }
        return buf.toString();
    }

    public static String bytes2hex(byte[] bytes) {
        StringBuffer r = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String x = Integer.toHexString(bytes[i] & 0xff);
            if (x.length() < 2) {
                r.append("0");
            }
            r.append(x);
        }
        return r.toString();
    }
}
