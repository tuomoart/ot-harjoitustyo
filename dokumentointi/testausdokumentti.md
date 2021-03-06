# Testausdokumentti

Ohjelmalogiikkaa on testattu Junitilla yksikkö- ja integraatiotasolla ja koko ohjelman toimintaa on testattu manuaalisesti.


## Yksikkö- ja integraatiotestaus

Sovelluslogiikan testaamisesta huolehtivat testiluokat domain.FractalTest sekä domain.ComplexNumberTest. Nämä luokat testaavat pakkauksen domain luokkia sekä yksikkö-, että integraatiotestein. Monet sovelluslogiikan osat on testattu jo niiden kirjoitusvaiheessa yksikkötestein, eikä näiden testien poistamiselle jälkikäteen nähty tarvetta. Valtaosa sovelluslogiikan toiminnoista on testattu myös integraatiotestein, minkä takia useita asioita testataan moneen kertaan. Tämän ei nähty kuitenkaan olevan huono asia. Laajan yksikkötestauksen takia jokaista toimintoa ei ole testattu erillisillä integraatiotason testeillä, sillä laajan integraatiotason testin epäonnistuessa yksikkötestit kertovat, mistä epäonnistuminen johtuu. DAO luokan dao.SQLiteHistoryDao testaamisesta huolehtii testiluokka dao.SQLiteDaoTest. Myös siinä merkittävimmät toiminnallisuudet on testattu sekä yksikkö- että integraatiotestein.


### Testauskattavuus

Käyttöliittymää lukuunottamatta sovelluksen testauksen rivikattavuus on 100% ja haarautumakattavuus on 100%.

![](https://raw.githubusercontent.com/tuomoart/ot-harjoitustyo/master/dokumentointi/kuvat/testikattavuusraportti.png)

Käytännössä jokaista tilannetta ei ole testattu, mutta sovelluksen luonne ei vaadi jokaisen rajatapauksen erillistä testaamista. Nykyinen käyttöliittymä ei salli virheellisten syötteiden antamista, sillä syötteet annetaan liukusäätimillä tai muilla hallituilla keinoilla. Jos parametreja voisi muuttaa vapaammin, olisi kattavampi rajatapausten testaaminen aiheellista. Sovellukseen on myös kirjoitettu kohtalaisen paljon virhekäsittelyä, joka ei liity mihinkään nimenomaiseen virhetilanteeseen. Nämä käsittelyt on kirjoitettu sellaisia virhetilanteita varten, joita ei ole osattu ennustaa, mutta tämän virhekäsittelyn avulla sovellus toimii hallitulla tavalla myös odottamattomassa virhetilanteessa. Näitä odottamattomien virheiden käsittelytoimintoja ei ole kattavasti testattu.


## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti. Kaikki määrittelydokumentin ja käyttöohjeen listaamat toiminnallisuudet on testattu. Järjestelmätestaus on suoritettu Linux-ympäristössä Helsingin yliopiston Cubbli -alustalla. Testaus on tehty ainoastaan paikallisella laitteella, sillä yliopiston Cubbli-etätyöpöydillä JavaFX- sekä tietokantatoiminnot eivät ole toimineet. Ongelmat ovat koskeneet myös muita kurssin harjoitustöitä, ja sen perusteella on todettu, etteivät ongelmat johdu tästä ohjelmasta.
