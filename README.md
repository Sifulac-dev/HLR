# HLR
Plugins permettant de créer un hoppers pouvant d'aspirer les cactus dans un 1 chunk entier.
 - Celui-ci divise le monde en région de 32x32 chunks. Afin de rechercher une optimisation.
 - Avec un système de sauvegarde dans une base de donnée relationnelle (Sqlite)
 - La sauvegarde ce fait à chaque palier de 1000 cactus contenus dans un hopper, afin d'éviter le surplus de requête.
 - Communication du nombre de cactus à l'intérieur du hopper via l'action bar.
 - Peut supporter le multi-monde. 

Quelques calculs: 

Si nous prenons une zone de 16 384 par 16 384 dans un seul monde, ce qui représente une zone de 268 435 456 blocs de superficie cela représente donc 1 048 576 de chunks.

Le mauvais système serait que dans le pire des cas ou il y aurait un hopper par chunks. Cela voudrez dire qu'il faudrait 1 048 576 checks dans la liste des hoppers
pour savoir lequel serait le bon. (dans la pire situation et seulement pour un monde)

Le système mis en place ici coupe les chunks en région de 32x32 chunks.
Donc le système va d'abords chercher votre monde puis région et enfin le chunks ou vous vous situez. 

Ce qui donnerait 1 048 576 / (32x32) = 1024. Donc le plugin checkerai 1024 régions au maximun puis comme une région représente 1024 chunks alors dans le pire des cas il y aurait:
1024 + 1024 = 2048 possiblités dans la pire situation.

Ces calculs sont fournis selon la situation de départ au-dessus et nous explique l'optimisation qui a été effectué.
