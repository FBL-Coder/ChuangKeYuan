package com.yitong.ChuangKeYuan.friendscircle.widgets.videolist.visibility.scroll;


import com.yitong.ChuangKeYuan.friendscircle.widgets.videolist.visibility.items.ListItem;

/**
 * This interface is used by {@link com.waynell.videolist.visibility.calculator.SingleListViewItemActiveCalculator}.
 * Using this class to get {@link com.waynell.videolist.visibility.items.ListItem}
 *
 * @author Wayne
 */
public interface ItemsProvider {

    ListItem getListItem(int position);

    int listItemSize();

}
