package utility;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

@SessionScoped						// セッションスコープ
public class Pagenate implements Serializable {

	private int numOfData;			// データ件数
	private int per;				// ページ当たリの表示件数
	private int maxPage;			// 総ページ数
	private int current;			// 現在のページ

	
	public void initPagination(int num, int per) {
		this.numOfData = num;
		this.per       = per;
		this.maxPage   = num/per + (num % per == 0 ? 0 : 1); 
		this.current   = 1;
	}
	
	public	int findTopData(){
		return	(current-1) * per ;
	}
	/**********************************************
	* 次ページを表示
	**********************************************/
	public void moveNext() {
		if(maxPage > current) ++current;
	}
	/**********************************************
	* 前ページを表示
	**********************************************/
	public void movePrev() {
		if(current > 1 ) --current;
	}
	/**********************************************
	* 末尾のページを表示
	**********************************************/
	public void moveLast() {
		current	= maxPage;
	}
	/**********************************************
	* 先頭のページを表示
	**********************************************/
	public void moveTop(){
		current  = 1;
	}
	/**********************************************
	* 表示するデータの範囲を返す
	* @return  データの先頭位置と最後尾を示す配列
	**********************************************/
	public	int[] ProcRange(){
		int[] n = new int[2];
		n[0] = findTopData();
		n[1] = n[0] + per - 1;
		return	n;
	}
    // ゲッター
    public int getCurrent() {
        return current;
    }
	public	int	getPer(){
		return	per;
	}
    public int getNumOfData() {
        return numOfData;
    }

    public int getMaxPage() {
        return maxPage;
    }
}
