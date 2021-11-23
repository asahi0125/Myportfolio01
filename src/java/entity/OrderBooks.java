package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@NamedQueries ({
    // 指定したIDがACCOUNTテーブルのid値と一致するレコードを抽出する
    @NamedQuery(
    name=OrderBooks.ORDER_HISTORY,
    query="select o from OrderBooks o where o.account.id=:userId ORDER BY o.po_date DESC")
})
@Entity
@Table(name="ORDERBOOK")
public class OrderBooks implements Serializable {
    // クエリ名を格納した定数を定義
    public static final String ORDER_HISTORY = "OrderBooks.orderHistory";
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long			id;           // 注文ごとに連番を割り振る
    private	UserAccount		account;      // UserAcountオブジェクト
    private	String			cust_name;    // 顧客の氏名
    private	String			cust_mail;    // 顧客のメールアドレス
    private	String			cust_demand;  // 注文時の要望
    private	Integer			cust_payment; // 支払い方法
    private	Integer			total_amount; // 購入金額
    @Temporal(javax.persistence.TemporalType.DATE)
    private	Date			po_date;      // 注文日
    @OneToMany(cascade={CascadeType.ALL})             // OneToManyでカスケード指定
    private	List<OrderState>	orderState;   // OrderStateオブジェクトのリスト

    /**********************************************
    * パラメーターなしのコンストラクター
    **********************************************/
    public OrderBooks(){}

    
    public OrderBooks(
        UserAccount account,
        String cust_name,
        String cust_mail,
        Integer cust_payment,
        String cust_demand,
        Date po_date,
        Integer total_amount,
        List<OrderState> orderState
    ){
        this.account      = account;
        this.cust_name    = cust_name;
        this.cust_mail    = cust_mail;
        this.cust_payment = cust_payment;
        this.cust_demand  = cust_demand;
        this.po_date      = po_date;
        this.total_amount = total_amount;
        this.orderState   = orderState;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UserAccount getAccount() {
        return account;
    }
    public void setAccount(UserAccount account) {
        this.account = account;
    }
    public String getCust_name() {
        return cust_name;
    }
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }
    public String getCust_mail() {
        return cust_mail;
    }
    public void setCust_mail(String cust_mail) {
        this.cust_mail = cust_mail;
    }
    public Integer getCust_payment() {
        return cust_payment;
    }
    public void setCust_payment(Integer cust_payment) {
        this.cust_payment = cust_payment;
    }
    public String getCust_demand() {
        return cust_demand;
    }
    public void setCust_demand(String cust_demand) {
        this.cust_demand = cust_demand;
    }
    public Integer getTotal_amount() {
        return total_amount;
    }
    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }
    public Date getPo_date() {
        return po_date;
    }
    public void setPo_date(Date po_date) {
        this.po_date = po_date;
    }
    public List<OrderState> getOrderState() {
        return orderState;
    }
    public void setOrderState(List<OrderState> orderState) {
        this.orderState = orderState;
    }
}
