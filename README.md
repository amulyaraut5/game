# Informationen
Hallo Astreine Akazien,

dies hier ist euer Git-Repository, welches ihr im Rahmen des Softwareentwicklungspraktikums nutzen könnt und sollt. Im derzeitigen Zustand befindet sich in diesem dieses Readme, welches ihr grade lest, und ein [.gitignore](https://git-scm.com/docs/gitignore)-File.

## Handhabung

Eine sehr einfach und verständliche Anleitung zum Thema "git" findet ihr unter folgender Adresse:  https://rogerdudler.github.io/git-guide/index.de.html (Bitte schaut euch diese mindestens einmal an!)

### Download
* Git für eure Kommandozeile könnt ihr euch hier herunterladen: https://git-scm.com/downloads
* Als Plugin für Eclipse: https://www.eclipse.org/egit/
* Einbindung in IntelliJ: https://www.jetbrains.com/help/idea/using-git-integration.html


### Einstellungen

    git config --global user.name "Vorname Nachname"
    git config --global user.email "name@cip.ifi.lmu.de"

### Klonen des Repositorys
Die Adresse eures Repositorys findet ihr oben rechts auf dieser Seite, wenn ihr auf den blauen `Clone` Knopf klickt. 
Auf euren Computer bekommt ihr dieses dann, indem ihr im gewünschten Verzeichnis folgenden Befehl aufruft:

    
    git clone git@gitlab2.cip.ifi.lmu.de:dbs_sep/dbs_sep2020-21/vp-astreine-akazien.git

Dies setzt einen von euch erstellten SSH Schlüssel voraus. Informiert euch bitte, wie ihr dies mit eurem Betriebssystem am Besten macht. Alternativ auch möglich mit https zu arbeiten. 

## Keine Angst!
Git und GitLab bieten eine Menge an Funktionen. Wir möchten euch ermutigen diese zu erkunden und zu experimentieren. Solltet ihr Hemmungen haben an diesem Repository zu werkeln, könnt ihr euch auch gerne beliebig viele eigene Repositorys hier im GitLab erstellen und dort euer Wissen erweitern.

Solltet ihr im Laufe des Praktikums dazu entscheiden mit Branches zu arbeiten, würden wir euch bitten diese nicht mutwillig zu löschen. Um die schlimmsten Unfälle zu verhindern, ist in diesem Repository das Löschen des `master` Branches sowie `git push --force` auf diesem Branch nicht möglich. Falls ihr zusätzliche Branches geschützt haben möchtet, wendet euch bitte an euren Tutor.


## Anleitung:

Spiel starten:
1. Terminal öffnen.
2. Befehl java -jar <Dateipfad Jar Datei> eingeben
(Die Schritte bitte einmal für die Jar Datei des Servers ausführen. Wiederholt für die jar Datei des Clients, je nach 
gewünschter Anzahl an Mitspielern.)

Nachdem Sie den Server und anschließend den Client geöffnet haben, folgen Sie bitte den Spielanweisungen. 
Hier eine kurze Anleitung:

- Username eingeben
- Datum Ihres letzten Dates eingeben
- mit @<name> kann eine Direktnachricht gesendet werden, Eingaben ohne Vorzeichen werden an alle User im Chat gesendet
 
- mit #play ein Spiel erstellen bzw. beitreten
- mit #choose <name> bzw. #choose <card> kann ein Spieler/eine Karte ausgewählt werden
- durch #help können mögliche Kommandos angezeigt werden
- mit "bye" wird der Raum verlassen 

Viel Spaß! :)

Anmerkungen:

-Es wurde auch an einer View mittels JavaFx gearbeitet, der aktuelle Stand befindet sich im View-branch. Da es noch Bugs
 im Hauptprogramm gab, auf die wir uns fokussieren wollten, konnten wir diese Version des Spiels allerdings nicht 
 vollständig fertigstellen.

-Um das Design etwas vielseitiger zu gestalten, werden Serverdurchsagen in verschiedenen Farben ausgegeben. 
Im Windows Terminal sind wir auf ein Problem gestoßen, bei dem der Text nicht wie gewünscht in den Farben ausgegeben 
wird. Stattdessen sind eigenartige Zeichenfolgen vor und nach den Nachrichten vorzufinden. Das Feature wurde bewusst 
nicht entfernt, da es auf MacOS und in IntelliJ einwandfrei funktioniert. 