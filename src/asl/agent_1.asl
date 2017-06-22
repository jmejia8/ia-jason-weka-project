// Agent sample_agent in project aprendizajePropV1

/* Initial beliefs and rules */
control(1).

/* Initial goals */

!start.
!inducirJ48.

/* Plans */

+!start : true 
	<- .print("Inicia");
		accionesInternas.extraerArff("bds/iris_1.arff");
		.print("Datos extraidos").

+!inducirJ48: true
	<- 	-+control(1);
		!creaJ48(J48);
		!enviarEjemplos;
		!construyeModelo.

+!creaJ48(X): true
	<- makeArtifact("J48-iris","artefactos.ArtefactJ48",["bds/iris-Header.arff"],X);
		.print("Artefacto J48 Creado").

+!enviarEjemplos: ej(_,_)
	<-	?control(C);
		?ej(C,V);
		addInstance(s(V));
		-ej(C,V);
		-+control(C+1);
		.print("se aadio nueva instancia");
		!enviarEjemplos.

+!enviarEjemplos: not ej(_,_)
	<-	.print("Fin de ejemplos").
		
+!construyeModelo: true
	<- 	.print("construyendo modelo...");
		induce(Model);
		.print(Model).
		
		