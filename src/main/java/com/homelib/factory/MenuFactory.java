package com.homelib.factory;

import com.homelib.enums.MenuType;
import com.homelib.utils.AdminMenu;
import com.homelib.utils.MainMenu;
import com.homelib.utils.Menu;
import com.homelib.utils.ServiceMenu;

public class MenuFactory  {
    public static Menu getMenu(MenuType menu){
        switch (menu){
            case MAIN -> {
                return new MainMenu();
            }
            case ADMIN -> {
                return new AdminMenu();
            }
            case SERVICE_OPERATIONS -> {
                return new ServiceMenu();
            }
            default -> throw new IllegalArgumentException("Menu não encontrado");
        }

    }
}
