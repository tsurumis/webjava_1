package jp.co.systena.tigerscave.ShoppingCart.application.Controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.Cart;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.DeleteForm;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.Item;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.ListForm;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.Order;

@Controller // Viewあり。Viewを返却するアノテーション
public class CartController {

  // "カートに追加"ボタンを押した際の処理
  @RequestMapping(value="/ShoppingCart", params="cart=カートに追加") // URLとのマッピング
  public ModelAndView order(HttpSession session, ModelAndView mav, Model model,
                     @Valid ListForm listForm, BindingResult result) {

    // 変数設定
    String select_name = listForm.getName();
    int select_kosu  = listForm.getKosu();
    int select_price = listForm.getPrice();

    // 選択した個数が1未満だった場合、商品一覧画面に戻す。
    if(select_kosu == 0) {
      String message = "個数は1個以上を選択してください。";
      //mav.addObject("message", message);
      session.setAttribute("message", message);
      return new ModelAndView("redirect:/ShoppingCart");

    }

    // "カートに商品を追加した旨"のメッセージの作成
    String message1 = "カートに " + select_name + " を " + select_kosu + " 個追加しました。";
    mav.addObject("message", message1);

    /***********************************
     * 選択した商品情報をカートへ追加
     ***********************************/
    // 注文した商品の情報を作成
    Item select_item = new Item(select_name, select_price);

    // 注文した商品の情報と個数を注文情報として作成
    Order order_meisai = new Order();
    order_meisai.setItem(select_item);
    order_meisai.setNum(select_kosu);


    // 現在の商品一覧を取得
    List<Order> order = (List<Order>)session.getAttribute("orderList");

    // 一つも注文商品がない場合は、注文配列を作成
    if( order == null) {
      // 新規の注文一覧を作成
      Cart order_list = new Cart();
      // indexをセット
      int syokai = 0;
      order_meisai.setIndex(syokai);
      // 注文情報をセット
      order_list.setOrderitem(order_meisai);
      // データをセッションへ保存
      session.setAttribute("orderList", order_list.getOrderitem());

      // 小計表示用の数字作成
      // 型変換（int型⇒Object型）
      Object tensu = new Integer(select_kosu);
      session.setAttribute("tensu", tensu);

    }else {

      // 現在の注文数を取得し、新しい注文にインデックスを付与する。
      int last_num = order.size();
      int new_index = last_num;
      order_meisai.setIndex(new_index);

      // 注文情報をセット
      order.add(order_meisai);
      // データをセッションへ保存
      session.setAttribute("orderList", order);

      /***************
       *  現在の点数を取得(小計の点数表示に使用)
       ***************/
      // 型変換（Object型⇒int型）
      int total_tensu = new Integer(session.getAttribute("tensu").toString());

      // 今までの合計金額に選択した商品の小計を加算
      total_tensu = total_tensu + select_kosu;

      // 型変換（int型⇒Object型）
      Object new_obj = new Integer(total_tensu);

      // セッションにセットする。
      session.setAttribute("tensu", new_obj);

    }

    /**************************
     * 合計金額の計算
     **************************/
    // 選択した商品の小計を計算
    int now_select_syokei = select_kosu * select_price;

    if( session.getAttribute("total_price") == null) {
      // 型変換（int型⇒Object型）
      Object obj = new Integer(now_select_syokei);
      session.setAttribute("total_price", obj);
    }else{
      // 現在の合計金額を取得
      // 型変換（Object型⇒int型）
      int total_price = new Integer(session.getAttribute("total_price").toString());

      // 今までの合計金額に選択した商品の小計を加算
      total_price = total_price + now_select_syokei;

      // 型変換（int型⇒Object型）
      Object obj = new Integer(total_price);

      // データをセッションへ保存
      session.setAttribute("total_price", obj);

    }

    // 選択した商品と個数、価格をmodelにセット(表示するため)
    mav.addObject("item", select_name);
    mav.addObject("price", select_price);
    mav.addObject("kosu", select_kosu);

    /*************************
     * 個数の設定
     *************************/
    HashMap<Integer, Integer> kosu_list = new HashMap<Integer, Integer>();

    for (int i = 1 ; i < 10 ; i++) {
       kosu_list.put(i, i);
    }

    mav.addObject("kosu_list", kosu_list);

    // 削除用空のFormの作成
    mav.addObject("DeleteForm", new DeleteForm());

    // Viewのテンプレート名を設定
    mav.setViewName("shopping_cart");
    return mav;

  }



  // "削除"を押した際の処理
  @RequestMapping(value="/ShoppingCart", params="cart=削除") // URLとのマッピング
  public ModelAndView delete(HttpSession session, ModelAndView mav, Model model,
      @Valid DeleteForm DeleteForm, BindingResult result) {

    // 現在の商品一覧を取得
    List<Order> order = (List<Order>)session.getAttribute("orderList");

    // indexを取得
    int index = DeleteForm.getIndex();

    // 削除データを取得
    Order remove_order = order.get(index);

    // 削除データの金額と個数を取得
    int remove_num = remove_order.getNum();
    int remove_price = remove_order.getItem().getitemPrice();
    int remove_total_price = remove_num * remove_price;

    // セッションの合計金額から、削除分を引く
    // 現在の合計金額を取得
    // 型変換（Object型⇒int型）
    int total_price = new Integer(session.getAttribute("total_price").toString());

    // 今までの合計金額に削除分の金額を引く
    total_price = total_price - remove_total_price;

    // 型変換（int型⇒Object型）
    Object obj = new Integer(total_price);

    // データをセッションへ保存
    session.setAttribute("total_price", obj);

    // indexのデータを削除
    order.remove(index);

    /************************
     * indexの振り直し
     * (もっと良い方法ないかね...)
     ************************/
    for (int i = 0 ; i < order.size() ; i++ ) {
      Order order01 = order.get(i);
      order01.setIndex(i);
    }

    // データをセッションへ保存
    session.setAttribute("orderList", order);
    
    /***************
     *  現在の点数を取得(小計の点数表示に使用)
     ***************/
    // 型変換（Object型⇒int型）
    int total_tensu = new Integer(session.getAttribute("tensu").toString());

    // 今までの小計に選択した商品の小計を引く
    total_tensu = total_tensu - remove_num;

    // 型変換（int型⇒Object型）
    Object new_obj = new Integer(total_tensu);
    
    // セッションにセット
    session.setAttribute("tensu", new_obj);

    /*************************
     * 個数の設定
     *************************/
    HashMap<Integer, Integer> kosu_list = new HashMap<Integer, Integer>();
    for (int i = 1 ; i < 10 ; i++) {
       kosu_list.put(i, i);
    }
    mav.addObject("kosu_list", kosu_list);

    // メッセージの作成
    String message = "カートから商品を1点削除しました。";
    mav.addObject("message", message);

    // 削除用空のFormの作成
    mav.addObject("DeleteForm", new DeleteForm());

    // Viewのテンプレート名を設定
    mav.setViewName("shopping_cart");
    return mav;
  }

}
