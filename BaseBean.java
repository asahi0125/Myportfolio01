package beans;

import enterprise.FetchOrderBooks;
import enterprise.FetchBookData;
import enterprise.*;
import enterprise.BookDataFacade;
import enterprise.OrderBooksFacade;
import entity.*;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import utility.BinaryDataUtility;
import utility.Pagenate;

public class BaseBean implements Serializable {

    protected Genre selectedGenreItem = Genre.NONE; // 選択中のジャンル
    protected Integer selectedSortItem = 1;	    // 選択中のソート条件
    protected Map<String, Integer> sortItems;       // ソート方法を選択するためのアイテム	
    protected Map<String, Genre> genreItems;        // ジャンルを選択するためのアイテム
    protected List<OrderState> cart;                // カートの中身を保持
    protected BookData disp_detail;                 // 書籍の詳細画面に表示するデータ						
    protected String customer_name;		    // 注文者の氏名
    protected String customer_address;              // 注文者の住所
    protected String customer_mail;		    // 注文者のメールアドレス
    protected String customer_msg;                  // 注文者からのメッセージ
    protected Integer selectedPayItem = 1;	    // 選択中の支払い方法
    protected Map<String, Integer> payItem;         // 支払い方法を選択するためのアイテム

    /**********************************************
    * EJBのインジェクト
    **********************************************/
    @EJB
    BookDataFacade bookDataFacade;                  // 書籍データ(BOOKテーブル)
    @EJB
    UserAccountFacade userAccountFacade;            // アカウント(Accountテーブル)
    @EJB
    OrderBooksFacade orderBooksFacade;              // 注文履歴(ORDERテーブル)
    @EJB
    OrderStateFacade orderStateFacade;              // 注文状況(ORDERSTATEテーブル）
    @EJB
    FetchBookData fetchBookData;                    // 書籍データの操作
    @EJB
    FetchOrderBooks fetchOrderBooks;                // 購入履歴の検索

    /**********************************************
    * ページング用ユーティリティのインジェクト
    **********************************************/
    @Inject
    protected Pagenate pagenate;

    /**********************************************
    * Loggerの生成
    **********************************************/
    static final Logger log = Logger.getLogger(
                            BinaryDataUtility.class.getName());

