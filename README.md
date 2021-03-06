# HLR-UP
Plugins permettant de créer un hoppers pouvant d'aspirer les cactus dans un 1 chunk entier.
 - Celui-ci divise le monde en région de 32x32 chunks. Afin de rechercher une optimisation.
 - Avec un système de sauvegarde dans une base de donnée relationnelle (Sqlite)
 - La sauvegarde ce fait à chaque palier de 1000 cactus contenus dans un hopper, afin d'éviter le surplus de requête.
 - Communication du nombre de cactus à l'intérieur du hopper via l'action bar.
 - Peut supporter le multi-monde. 

*Quelques calculs: 

Si nous prenons une zone de 16 384 par 16 384 dans un seul monde, ce qui représente une zone de 268 435 456 blocs de superficie cela représente donc 1 048 576 de chunks.

Le système utilisé le plus souvent serait 1 hopper par chunk et donc il faudrait 1 048 576 checks dans la liste des hoppers.
NB: Pour savoir lequel serait le bon. (dans la pire situation et seulement pour un monde)

Le système mis en place ici coupe les chunks en région de 32x32 chunks.
Donc le système va d'abords chercher votre monde puis région et enfin le chunks ou vous vous situez. 

Ce qui donnerait 1 048 576 / (32x32) = 1024. Donc le plugin checkerai 1024 régions au maximun puis comme une région représente 1024 chunks alors dans le pire des cas il y aurait:
1024 + 1024 = 2048 possiblités dans la pire situation.

Ces calculs sont fournis selon la situation de départ au-dessus et nous explique l'optimisation qui a été effectué.

De plus d'après des calculs et des analyses via spark:

35 hoppers représente 1.59 KB en variables dans la mémoire RAM car 1024x1,59=1628 bytes et donc le nombre de byte que prendrait un hopper en variable dans la ram serait:
1628/35=46 bytes. Le hopper prendrait alors seulement 46 bytes environ. 

Ce test a été réalisé avec 81 hoppers ce qui représente 3.75 KB donc 1024x3.75=3840 bytes et enfin 3840/81=47 bytes environ.

Le hopper de farm représente environ 50 bytes pour être large au niveau des valeurs car celle-ci on été arrondi a l'unité supérieur.

*Commandes:

- /hopper -> permet de give un hopper.
- /hopper list -> permet de lister tous les hoppers. 

*Permissions:

- hlr.command.use

![image](https://user-images.githubusercontent.com/75928424/111283245-34b0dd80-863f-11eb-94da-0ea5032f8348.png)
![image](https://user-images.githubusercontent.com/75928424/111360506-1eca0980-868d-11eb-8917-9037b14fda76.png)
