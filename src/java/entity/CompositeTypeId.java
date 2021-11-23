package entity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable                         // 複合キークラスとして設定
public class CompositeTypeId implements Serializable {
    private String user_type;       // ユーザータイプを扱うフィールド
    private String user_id;         // ユーザーIDを扱うフィールド

	/**********************************************
	* パラメーターなしのコンストラクター
	**********************************************/
    public CompositeTypeId() { }
    
	
    public CompositeTypeId( String user_type, String user_id) {
        this.user_type = user_type;
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.user_type);
        hash = 61 * hash + Objects.hashCode(this.user_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositeTypeId other = (CompositeTypeId) obj;
        if (!Objects.equals(this.user_type, other.user_type)) {
            return false;
        }
        if (!Objects.equals(this.user_id, other.user_id)) {
            return false;
        }
        return true;
    }
}

