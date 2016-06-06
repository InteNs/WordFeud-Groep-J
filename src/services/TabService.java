package services;

import enumerations.Role;
import models.User;
import views.MainView;

import java.util.Objects;

/**
 * Created by Christoph on 6-6-2016.
 */
public class TabService {
    public static void hideForbiddenTabs(MainView mainView, User user){
        // games tab
        if ((user.hasRole(Role.ADMINISTRATOR) || user.hasRole(Role.MODERATOR)) && !user.hasRole(Role.PLAYER) && !user.hasRole(Role.OBSERVER)){
            mainView.control.getTabs().remove(mainView.gameListView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.gameListView)){
                mainView.control.getTabs().add(mainView.gameListView);
            }
        }

        // accounts tab
        if (user.hasRole(Role.MODERATOR) && !user.hasRole(Role.ADMINISTRATOR) && !user.hasRole(Role.PLAYER) && !user.hasRole(Role.OBSERVER)){
            mainView.control.getTabs().remove(mainView.userListView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.userListView)){
                mainView.control.getTabs().add(mainView.userListView);
            }
        }
        // competitions tab
        if ((user.hasRole(Role.ADMINISTRATOR) || user.hasRole(Role.MODERATOR)) && !user.hasRole(Role.PLAYER) && !user.hasRole(Role.OBSERVER)){
            mainView.control.getTabs().remove(mainView.competitionListView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.competitionListView)){
                mainView.control.getTabs().add(mainView.competitionListView);
            }
        }
        // invitations tab
        if ((user.hasRole(Role.ADMINISTRATOR) || user.hasRole(Role.MODERATOR) || user.hasRole(Role.OBSERVER)) && !user.hasRole(Role.PLAYER)){
            mainView.control.getTabs().remove(mainView.challengeListView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.challengeListView)){
                mainView.control.getTabs().add(mainView.challengeListView);
            }
        }
        // words tab
        if ((user.hasRole(Role.ADMINISTRATOR) || user.hasRole(Role.OBSERVER)) && !user.hasRole(Role.PLAYER) && !user.hasRole(Role.MODERATOR)){
            mainView.control.getTabs().remove(mainView.wordListView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.wordListView)){
                mainView.control.getTabs().add(mainView.wordListView);
            }
        }
        // gamecontrol tab
        if ((user.hasRole(Role.ADMINISTRATOR) || user.hasRole(Role.MODERATOR)) && !user.hasRole(Role.PLAYER) && !user.hasRole(Role.OBSERVER)){
            mainView.control.getTabs().remove(mainView.gameControlView);
        } else {
            if (!mainView.control.getTabs().contains(mainView.gameControlView)){
                mainView.control.getTabs().add(mainView.gameControlView);
            }
        }
        // no roles no tabs
        if (user.getRoles().filtered(role -> role != null).isEmpty()){
            mainView.control.getTabs().clear();
        }
    }
}
