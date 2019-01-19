package jp.co.systena.tigerscave.ShoppingCart.application.Model;

/******************************
 * 注文クラス
 * 1回の注文を表す。
 ******************************/
public class Order {

  private Item item;

  // 個数
  private int num;

  // 何番目の注文か？
  private int index;

  //setter
  public void setNum(int num) {
    this.num = num;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void setIndex(int index) {
    this.index = index;

  }

  // getter
  public int getNum() {
    return this.num;
  }

  public Item getItem() {
    return this.item;
  }

  public int getIndex() {
    return this.index;
  }

}
