package entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="USERTYPE")
public class TypeAndId implements Serializable {
    // CompositeTypeIdの複合キー（user_type、user_idフィールド）を主キーとして設定
    @EmbeddedId
    private CompositeTypeId comp_type_id;       
    // UserAccountクラスとマッピングする
    @OneToOne(mappedBy="type_id")
    private UserAccount user_account;
    
    /**********************************************
    * パラメーターなしのコンストラクター
    **********************************************/
    public TypeAndId() {}
    
   
    public TypeAndId(String type, UserAccount user_account) {
        this.comp_type_id = new CompositeTypeId(type, user_account.getId());
        this.user_account = user_account;
    }
    
    
    public TypeAndId(CompositeTypeId comp_type_id, UserAccount user_account) {
        this.comp_type_id = comp_type_id;
        this.user_account = user_account;
    }

    public CompositeTypeId getComp_type_id() {
        return comp_type_id;
    }

    public void setComp_type_id(CompositeTypeId comp_type_id) {
        this.comp_type_id = comp_type_id;
    }

    public UserAccount getUser_account() {
        return user_account;
    }

    public void setUser_account(UserAccount user_account) {
        this.user_account = user_account;
    }
}