    /**********************************************
    * ライフサイクル・コールバックメソッド
    * インジェクト完了後に以下の処理を実行
    * 
    * *ソート方法の選択用アイテムを生成
    * *ジャンル選択用アイテムを生成
    * *ショッピングカート用のArrayListを用意
    * *Pagenateの初期化
    **********************************************/
    @PostConstruct
    public void init() {
        // ソート方法の選択用アイテムを生成
        sortItems = new LinkedHashMap<>();
        sortItems.put("なし", 1);
        sortItems.put("安い順番", 2);
        sortItems.put("高い順番", 3);

        // ジャンル選択用アイテムを生成
        genreItems = new LinkedHashMap<>();
        genreItems.put("すべて", Genre.NONE);
        genreItems.put("IT関連書", Genre.JENRE1);
        genreItems.put("ノンフィクション", Genre.JENRE2);
        genreItems.put("ハウツー", Genre.JENRE3);

        // ショッピングカート用のArrayListを用意
        cart = new ArrayList<>();
        // Pagenateの初期化
        pagenate.initPagination(bookDataFacade.count(), 5);

        // 支払い方法選択用アイテムを生成
        payItem = new LinkedHashMap<>();
        payItem.put("着払い", 1);	
        payItem.put("銀行振込み", 2);
    }
    /**********************************************
    * データベースからサムネイル用の画像データを読み込んで表示する
    * @return    DefaultStreamedContentオブジェクト
    **********************************************/
    public StreamedContent getThumbnail() {
        // FacesContextのインスタンスを取得
        FacesContext context = FacesContext.getCurrentInstance();
        if (
            // ライフサイクルのプロセス検証フェーズであるか
            context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE
        ){
            // HTMLの<img>タグをレスポンス用のHTMLに出力
            return new DefaultStreamedContent();
        } else {
            // 画像のリクエストであれば画像データを返す
            // ExternalContextオブジェクトを取得
            ExternalContext external = context.getExternalContext();
            // リクエスト時に送信されたパラメーターをMap型で取得
            Map<String, String> map = external.getRequestParameterMap();
            // Map型の連想配列からbook_idパラメーター（主キー）の値を取得
            String value = map.get("book_id");

            // 取得した主キーの値を指定してfind()メソッドを実行
            // 対象のレコードを保持するエンティティを取得
            BookData entity = (BookData) (
                bookDataFacade.find(Long.valueOf(value))
            );

            // エンティティのgetImg1()メソッドでサムネイルのデータを取得し、
            // 入力ストリームオブジェクトに格納
            ByteArrayInputStream in =
                    new ByteArrayInputStream(entity.getImg1());
            // 入力ストリームオブジェクトをコンテンツの
            // ストリームオブジェクトに格納
            DefaultStreamedContent ds =
                    new DefaultStreamedContent(in);
            // DefaultStreamedContentオブジェクトを返す
            return ds;
        }
    }
    /**********************************************
    * データベースから拡大表示用の画像データを読み込んで表示する
    * @return    DefaultStreamedContentオブジェクト
    **********************************************/
    public StreamedContent getImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (
            context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE
        ){
            return new DefaultStreamedContent();
        } else {
            ExternalContext external = context.getExternalContext();
            Map<String, String> map = external.getRequestParameterMap();
            String value = map.get("book_id");
            BookData entity = (BookData) (
                bookDataFacade.find(Long.valueOf(value))
            );
                // エンティティのgetImg2())ソッドで拡大表示用の画像
                // データを取得し、入力ストリームオブジェクトに格納
                ByteArrayInputStream in =
                        new ByteArrayInputStream(entity.getImg2());
                DefaultStreamedContent ds =
                        new DefaultStreamedContent(in);
                return ds;
        }
    }
    /**********************************************
    * メッセージを作成しFacesContextにセットする
    * @param m  メッセージ
    **********************************************/
    public void setMessage(String m) {
        FacesMessage msg = new FacesMessage(m);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    /**********************************************
    * エラーメッセージを作成しFacesContextにセットする
    * @param m  メッセージ
    **********************************************/
    public void setErrorMessage(String m) {
        FacesMessage msg = new FacesMessage(m);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    /**********************************************
    * Servletのコンテキスト（ExternalContext）を取得する
    * @return　　Servletのコンテキスト（ExternalContext）
    **********************************************/
    public ExternalContext getServletContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    /**********************************************
    * リクエストオブジェクトを取得する
    * @return　　リクエストオブジェクト（Object型をHttpServletRequest型にキャスト）
    **********************************************/
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) getServletContext().getRequest();
    }
    /**********************************************
    * ログインしているユーザーのIDを取得する
    * @return　　ログインユーザーのID（String）
    **********************************************/
    public String getUserId() {
        return getRequest().getRemoteUser();
    }
    /**********************************************
    * セッター／ゲッター
    * @return
    **********************************************/
    public List<OrderState> getCart() {
            return cart;
    }
    public void setCart(List<OrderState> cart) {
        this.cart = cart;
    }
    public BookData getDisp_detail() {
        return disp_detail;
    }
    public void setDisp_detail(BookData disp_detail) {
        this.disp_detail = disp_detail;
    }
    public Integer getSelectedSortItem() {
        return selectedSortItem;
    }
    public void setSelectedSortItem(Integer selectedSortItem) {
        this.selectedSortItem = selectedSortItem;
    }
    public Map<String, Integer> getSortItems() {
        return sortItems;
    }
    public Genre getSelectedGenreItem() {
        return selectedGenreItem;
    }
    public void setSelectedGenreItem(Genre selectedGenreItem) {
        this.selectedGenreItem = selectedGenreItem;
    }
    public Map<String, Genre> getGenreItems() {
        return genreItems;
    }
    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public String getCustomer_address() {
        return customer_address;
    }
    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }
    public String getCustomer_mail() {
        return customer_mail;
    }
    public void setCustomer_mail(String customer_mail) {
        this.customer_mail = customer_mail;
    }
    public String getCustomer_msg() {
        return customer_msg;
    }
    public void setCustomer_msg(String customer_msg) {
        this.customer_msg = customer_msg;
    }
    public Pagenate getPagenate() {
        return pagenate;
    }
    public void setPagenate(Pagenate pagenate) {
        this.pagenate = pagenate;
    }
    public Integer getSelectedPayItem() {
        return selectedPayItem;
    }
    public void setSelectedPayItem(Integer selectedPayItem) {
        this.selectedPayItem = selectedPayItem;
    }
    public Map<String, Integer> getPayItem() {
        return payItem;
    }
    public void setPayItem(Map<String, Integer> payItem) {
        this.payItem = payItem;
    }
}

