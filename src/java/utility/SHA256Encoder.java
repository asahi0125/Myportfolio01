package utility;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SHA256Encoder  implements Serializable {
    
    public String encodeBySHA256(String password) {
        // エンコード後のパスワードを格納する変数
        String encorded = "";
        try {
            // メッセージダイジェストアルゴリズムの機能を提供する抽象クラスを
            // getInstance()で取得 取得の際にSHA-256を指定
            MessageDigest mdst = MessageDigest.getInstance("SHA-256");
            // パスワードのバイト列を引数にしてハッシュ値を生成
            // 結果はbyte型の配列で返される
            byte[] digest    = mdst.digest(password.getBytes());
            // StringBuilderオブジェクトを生成
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String tmp_values =
                        // 16進数で表現したハッシュ値digestの各要素を
                        // 符号なしの文字列表現として取得
                        Integer.toHexString(digest[i] & 0xff);
                if (tmp_values.length() == 1) {
                    // 長さが1であれば0をstrBuilderに付加し、
                    // さらにtmp_valuesを追加する
                    // 先頭に0を付けるのは複合化の際に
                    // 16進数として扱われるようにするため
                    strBuilder.append('0').append(tmp_values);
                } else {
                    // それ以外はstrBuilderに直接tmp_valuesを追加
                    strBuilder.append(tmp_values);
                }
            }
            // strBuilder型をString型にキャスト
            encorded = strBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHA256Encoder.class.getName()
                            ).log(
                                Level.SEVERE, null, ex
                            );
        }
        return encorded;    // ハッシュ値を文字列表現にしたものを返却
    }
}

