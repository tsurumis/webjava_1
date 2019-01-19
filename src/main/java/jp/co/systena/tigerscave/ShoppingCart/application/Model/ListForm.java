package jp.co.systena.tigerscave.ShoppingCart.application.Model;

/**************************************
 * フォームクラス
 * 画面での入力内容を保持し、
 * コントローラとの受け渡しに使用する。
 **************************************/
public class ListForm {

  //@NotNull
  //@Size(min = 1, max = 127)
  private String name;

  private int price;

  private int kosu;

  // setter
  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setKosu(int kosu) {
    this.kosu = kosu;
  }

  // getter
  public String getName() {
    return this.name;
  }

  public int getPrice() {
    return this.price;
  }

  public int getKosu() {
    return this.kosu;
  }

}
