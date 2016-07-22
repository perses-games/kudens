Verplicht in elke map:
* layer met name 'tnt'
* layer met name 'background'

Optionele property op background layer niveau:
* scroll, scroll snelheid tov tnt laag
* music, naam van de MusicPlayer enum die gespeelt moet worden (bv. BREAKOUT of BONGOBIRDS)

Properties op tile niveau:
* type
** bounce, buster bounced hiertegen
** tnt, dodelijk
-> Als een tnt blockje een location heeft wordt aan de hand daarvan ook andere blokjes eromheen verwijdert
- TL - Top Left van de tnt
- TR - Top right van de tnt
- BL - Bottom left van de tnt
- BR - Bottow right van de tnt


// todo:
* speed, versnelling, waarde is hoeveel sneller (1 doet niets)
* distance, hoeveel langer deze jump is (hele getallen > 1, 1 doet niets)
