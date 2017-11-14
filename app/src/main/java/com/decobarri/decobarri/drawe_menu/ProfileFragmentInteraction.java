package com.decobarri.decobarri.drawe_menu;

import com.decobarri.decobarri.db_resources.User;

/**
 * Created by Asus on 09/11/2017.
 */

public interface ProfileFragmentInteraction {
    void ChangeFragment (Integer mode);//mode: 1(edit) 2(save) 3(cancel) 4(editpassword)
    boolean EditUser(User u);
    boolean EditPassword(String username, String old_password, String new_password);

}
