// Agent cliente in project ia2project

/* Initial beliefs and rules */

contador(0).
totalEjemplos(0).

/* Initial goals */

!start.

/* Plans */

+!start : true 
	<-  .print("Iniciando...");
		// Artefacto que particiona la base de datos
		// la divide en conjunto de entrenamiento y
		// n de prueba, con n el número de agentes.		
		makeArtifact("particionador","artefactos.Particiona",["bds/credit-g.arff", 4], X);
		
		// Total de ejemplo de prueba
		numEjemplos(T);
		-+totalEjemplos(T);
	    .print("jefe, clasificame los datos.").
	    
+madaEjemplos[source(A)]
	<-  ?totalEjemplos(N);
		
		// Manda ejemplos al jefe
		startTime;
		for(.range(X, 0, N)){
			?contador(C);
		  //  ejemploPrueba(C, X);
		    .send(jefe, tell, recibeEjemplo(X));
		    -+contador(C + 1);
	  	}
	  	endTime(Tiempo);
	  	
	  	
	  	println("Tiempo enviando ejemplos: ", Tiempo, "ms.");
	  	.wait(3000);
	  	//precision(V);
	.
	
+lasClases(C, R)[source(A)]
	<-  println(C);
		println(R)
	.
	
	