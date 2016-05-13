package com.volkdem.cashdesc.buyer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.volkdem.cashdesc.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Evgeny on 28.04.2016.
 */
public class MainMenuAdapter extends BaseAdapter {

    private static List<MainMenuItem> menu = MainMenuItem.getMenu();

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainMenuItem item = menu.get( position );
        View view = null;

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        if ( MainMenuItem.isGroup( item ) ) {
            view = inflater.inflate(R.layout.main_menu_group_item, null );
        } else {
            view = inflater.inflate(R.layout.main_menu_action_item, null );
        }

        TextView text = (TextView) view.findViewById( R.id.main_menu_item_text );
        text.setText( item.getResourceTextId() );

        return view;
    }
}

enum MainMenuItem {
    SHOPPING(R.string.shopping),
    PURCHASE_HISTORY(R.string.purchase_history),
    BANK_CARDS(R.string.bank_cards ),
    PROFILE(R.string.profile),
    SETTINGS(R.string.settings),
    LOG_OUT(R.string.log_out);

    private int resourceId;

    private static List<MainMenuItem> menu = Collections.unmodifiableList( Arrays.asList(
            new MainMenuItem[] {
                    SHOPPING,
                    PURCHASE_HISTORY,
                    BANK_CARDS,
                    PROFILE,
                    SETTINGS,
                    LOG_OUT
            }) );

    private static Collection groups = Arrays.asList(
            new MainMenuItem[] {
                    SHOPPING,
                    PROFILE
            }
    );

    private MainMenuItem(int resourceId) {
        this.resourceId = resourceId;
    }

    public static List<MainMenuItem> getMenu() {
        return menu;
    }

    public static boolean isGroup( MainMenuItem item ) {
        return groups.contains( item );
    }

    public int getResourceTextId() {
        return resourceId;
    }
}
