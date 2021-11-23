package beans;

import entity.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class OerderHandler extends BaseBean implements Serializable {
 	/**********************************************
	* index_content.xhtmlから呼び出すメソッド
 	/**********************************************
 	/**********************************************
	* ショッピングカートの中身を表示する
	* @return　　cart.xhtml
	**********************************************/
	public String showCart() {
            if (cart.isEmpty()) {
                setMessage("ショッピングカートに商品はありません。");
                return "index";			// index.xhtmlにリダイレクト
            }
            return "cart?faces-redirect=true";	// cart.xhtmlにリダイレクト
	}
 	/**********************************************
	* 書籍の詳細ページを表示する
	* @param   book  詳細を表示する書籍のデータ
	* @return　　detail.xhtml
	**********************************************/
	public String detail(BookData book) {
            disp_detail = book;                     // 表示するデータをフィールドにセット
            return "detail?faces-redirect=true";    // detail.xhtmlにリダイレクト
	}
 	/**********************************************
	* 1ページに表示する書籍データを取得する
	* @return　　BookDataオブジェクトのリスト
	**********************************************/
	public List<BookData> getBookRecord() {
            List<BookData> bookList = null;
            try {
                bookList = fetchBookData.getBookList(selectedSortItem,
                                             selectedGenreItem,
                                             pagenate);
            } catch (Exception e) {
                setMessage("書籍情報の取得に失敗しました");
            }
            return bookList;
	}
 	/**********************************************
	* 書籍の全レコードの件数と1ページ当たりの表示件数を再設定する
	**********************************************/
	public void resetRecordsNumber() {
            try {
                fetchBookData.preparePage(selectedGenreItem, pagenate);
            } catch (Exception e) {
                setMessage("書籍情報を表示できません");
            }
	}
 	/**********************************************
	* detail_content.xhtmlから呼び出すメソッド
 	/**********************************************
 	/**********************************************
	* ショッピングカートに商品を追加する
	* @return　　cart.xhtml
	**********************************************/
	public String addToCart() {
            addNewOrder();
            return "cart?faces-redirect=true";  // cart.xhtmlにリダイレクト
	}
 	/**********************************************
	* ショッピングカートに同じ書籍があれば冊数を追加、なければ新規に追加する
	* @return　　OrderStateオブジェクト
	**********************************************/
	public OrderState addNewOrder() {
            // 詳細ページを表示している書籍のidを取得
            Long disp_detail_id = disp_detail.getId();
            // ショッピングカートから明細（OrderState）を取り出し順次、処理を行う
            for (OrderState orderStateInCart : cart) {
                // OrderStateから書籍データを取り出す	
                BookData bookInCart = orderStateInCart.getBookData();
                // ショッピングカートに入っている書籍と追加された書籍を比較する
                if (Objects.equals(bookInCart.getId(), disp_detail_id)) {
                    orderStateInCart.adding();	// 同じなら冊数を1加算する
                    return orderStateInCart;	// OrderStateオブジェクトを返す
                }
            }
	// 追加する書籍がショッピングカートに存在しない場合の処理
        // OrderStateオブジェクトを生成
        OrderState newOrder = new OrderState(disp_detail, 1);
        // ショッピングカートに追加する
        cart.add(newOrder);
        // OrderStateオブジェクトを返す
        return newOrder;
	}
 	/**********************************************
	* cart_content.xhtmlから呼び出すメソッド
 	/**********************************************
 	/**********************************************
	* 注文画面を表示
	* @return　　orderForm.xhtml、カートが空ならcart.xhtml
	**********************************************/
	public String redirectOrderPage() {
            // ショッピングカートが空であればメッセージを表示してcart.xhtmlへリダイレクト
            if (cart.isEmpty()) {
                setMessage("現在、カートは空です。");
                return "cart";
            }
            return "orderForm?faces-redirect=true";     // 注文画面へリダイレクト
	}
 	/**********************************************
	* ショッピングカートを空にする
	* @param   order  OrderStateオブジェクト
	* @return　　NULL
	**********************************************/
	public String deleteOrder(OrderState order) {
            cart.remove(order);
            return null;
	}
 	/**********************************************
	* orderForm_content.xhtmlから呼び出すメソッド
 	**********************************************/
 	/**********************************************
	* 注文内容を保存する
	* @return　　NULL
	**********************************************/
	public String buy() {
            String login_user = getUserId();	// ログイン中のユーザーidを取得
            if (login_user == null) {
                login_user = "guest";		// ログイン状態でなければguestユーザーとして扱う
            }
            registerOrder(login_user);		// 注文内容をORDERBOOKテーブルに保存
            emptyCart();				// ショッピングカートを空にする
            return "confirm?faces-redirect=true";	// confirm.xhtmlへリダイレクト
	}
 	/**********************************************
	* 注文内容をORDERBOOKテーブルに保存する
	* @param   user_id  注文者のユーザーID
	**********************************************/
	public void registerOrder(String user_id) {
            // 注文したユーザーのidを取得
            UserAccount user_account = (UserAccount) userAccountFacade.find(user_id);
            int total = 0;
            // 合計金額を求める（ラムダ式使用）
            total = cart.stream()
                    .map(
                        (state) -> state.getQuantity() * state.getBookData().getPrice()
                    )
                    .reduce(total, Integer::sum);
            OrderBooks currentOrder = new OrderBooks(user_account,
                                            customer_name,   // 受取人名
                                            customer_mail,   // メールアドレス
                                            selectedPayItem, // 支払い方法
                                            customer_msg,    // 要望等
                                            new Date(),      // 注文日
                                            total,           // 購入金額
                                            cart);
            orderBooksFacade.create(currentOrder);
	}
 	/**********************************************
	* 注文ページのフィールドをクリアし、ショッピングカートを空にする
	**********************************************/
	public void emptyCart() {
            // 注文ページのフィールドをクリア
            customer_name = customer_address
                          = customer_mail
                          = customer_msg
                          = null;
            // ショッピングカートを空にする
            cart.clear();
	}
 	/**********************************************
	* history_content.xhtmlから呼び出すメソッド
 	/**********************************************
 	/**********************************************
	* 注文履歴を取得する
	* @return　　過去の注文データを格納したOrderBooksのリスト
	**********************************************/
	public List<OrderBooks> getOrderHistory() {
            List<OrderBooks> orderHistory = new ArrayList<>();
            try {
                orderHistory = fetchOrderBooks.getHistory(getUserId());
            } catch (Exception e) {
                setMessage("注文履歴を取得することができません");
            }
            return orderHistory;
	}
}

