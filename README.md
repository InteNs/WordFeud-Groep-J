# WordFeud
Informatie over alles wat met de applicatie te maken heeft.
## structuur van applicatie

see issue [#28](https://github.com/InteNs/WordFeud-Groep-J/issues/28)

## MySql database
Server ip 	: 77.172.146.212

Server port    : 3306

Username 	: wordfeud

Password 	: wordfeud01

Database 	: wordfeud  

[download-link MySql connector](https://dev.mysql.com/downloads/connector/j/)

## Java
[documentatie java-FX](https://docs.oracle.com/javase/8/javase-clienttechnologies.htm)

[download-link scene-builder](http://gluonhq.com/open-source/scene-builder/)

[Junit4 in eclipse](https://stackoverflow.com/questions/1994308/junit4-in-eclipse)

## Git
[tutorial ssh](https://help.github.com/articles/generating-an-ssh-key/)

[alias config voor commands](http://pastebin.com/CaAkZmDJ)   **Sterk aan te raden!**

Alias plakken in `/Users/<naam>/.gitconfig`.

### GitFlow: (met alias)
Doe tussen de stappen veel controle of alles goed gaat:
```bash
git l               //lijst van commits
git s               //lijst van bestanden
```
```bash
//zonder alias:
git status
```



Voor problemen en vragen naar Cedric of Mark.

1. ga naar master en haal de nieuwste versie op

   ```bash
   git co master
   git pull
   ```
   ```bash
   //zonder alias:
   git checkout master
   git pull
   ```

2. schrijf je java code

3. als je klaar ben met de code, haal nog een keer master op en los eventuele conflicten op.

   ```bash
   git pull
   //los conflicten op (in je bestand die een conflict heeft)
   ```

4. zet al je bestanden klaar voor de commit

   ```bash
   git add <file-or-folder-name> //om alleen een bepaald mapje of bestand te adden
   git add .                     //om ALLES te adden(kan rommel meebrengen)
   ```

5. maak een nieuwe branch aan voor de commit ( verzin zelf een mooie branch naam)

   ```bash
   git cn name-of-branch
   ```
   ```bash
   //zonder alias:
   git checkout -b name-of-branch
   ```

   ​

6. maak de commit ( verzin zelf een mooie commit message)

   ```bash
   git c "dit verandert er met deze commit"
   ```
   ```bash
   //zonder alias:
   git commit -m "dit verandert er met deze commit"
   ```

7. push je commit naar de repository

   ```bash
   git push
   ```

8. maak een pull request aan(zodat iedereen ervan kan genieten)

   ```bash
   // moet via de github website of client
   git pr //als je Cedric heet
   ```

9. ga terug naar stap 1.
