package jp.co.systena.tigerscave.ShoppingCart.application.Model;

/*********************************
 * 商品クラス
 * １つの商品を表す。
 *********************************/
public class Item {

  private String name;
  private int price;


  // コンストラクタ
  public Item(String name, int price) {
    this.name = name;
    this.price = price;
  }

  /*
  //setter
  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(int price) {
    this.price = price;
  }
  */

  // getter
  public String getitemName() {
    return this.name;
  }

  public int getitemPrice() {
    return this.price;
  }

}
