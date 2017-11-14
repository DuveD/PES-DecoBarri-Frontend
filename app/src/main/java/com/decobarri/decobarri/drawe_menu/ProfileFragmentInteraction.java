package com.decobarri.decobarri.drawe_menu;

import com.decobarri.decobarri.db_resources.User;

/**
 * Created by Asus on 09/11/2017.
 */

public interface ProfileFragmentInteraction {
    Integer ProfileInteraction(Integer mode, User u, String old_password); //mode: 1(edit) 2(save) 3(cancel)
}
