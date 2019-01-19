package jp.co.systena.tigerscave.ShoppingCart.application.Model;

import java.util.ArrayList;
import java.util.List;

/*****************************************
 * サービスクラス
 * 画面に依存しないロジック部分を記述する。
 *****************************************/
public class ListService {

  private List<Item> itemlist = new ArrayList<Item>();

  public void setitemList(Item item) {
    this.itemlist.add(item);
  }

  public List<Item> getItemList(){
    return itemlist;
  }

}
