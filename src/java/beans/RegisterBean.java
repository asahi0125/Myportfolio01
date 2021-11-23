package beans;

import enterprise.FetchUserAccount;
import enterprise.UserAccountFacade;
import javax.ejb.*;
import entity.*;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import utility.SHA256Encoder;

@RequestScoped
@Named
public class RegisterBean extends BaseBean implements Serializable {

    @EJB
    UserAccountFacade entity;
    
    @EJB
    FetchUserAccount fetchUserAccount;  // アカウント情報に関するクエリを実行するEJB

    private String id;                  // ユーザーID
    private String passwd;              // パスワード
    private String name;                // 氏名
    private String mail;                // メールアドレス
    private String type_id= "user";     // ユーザーのタイプはuserとする

    /**********************************************
    * ユーザー登録画面で入力された値をデータベースに登録する
    * @return    cart.xhtmlにリダイレクト
    **********************************************/
    public String registerAccount() {

        if(0!=countAccount()){
            setMessage("指定したユーザーIDはすでに使用されています。");
            return null;
        }else{

            // 複合キークラスをインスタンス化
            CompositeTypeId key = new CompositeTypeId(this.type_id, this.id);
            // エンティティTypeAndIdをインスタンス化
            TypeAndId type      = new TypeAndId(key, null);
            // UserAccountクラスをインスタンス化
            // パスワードはSHA256で暗号化する
            UserAccount account = new UserAccount(
                                          this.id,
                                          getEncoded(this.passwd),
                                          this.name,
                                          this.mail,
                                          type
                                      );
            // TypeAndIdクラスのセッターでUserAccountオブジェクトをセット
            type.setUser_account(account);
            // アカウント情報をデータベースに登録
            entity.create(account);
            // トップページにリダイレクト
            return "index?faces-redirect=true";
        }
    }

    public long countAccount() {
        long account = 0;
        try {
            account = fetchUserAccount.countAccount(this.id);
        } catch (Exception e) {
            setMessage("アカウント情報の取得に失敗しました");
        }
        return account;
    }
    
    /**********************************************
    * パスワードの暗号化
    * @return  暗号化後のパスワード
    **********************************************/
    private String getEncoded(String pass) {
        // SHA256Encoderクラスをインスタンス化
        SHA256Encoder encoder = new SHA256Encoder();
        // encodeBySHA256()でエンコードした結果を返す
        return encoder.encodeBySHA256(pass);
    }

    // セッター・ゲッター
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getPasswd() { return passwd; }

    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }

    public void setMail(String mail) { this.mail = mail; }

    public String getType_id() { return type_id; }

    public void setType_id(String type_id) { this.type_id = type_id; }
}

