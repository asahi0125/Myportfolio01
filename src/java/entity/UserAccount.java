package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="STOREUSER")

@NamedQueries ({
    // 新規登録するユーザーIDがすでに登録されていないか検索する
    @NamedQuery(
        name  = UserAccount.COUNT_ID,
        query = "select COUNT(b) from UserAccount b where b.id=:userId"),
})

public class UserAccount implements Serializable {
    @Id
    private String id;                     // ユーザーIDを保持する
    private String passwd;                 // パスワードを保持する
    private String name;                   // 氏名を保持する
    private String mail;                   // メールアドレスを保持する
    @OneToOne(cascade={CascadeType.ALL})   // カスケードの指定（すべてのデータ処理を自動で行う）
    private TypeAndId type_id;             // TypeAndIdのインスタンス

    // クエリ名を格納した定数を定義
    public static final String COUNT_ID  = "UserAccount.countId";

    /**********************************************
    * パラメーターなしのコンストラクター
    **********************************************/
    public	UserAccount(){}

    
    public UserAccount(
        String id,
        String passwd,
        String name,
        String mail,
        TypeAndId type_id
    ){
        this.id       = id;
        this.passwd =	passwd;
        this.name     = name;
        this.mail     = mail;
        this.type_id  = type_id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public TypeAndId getType_id() {
        return type_id;
    }

    public void setType_id(TypeAndId type_id) {
        this.type_id = type_id;
    }
}

