package control.ajax;

import com.craftfire.authdb.util.encryption.Encryption;

//https://github.com/CraftFire/AuthDB
public class PhpBB {

    public static String Name = "phpbb";
    public static String ShortName = "phpbb";
    public static String VersionRange = "3.0.0-3.0.9";
    public static String VersionRange2 = "2.0.0-2.0.23";
    public static String LatestVersionRange = VersionRange;

    private static String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String phpbb_hash(String password) {
        String random_state = unique_id();
        StringBuffer random = new StringBuffer();
        int count = 6;
        String temp = "";

        if (random.length() < count) {
            for (int i = 0; i < count; i += 16) {
                random_state = Encryption.md5(unique_id() + random_state);
                random.append(Encryption.pack(Encryption.md5(random_state)));
            }
            temp = random.toString().substring(0, count);
        }

        String hash = _hash_crypt_private(password, _hash_gensalt_private(temp, itoa64));
        if (hash.length() == 34) {
            return hash;
        }

        return Encryption.md5(password);
    }

    private static String unique_id() {
        return "1234567890abcdef";
    }

    private static String _hash_gensalt_private(String input, String itoa64) {
        return _hash_gensalt_private(input, itoa64, 6);
    }

    private static String _hash_gensalt_private(String input, String itoa64, int iteration_count_log2) {
        if (iteration_count_log2 < 4 || iteration_count_log2 > 31) {
            iteration_count_log2 = 8;
        }
        int PHP_VERSION = 5;
        StringBuffer output = new StringBuffer("$H$");
        output.append(itoa64.charAt(Math.min(iteration_count_log2 + ((PHP_VERSION >= 5) ? 5 : 3), 30)));
        output.append(_hash_encode64(input, 6));

        return output.toString();
    }

    /**
     * Encode hash
     */
    private static String _hash_encode64(String input, int count) {
        StringBuffer output = new StringBuffer();
        int i = 0;

        do {
            int value = input.charAt(i++);
            output.append(itoa64.charAt(value & 0x3f));

            if (i < count) {
                value |= input.charAt(i) << 8;
            }

            output.append(itoa64.charAt((value >> 6) & 0x3f));

            if (i++ >= count) {
                break;
            }

            if (i < count) {
                value |= input.charAt(i) << 16;
            }

            output.append(itoa64.charAt((value >> 12) & 0x3f));

            if (i++ >= count) {
                break;
            }

            output.append(itoa64.charAt((value >> 18) & 0x3f));
        } while (i < count);

        return output.toString();
    }

    static String _hash_crypt_private(String password, String setting) {
        String output = "*";

        // Check for correct hash 
        if (!setting.substring(0, 3).equals("$H$")) {
            return output;
        }

        int count_log2 = itoa64.indexOf(setting.charAt(3));
        if (count_log2 < 7 || count_log2 > 30) {
            return output;
        }

        int count = 1 << count_log2;
        String salt = setting.substring(4, 12);
        if (salt.length() != 8) {
            return output;
        }

        String m1 = Encryption.md5(salt + password);
        String hash = Encryption.pack(m1);
        do {
            hash = Encryption.pack(Encryption.md5(hash + password));
        } while (--count > 0);

        output = setting.substring(0, 12) + _hash_encode64(hash, 16);

        return output;
    }

    public static boolean check_hash(String password, String hash) {
        if (hash.length() == 34) {
            return _hash_crypt_private(password, hash).equals(hash);
        } else {
            return Encryption.md5(password).equals(hash);
        }
    }
}
