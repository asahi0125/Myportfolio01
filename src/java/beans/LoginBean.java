package beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBean extends BaseBean implements Serializable {

    @Inject
    transient Logger log;

    private String username;
    private String password;

    /**********************************************
    * 認証を行ってログインのための処理を実行する
    * @return  トップページへリダイレクト
    **********************************************/
    public String login() {
        try {
            HttpServletRequest request = getRequest();
            // サインイン中であればメッセージを表示
            if (request.getUserPrincipal() != null) {
                setErrorMessage("すでにサインインしています");
                return null;
            }
            // サインインしたユーザー名をログに出力
            log.log(Level.INFO, "{0}:signin", username);
            // ユーザー認証を実行
            request.login(username, password);
        } catch (ServletException ex) {
            setErrorMessage("サインインできません");
            return null;
        }
        // 認証後、トップページへリダイレクト
        return "/index.xhtml?faces-redirect=true";
    }

    /**********************************************
    * ログアウトの処理を行う
    * @return  トップページへリダイレクト
    **********************************************/
    public String logout() {
        try {
            HttpServletRequest request = getRequest();
            // サインイン中でなければメッセージを表示
            if (request.getUserPrincipal() == null) {
                setErrorMessage("サインインしていません");
                return null;
            }
            // スーパークラスのgetServletContext()で
            // ExternalContextを取得し、invalidateSession()で
            // セッションを無効にする
            getServletContext().invalidateSession();
            // HttpServletRequestのlogout()で
            // 認証情報を破棄することで、ログアウトの処理を行う
            request.logout();
        } catch (ServletException ex) {}
        // トップページへリダイレクト
        return "/index.xhtml?faces-redirect=true ";
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

