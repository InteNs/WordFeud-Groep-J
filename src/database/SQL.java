package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String GAMESFORUSER = "SELECT * FROM spel WHERE account_naam_tegenstander = ? OR account_naam_uitdager = ?;";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String SELECTLETTERS = "SELECT * FROM lettertype WHERE letterset_code = ?";
    }

    public class ALL {
        public static final String USERS = "SELECT * FROM account;";
        public static final String GAMES = "SELECT * FROM spel;";
        public static final String COMPETITIONS = "SELECT * FROM competitie";
        public static final String ROLES = "SELECT * FROM accountrol";
        public static final String PLAYERSCOMPS = "SELECT * FROM deelnemer";
        public static final String WINSLOSES = "SELECT w.account_naam, wins,lost FROM rank_nr_wins w JOIN rank_nr_lost l ON w.account_naam = l.account_naam";
    }

    public class INSERT {
        public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
        public static final String  SETROLE = "INSERT INTO `wordfeud`.`accountrol` (`account_naam`, `rol_type`) VALUES (?, ?);";
        public static final String INSERTCOMPETITION = "INSERT INTO competitie (omschrijving, account_naam_eigenaar) VALUES (?, ?);";
        public static final String INSERTPLAYER = "INSERT INTO deelnemer (account_naam, competitie_id) VALUES (?, ?);";
    }
    
    public class DELETE {
        public static final String REMOVEROLE = "DELETE FROM `wordfeud`.`accountrol` WHERE `accountrol`.`account_naam` = ? AND `accountrol`.`rol_type` = ? ";
    }
    
    public class UPDATE {
        public static final String UPDATEPASSWORD = "UPDATE account SET wachtwoord = ? WHERE naam = ?";
                
    }
    
}
