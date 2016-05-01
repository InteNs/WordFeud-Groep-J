# WordFeud
Informatie over alles wat met de applicatie te maken heeft.

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
Voor problemen en vragen naar Cedric of Mark.

1. ga naar master en haal de nieuwste versie op

   ```bash
   git co master
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
   git add <file-or-folder-name>
   ```

5. maak een nieuwe branch aan voor de commit

   ```bash
   git cn name-of-branch
   ```

6. maak de commit

   ```bash
   git c "dit verandert er met deze commit"
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