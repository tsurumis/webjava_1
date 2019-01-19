package jp.co.systena.tigerscave.ShoppingCart.application.Model;

/**************************************
 * 削除フォームクラス
 * 画面でのindexを取得しコントローラーに渡す
 **************************************/
public class DeleteForm {

  private int index;

  // setter
  public void setIndex(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }

}