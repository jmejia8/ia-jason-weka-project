// Agent empleado in project ia2project

/* Initial beliefs and rules */

id(0).

// entre más grande sea el pago
// más credibilidad tendrá el agente
pago(1).

/* Initial goals */

!start.

/* Plans */

+!start : true 
	<- .print("Iniciando...").

/*
 * Crea árbol de decisión J48 de weka y lo
 * enlaza con el particionador del cliente
 */
+creaJ48(I)[source(A)]
	<-  .print("Ya vamos, ",A);
		-+id(I);
		
		// Crea artefacto para clasificar instancias
		.concat("arbol", I, Name);
		makeArtifact(Name, "artefactos.creaJ48", [I], J48id);
		focus(J48id);
		
		// Enlaza artefactos: particionador y j48
		.concat("enlace-", I, LinkName);
		lookupArtifact("particionador", PartId);
		linkArtifacts(J48id, "portParticiona", PartId);
		
		// Carga en conjunto de prueba
		startTime;
		cargaConjuntoPrueba;
	  	endTime(Tiempo);
	  	println("Tiempo cargando ejemplos de pruba: ", Tiempo, "ms.");
		.send(jefe, tell, listoEmpleado(I))
		.
		
+clasificaParaJefe(X)[source(A)]
	<-  // Casificando
		// startTime;
		clasificaChido(X, ClaseReal, Clase);
		// endTime(Tiempo);
		// println("Tiempo calsificando: ",  Tiempo);
		
		?pago(Pago); ?id(Id);
		
		// Agrega o quita sueldo
		if ( ClaseReal == Clase ){
			-+pago(Pago + 1);
		}else{
			-+pago(Pago - 1);
		}
		
		// Agrega el voto
		agregarVoto(X, Id, Pago, ClaseReal, Clase);
	.
	
	
