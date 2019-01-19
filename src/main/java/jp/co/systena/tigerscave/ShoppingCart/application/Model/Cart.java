package jp.co.systena.tigerscave.ShoppingCart.application.Model;

import java.util.ArrayList;
import java.util.List;

/**************************
 * カートクラス
 * 注文内容を入れておく。
 **************************/
public class Cart {

  private List<Order> orderlist = new ArrayList<Order>();

  public void setOrderitem(Order order) {
    this.orderlist.add(order);
  }

  public List<Order> getOrderitem(){
    return this.orderlist;
  }

}
