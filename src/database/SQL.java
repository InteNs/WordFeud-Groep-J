package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String LETTERSFORLANG = "SELECT * FROM lettertype WHERE letterset_code = ?;";
        public static final String TURNSFORGAME = "SELECT b.*, g.woorddeel, g.`x-waarden`, g.`y-waarden`, p.inhoud FROM beurt b LEFT JOIN gelegd g ON g.beurt_id = b.id AND g.spel_id = b.spel_id LEFT JOIN plankje p ON p.beurt_id = b.id AND p.spel_id = b.spel_id WHERE b.spel_id = ?;\n";
        public static final String TILESFORBOARD = "SELECT * FROM wordfeud.tegel WHERE bord_naam =?";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String USERWITHCREDS = "SELECT a.naam, a.wachtwoord, r.rol_type FROM account a, accountrol r WHERE a.naam = r.account_naam AND a.naam = ? AND a.wachtwoord = ? ORDER BY a.naam;";
        public static final String USERWITHNAME = "SELECT a.naam, a.wachtwoord, r.rol_type FROM account a, accountrol r WHERE a.naam = r.account_naam AND a.naam = ? ORDER BY a.naam;";
        public static final String COMPFOROWNER = "SELECT id FROM competitie WHERE account_naam_eigenaar = ?";
    }

    public class ALL {
        public static final String USERS = "SELECT a.naam, a.wachtwoord, r.rol_type FROM account a, accountrol r WHERE a.naam = r.account_naam ORDER BY a.naam;";
        public static final String GAMES = "SELECT * FROM spel;";
        public static final String COMPETITIONS = "SELECT C.id, C.omschrijving, C.account_naam_eigenaar, AVG(R.gemidddelde_score) AS gemidddelde_score FROM competitie AS C LEFT JOIN rank_avg AS R ON C.id = R.competitie_id GROUP BY C.id;";
        public static final String PLAYERSCOMPS = "SELECT * FROM deelnemer";
        public static final String WINSLOSES = "SELECT w.account_naam, wins,lost FROM rank_nr_wins w JOIN rank_nr_lost l ON w.account_naam = l.account_naam";
    }

    public class INSERT {
        public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
        public static final String SETROLE = "INSERT INTO `wordfeud`.`accountrol` (`account_naam`, `rol_type`) VALUES (?, ?);";
        public static final String INSERTCOMPETITION = "INSERT INTO competitie (omschrijving, account_naam_eigenaar) VALUES (?, ?);";
        public static final String INSERTPLAYER = "INSERT INTO deelnemer (account_naam, competitie_id) VALUES (?, ?);";
        public static final String INSERTMESSAGE = "INSERT INTO wordfeud.chatregel (chatregel.spel_id,chatregel.account_naam,chatregel.bericht) VALUES (?,?,?);";
    }
    
    public class DELETE {
        public static final String REMOVEROLE = "DELETE FROM `wordfeud`.`accountrol` WHERE `accountrol`.`account_naam` = ? AND `accountrol`.`rol_type` = ? ";
    }
    
    public class UPDATE {
        public static final String UPDATEPASSWORD = "UPDATE account SET wachtwoord = ? WHERE naam = ?";
                
    }
    
}
