package artefactos;


import java.util.Arrays;

import cartago.*;



public class Votacion extends Artifact {	
	private int cantidadEjemplos, contador = 0;
	private int[][] votos;
	private String[] clasesReales;
	private String[] clasesParaCliente;
	private String[][] clasesInferidas;
	int[] pagos;
	
	void init(int numEjemplos) throws Exception {
		this.cantidadEjemplos = numEjemplos + 1;
		
		// cantidad de ejemplo con su repectiva 
		// votacion y su peso
		votos = new int[cantidadEjemplos][2];
		
		clasesReales     = new String[cantidadEjemplos];
		clasesParaCliente= new String[cantidadEjemplos];
		clasesInferidas  = new String[cantidadEjemplos][4];
		
		pagos = new int[4];
	}
	
	@OPERATION
	void agregarVoto(int ejemplo, int agente, int pago, String claseReal, String clase){
		clasesReales[ejemplo]			 = claseReal;
		clasesInferidas[ejemplo][agente - 1] = clase;
		pagos[agente - 1] = pago;
		contador++;
	}
	
	@OPERATION
	void votos(int ejemplo, OpFeedbackParam<String> v){
		v.set(Arrays.toString(votos[ejemplo]));
	}
	
	@OPERATION
	void arregloVotos(OpFeedbackParam<String> v){
		v.set(Arrays.toString(votos));
	}
	
	@OPERATION
	void precision(OpFeedbackParam<Number> v){
		float s = 0;
		String c = "";
		
		int sumaPagos = 0, tmp;
		for (int i = 0; i < pagos.length; i++) {
			sumaPagos += pagos[i];
		}
		
		sumaPagos = sumaPagos / 2 + 1;
		
		for (int i = 0; i < clasesReales.length; i++) {
			tmp = 0;
			for (int l = 0; l < pagos.length; l++) {
				if(clasesReales[i].equals(clasesInferidas[i][l])){
					tmp += pagos[l];
				}
				
				c = clasesInferidas[i][l];
			}		
			
			if (tmp >= sumaPagos) {
				s += 1;
				clasesParaCliente[i] = clasesReales[i];
			}else{
				clasesParaCliente[i] = c;
			}
		}
		v.set(s / cantidadEjemplos);
		System.out.println(s / cantidadEjemplos);
	}
	
	@OPERATION
	void faltanVotos(OpFeedbackParam<Number> v){
		if (contador >= cantidadEjemplos * 4){
			v.set(1);
		}else{
			v.set(0);
		}

	}
	
	@OPERATION
	void ponPago(int i, int pago){
		pagos[i] = pago;
	}
	
	@OPERATION
	void regresaClasesInferidas(OpFeedbackParam<String> v){
		v.set(Arrays.toString(clasesParaCliente));
	}
	
	@OPERATION
	void regresaClasesReales(OpFeedbackParam<String> v){
		v.set(Arrays.toString(clasesReales));
	}

}

