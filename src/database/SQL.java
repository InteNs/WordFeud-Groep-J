package database;
//language=MySQL

public class SQL {
    public class SELECT {
        public static final String LETTERSFORGAME = "SELECT\n  letter.*,\n  lettertype.waarde\nFROM letter\n\n  JOIN lettertype\n    ON letter.lettertype_letterset_code = lettertype.letterset_code AND letter.lettertype_karakter = lettertype.karakter\nWHERE spel_id = ?";
        public static final String TURNSFORGAME = "SELECT\n  spel.id           spel,\n  beurt.id          beurt,\n  beurt.aktie_type,\n  beurt.score,\n  beurt.account_naam,\n  placed.letter_id  gelegd_id,\n  plankje.letter_id plank_id,\n  placed.blancoletterkarakter,\n  placed.tegel_x,\n  placed.tegel_y\nFROM spel\n  LEFT JOIN beurt ON beurt.spel_id = spel.id\n  LEFT JOIN gelegdeletter placed ON placed.beurt_id = beurt.id AND placed.spel_id = spel.id\n  LEFT JOIN letterbakjeletter plankje ON beurt.id = plankje.beurt_id AND beurt.spel_id = plankje.spel_id\n\nWHERE spel.id = ?;";
        public static final String TILESFORBOARD = "SELECT * FROM tegel WHERE bord_naam =?";
        public static final String MESSAGESFORGAME = "SELECT * FROM chatregel WHERE spel_id = ? ORDER BY tijdstip;";
        public static final String USERWITHCREDS = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r ON a.naam = r.account_naam\nWHERE naam = ?\n      AND wachtwoord = ?";
        public static final String USERWITHNAME = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r ON a.naam = accountrol.account_naam\nWHERE a.naam = ?";
        public static final String COMPFOROWNER = "SELECT id FROM competitie WHERE account_naam_eigenaar = ?";
        public static final String LETTERSFORNEWGAME = "SELECT lettertype.karakter, lettertype.aantal FROM lettertype WHERE letterset_code = ?";
        public static final String POT = "SELECT pot.letter_id, lettertype.waarde, pot.karakter FROM pot LEFT JOIN lettertype ON pot.karakter = lettertype.karakter WHERE spel_id=? AND lettertype.letterset_code=? GROUP BY letter_id;";
        public static final String TOPPLAYERS = "SELECT account_naam, aantal_gewonnen_spellen FROM competitiestand WHERE competitie_id = ? ORDER BY aantal_gewonnen_spellen DESC LIMIT 5;";
    }

    public class ALL {
        public static final String USERS = "SELECT\n  a.naam,\n  a.wachtwoord,\n  r.rol_type\nFROM account a\n  LEFT JOIN accountrol r on a.naam = r.account_naam";
        public static final String GAMES = "SELECT spel.*, MAX(beurt.id) AS last_turn, score.totaalscore, score.account_naam\nFROM spel\nLEFT JOIN beurt ON spel.id = beurt.spel_id\n  LEFT JOIN score ON score.spel_id = spel.id\nGROUP BY score.account_naam, spel.id\nORDER BY id";
        public static final String COMPETITIONS = "SELECT\n  C.id,\n  C.omschrijving,\n  C.account_naam_eigenaar,\n  AVG(R.gemiddelde_score) AS gemiddelde_score\nFROM competitie AS C LEFT JOIN rank_avg AS R ON C.id = R.competitie_id\nGROUP BY C.id;";
        public static final String PLAYERSCOMPS = "SELECT * FROM deelnemer";
        public static final String STATS = "SELECT * FROM competitiestand";
        public static final String WORDS = "SELECT * FROM woordenboek WHERE account_naam != 'bookowner'";
    }

    public class INSERT {
        public static final String INSERTTURN = "INSERT INTO beurt(id, spel_id, account_naam, score, aktie_type) VALUES (?,?,?,?,?);";
        public static final String INSERTPLACEDTILES = "INSERT INTO gelegdeletter(letter_id, spel_id, beurt_id, tegel_x, tegel_y, tegel_bord_naam, blancoletterkarakter) VALUES";
        public static final String INSERTRACKTILES = "INSERT INTO letterbakjeletter(spel_id, letter_id, beurt_id) VALUES";
        public static final String INSERTUSER = "INSERT INTO account (naam, wachtwoord) VALUES (?, ?)";
        public static final String SETROLE = "INSERT INTO accountrol  (`account_naam`, `rol_type`) VALUES (?, ?);";
        public static final String INSERTCOMPETITION = "INSERT INTO competitie (omschrijving, account_naam_eigenaar) VALUES (?, ?);";
        public static final String INSERTPLAYER = "INSERT INTO deelnemer (account_naam, competitie_id) VALUES (?, ?);";
        public static final String INSERTMESSAGE = "INSERT INTO chatregel (chatregel.spel_id,chatregel.account_naam,chatregel.bericht) VALUES (?,?,?);";
        public static final String CREATEGAME = "INSERT INTO spel (competitie_id, toestand_type, account_naam_uitdager, reaktie_type, bord_naam, letterset_naam, account_naam_tegenstander) VALUES (?, 'request', ?, 'unknown', 'standard', ?, ?);";
        public static final String LETTERSFORPOT = "INSERT INTO letter(id, spel_id, lettertype_letterset_code, lettertype_karakter) VALUES";
        public static final String INSERTWORD = "INSERT INTO woordenboek (`woord`, `letterset_code`, `status`, `account_naam`) VALUES (?, ?, ?, ?);";
    }

    public class DELETE {
        public static final String REMOVEROLE = "DELETE FROM accountrol WHERE `accountrol`.`account_naam` = ? AND `accountrol`.`rol_type` = ? ";
    }

    public class UPDATE {
        public static final String UPDATEPASSWORD = "UPDATE account SET wachtwoord = ? WHERE naam = ?";
        public static final String UPDATEWORDSTATUS = "UPDATE woordenboek SET status = ? WHERE woord = ? AND account_naam = ?";
        public static final String UPDATEGAMESTATE = "UPDATE spel SET toestand_type = ? WHERE id = ?";
        public static final String UPDATEREACTIONTYPE = "UPDATE spel SET reaktie_type = ? WHERE id = ?";

    }

    public class COUNT{
        public static final String COUNTWORDS = "SELECT COUNT(woordenboek.woord ) FROM woordenboek WHERE woord =? AND woordenboek.letterset_code=?;";
    }
}
