// Agent jefe in project ia2project

/* Initial beliefs and rules */

empleadosListos([]).
totalEjemplos(0).
totalClasificados(0).

/* Initial goals */

!start.

/* Plans */

+!start : true 
	<- .print("Iniciando...");
		// verifica que el cliente ya tenga sus datos
		?artefactoListo;

		numEjemplos(N);
		-+totalEjemplos(N);
		
		// Crea artefactos para votaciones y contar tiempo
		makeArtifact("comicios", "artefactos.Votacion",[N], X);
		
		// Crea artefacto para contar el tiempo
		// en ejecuciones
		makeArtifact("timer", "artefactos.Cronometro",[], _);
		print("Claro, en seguida mando a mis empleados");
		
		// Señal para generar su modelo J48 
		// de Weka
		.send(empleado1, tell, creaJ48(1));
		.send(empleado2, tell, creaJ48(2));
		.send(empleado3, tell, creaJ48(3));
		.send(empleado4, tell, creaJ48(4));
		?finVotacion
		.

+listoEmpleado(I)[source(A)]
	<-  ?empleadosListos(L);
		-+empleadosListos([I | L]);
		?esperando;
	.

/*
* Recibe datos de cliente y se los manda
* a sus empleados para clasificarlos 
*/
+recibeEjemplo(X)[source(A)]
	<-  .send(empleado1, tell, clasificaParaJefe(X));
		.send(empleado2, tell, clasificaParaJefe(X));
		.send(empleado3, tell, clasificaParaJefe(X));
		.send(empleado4, tell, clasificaParaJefe(X));
	.

+?artefactoListo
	<-	lookupArtifact("particionador",_).
	
-?artefactoListo
	<-	.wait(100);
		?artefactoListo.


+?esperando
	<- ?empleadosListos(L);
		.length(L) == 4;
		.send(cliente, tell, madaEjemplos)
	.

-?esperando
	<- .wait(50);
		?esperando
	.
	
+?finVotacion
	<-	faltanVotos(V);
		V == 1;
		println(">>> Termino la votacion ");
		precision(A);
		println("Precisión: ", A);
		regresaClasesInferidas(C);
		regresaClasesReales(R);
		.send(cliente, tell, lasClases(C, R))
	.
	
-?finVotacion
	<-	.wait(50);
		?finVotacion
	.

	

	