package jp.co.systena.tigerscave.ShoppingCart.application.Controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.Item;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.ListForm;
import jp.co.systena.tigerscave.ShoppingCart.application.Model.ListService;

@Controller // Viewあり。Viewを返却するアノテーション
public class ListController {

  @RequestMapping("/ShoppingCart") // URLとのマッピング
  public ModelAndView show(HttpSession session,
                      ModelAndView mav,
                     @RequestParam(name = "sort_type", required = false) String sort_type) {
    /***************************
     * Viewに渡すデータを設定
     ***************************/
    // 商品一覧の作成
    // ※この時点ではまだ空っぽ
    ListService listservice = new ListService();

    // 商品の作成
    Item item01 = new Item("青森県産 リンゴ"  , 100);
    Item item02 = new Item("国産 バナナ"      , 200);
    Item item03 = new Item("山梨県産 ブドウ"  , 300);
    Item item04 = new Item("沖縄県産 マンゴー", 400);
    Item item05 = new Item("夕張メロン"       , 500);

    // 商品一覧にセット
    listservice.setitemList(item01);
    listservice.setitemList(item02);
    listservice.setitemList(item03);
    listservice.setitemList(item04);
    listservice.setitemList(item05);

    // 商品一覧の取得
    List<Item> itemlist = listservice.getItemList();


    /*****************************
     * ソート処理
     *****************************/
    // 商品の名前でソート
    // (utf-8でソートするため、一旦保留)
    //itemlist.sort((a,b)-> a.getitemName().compareTo(b.getitemName()));
    // 選択したタイプでソートする
    // デフォルトは安い順

    // ソートタイプ
    String sort_type_now;

    // セッションから現在のソートタイプを取得
    if ( session.getAttribute("sort_type") == null) {
      // 取得できない = 最初に商品一覧にアクセスするとき
      sort_type_now = "1";
    }else{
      if (sort_type == null) {
        // 現在のソートタイプを取得
        sort_type_now = session.getAttribute("sort_type").toString();
      }else {
        // 並び替えが選択されたとき
        sort_type_now = sort_type;
      }

    }

    if( sort_type_now.equals("1") ) {
      // 安い順
      itemlist.sort( (a,b)-> a.getitemPrice() - b.getitemPrice());
    }else{
      // 高い順
      itemlist.sort( (a,b)-> b.getitemPrice() - a.getitemPrice());
    }

    // 現在のソートタイプをセッションに保持
    session.setAttribute("sort_type", sort_type_now);

    // ソート処理 ～END～

    // modelに商品リストをセット
    mav.addObject("itemlist", itemlist);
    // 以下でも同じ
    //mav.addObject("itemlist", listservice.getItemList());

    // 0個を選択した場合は、セッションに保存されているメッセージをmodelに移してから、
    // セッション内のmessageを削除しておく
    if (session.getAttribute("message") != null) {
      mav.addObject("message", (String)session.getAttribute("message"));
      session.removeAttribute("message");
    }

    // 個数の設定
    HashMap<Integer, Integer> kosu_list = new HashMap<Integer, Integer>();

    for (int i = 1 ; i < 10 ; i++) {
       kosu_list.put(i, i);
    }

    mav.addObject("kosu_list", kosu_list);

    // 空のListFormを作成
    mav.addObject("ListForm", new ListForm());

    // Viewのテンプレート名を設定
    mav.setViewName("ListView");
    return mav;

  }

  /*
  // "カートに追加"ボタンを押した際の処理
  @RequestMapping(value="/ShoppingCart", params="cart=カートに追加") // URLとのマッピング
  public ModelAndView order(HttpSession session, ModelAndView mav,
                     @Valid ListForm listForm, BindingResult result) {

    // 変数設定
    String select_name = listForm.getName();
    int select_kosu  = listForm.getKosu();
    int select_price = listForm.getPrice();

    // 選択した個数が1未満だった場合、商品一覧画面に戻す。
    if(select_kosu == 0) {
      String message = "個数は1個以上を選択してください。";
      mav.addObject("message", message);
      return show(session, mav, "");

    }

    /***********************************
     * 選択した商品情報をカートへ追加
     ***********************************/

    /*

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
      // 注文情報をセット
      order_list.setOrderitem(order_meisai);
      // データをセッションへ保存
      session.setAttribute("orderList", order_list.getOrderitem());
    }else {
      // 注文情報をセット
      order.add(order_meisai);
      // データをセッションへ保存
      session.setAttribute("orderList", order);
    }

    /**************************
     * 合計金額の計算
     **************************/

    /*

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

    // Viewのテンプレート名を設定
    mav.setViewName("shopping_cart");
    return mav;

  }
  */

}
