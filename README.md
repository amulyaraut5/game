# 💌 Love Letter (Vorprojekt Astreine Akazien)



## Spiel starten
1. Terminal öffnen.
2. Befehl `java -jar %PATH_TO_JAR%` eingeben.

(Die Schritte bitte einmal für die `.jar`-Datei des Servers ausführen. Wiederholt für die `.jar`-Datei des Clients, je nach 
gewünschter Anzahl an Mitspielern.)

Nachdem Sie den Server und anschließend den Client geöffnet haben, folgen Sie bitte den Spielanweisungen.
Sie müssen diesen strikt folgen und können nicht cheaten. 


## Hier eine kurze Anleitung

- Username eingeben
- Datum Ihres letzten Dates eingeben
- mit `@<name>` kann eine Direktnachricht gesendet werden, Eingaben ohne Vorzeichen werden an alle User im Chat gesendet
 
- mit #play ein Spiel erstellen bzw. beitreten
- mit `#choose <name>` bzw. `#choose <card>` kann ein Spieler/eine Karte ausgewählt werden
- durch #help können mögliche Kommandos angezeigt werden
- mit "bye" wird der Raum verlassen 

Viel Spaß! :)

## Anmerkungen

- Es wurde auch an einer View mittels JavaFx gearbeitet, der aktuelle Stand befindet sich im View-branch. Da es noch Bugs
 im Hauptprogramm gab, auf die wir uns fokussieren wollten, konnten wir diese Version des Spiels allerdings nicht 
 vollständig fertigstellen. Zur kurzen Übersicht unseres bisherigen Standes, liegen in diesem Projekt auf dem Master 
 branch 4 Beispiel Bilder bereit.

 Zum Starten des Spiels mit View müssen in IntelliJ die [VM options](https://www.jetbrains.com/help/idea/javafx.html#vm-options) unter **Run | Edit Configurations | Application** gesetzt sein:

 ```--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml```
 
 (Bitte statt **%PATH_TO_FX%** den Pfad zum JavaFX SDK lib Ordner angeben)

- Um das Design etwas vielseitiger zu gestalten, werden Serverdurchsagen in verschiedenen Farben ausgegeben. 
Im Windows Terminal sind wir auf ein Problem gestoßen, bei dem der Text nicht wie gewünscht in den Farben ausgegeben 
wird. Stattdessen sind eigenartige Zeichenfolgen vor und nach den Nachrichten vorzufinden. Das Feature wurde bewusst 
nicht entfernt, da es auf MacOS und in IntelliJ einwandfrei funktioniert. 

## Befehle

| Befehl | Beschreibung |
| ------ | ------ |
| `<message>` | Nachricht wird an alle User geschickt |
| `@<name> <message>` | Schickt einem User eine Privatnachricht |
| `bye` | Du verlässt den Chatraum |
| `#help` | listet alle Befehle im Spiel auf |
| `#play` | du trittst einem neuen Spiel bei |
| `#start` | Spiel wird gestartet, wenn 2-4 Spieler begetreten sind |
| `#score` | Zeigt den aktuellen Punktestand an |
| `#choose <card>` | wähle eine gegebene Karte |
| `#choose <player>` | wähle einen den gegebenen Spieler |

![Show Login View Picture](https://gitlab2.cip.ifi.lmu.de/dbs_sep/dbs_sep2020-21/vp-astreine-akazien/-/blob/master/GUI%20Pictures/viewPicture1.png "Login View Picture")