package artefactos;


import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import cartago.*;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.io.File;
import java.io.IOException;
import java.lang.String;


public class Particiona extends Artifact {	
	private DataSource source;
	private Instances  instances;
	private Instances  conjuntoPrueba;
	private Attribute[] attributes;
	private int particiones;
	private int numAttributes;
	//private int actual;

	
	void init(String archivo, int parts) throws Exception {
		// cantidad de agentes mas la de prueba
		this.particiones = parts + 1;
		
        // Obtiene todos base de datos
   		source    = new DataSource(archivo);
   		instances = source.getDataSet();
   		
   		// Numero de atributos
   		this.numAttributes = instances.numAttributes();
   		instances.setClassIndex(numAttributes-1);
   		
   		// atrubutos
   		attributes = new Attribute[numAttributes - 1];
   		for(int i = 0; i < numAttributes - 1; i++){
   			attributes[i] = instances.attribute(i);
   		}
   		
   		// Aleatoriza datos
   		// Random seed = new Random();
   		// instances.randomize(seed);
   		
   		// Estratifica base de datos
   		//instances.stratify(particiones);
   			
   		// Primer fold para probar
   		// conjuntoPrueba = instances.testCV(particiones, 0);
   		
   		cargaConjuntoPrueba();
	}
	
	void cargaConjuntoPrueba() throws Exception{
		DataSource a = new DataSource("./out/test.arff");
		conjuntoPrueba = a.getDataSet();
	}
	
	void saveArff(Instances dataSet, String arffName) throws IOException{
		ArffSaver saver = new ArffSaver();
   		saver.setInstances(dataSet);
   		//saver.setFile(new File("./data/test.arff"));
	   	saver.setDestination(new File("./out/" + arffName + ".arff"));
	   	saver.writeBatch();
	}
	
	@OPERATION
	void guardaParticiones() throws IOException{
   		saveArff(conjuntoPrueba, "test");

   		
   		for (int i = 1; i < particiones; i++) {
   			Instances tmp = instances.testCV(particiones, i);
   			saveArff(tmp, "train_" + i);
		}
	}
	
	@OPERATION
	void numEjemplos(OpFeedbackParam<Number> t){
		t.set(instances.numInstances() / particiones - 1);
	}
	
	@OPERATION
	void ejemploPrueba (int ejemplo, OpFeedbackParam<Term> x){
        Instance insAux = conjuntoPrueba.instance(ejemplo);
        String txt = insAux.toString();
        
        txt = "ejemplo( " + ejemplo + ",\"" + txt + "\")";
        //System.out.println(txt);

        x.set(Literal.parseLiteral(txt));
        //System.out.println(txt);

	}
	
	@LINK
	void ejemploPruebaLink(OpFeedbackParam<Instances> data){
		data.set( conjuntoPrueba);
		//actual++;
	}

}
