// Agent sample_agent in project aprendizajePropV1

/* Initial beliefs and rules */
control(1).

/* Initial goals */

!start.
!inducirJ48.

/* Plans */

+!start : true 
	<- .print("Incia");
		accionesInternas.extraerArff("bds/iris_3.arff");
		.print("Datos extraidos").

+!inducirJ48: true
	<- 	-+control(1);
		?artefactoListo;
		!enviarEjemplos;
		!construyeModelo.

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
		
+?artefactoListo
	<-	lookupArtifact("J48-iris",_).
	
-?artefactoListo
	<-	.wait(50);
		?artefactoListo.
		