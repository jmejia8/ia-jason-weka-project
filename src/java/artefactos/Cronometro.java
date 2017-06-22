package artefactos;

import cartago.*;

public class Cronometro extends Artifact {
	private long startTime;
	private long endTime;
		
@OPERATION
    void startTime(){
		 startTime= System.nanoTime();		 
	 }
	 
 @OPERATION
    void endTime(OpFeedbackParam<java.lang.Long> totalTime){
		endTime = System.nanoTime();
		totalTime.set((endTime - startTime)/1000000); // milliseconds
	 }

}