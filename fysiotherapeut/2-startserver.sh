#! /bin/sh
#
# shell-script voor het starten van de IVH5 LibraryServer.
# Je start de server het handigst vanuit de directory waar de webserver
# de classes kan vinden. Pas deze zo nodig hieronder aan.
#
cd ./target

# Start java met het juiste classpath
java -cp ./library-server.jar:./library-shared.jar:./library-api.jar:./dependencies/log4j-1.2.17.jar:./dependencies/mysql-connector-java-5.1.33.jar edu.avans.ivh5.server.model.main.LibraryServer -properties resources/breda.properties &

# Start tweede server voor inter-server communicatie.
java -cp ./library-server.jar:./library-shared.jar:./library-api.jar:./dependencies/log4j-1.2.17.jar:./dependencies/mysql-connector-java-5.1.33.jar edu.avans.ivh5.server.model.main.LibraryServer -properties resources/oosterhout.properties &
 
# Wanneer je securityproblemen wilt oplossen, voeg dan onderstaande optie aan het command toe.
# Hiermee krijg je inzicht in alle security instellingen.
#
# 		-Djava.security.debug=access,failure

read -p "press enter"
