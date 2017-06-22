package artefactos;


import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import cartago.*;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.String;

@ARTIFACT_INFO(
outports = { 
				@OUTPORT(name="portParticiona")
		   }
)

public class creaJ48 extends Artifact {	
	private DataSource  source;
	private Instances   instances;
	private J48         Arbol;
	private int         numAttributes;
	private Attribute[] attributes;
	private BufferedWriter writer;
	private OpFeedbackParam<Instances> data;
	Instances trainData;

	
	void init(int particion) throws Exception {
		
		String archivo = "./out/train_" + particion + ".arff";
		
        // Obtiene todos base de datos
   		source    = new DataSource(archivo);
   		instances = source.getDataSet();
   		
   		// Numero de atributos
   		numAttributes = instances.numAttributes();
   		instances.setClassIndex(numAttributes-1);
   		
   		// atrubutos
   		attributes = new Attribute[numAttributes - 1];
   		for(int i = 0; i < numAttributes - 1; i++){
   			attributes[i] = instances.attribute(i);
   		}
   		
   		// Crea modelo
   		Arbol = new J48();
   		Arbol.setSeed(666);
   		Arbol.buildClassifier(instances);
   		
   		// guarda el árbol generado por el conjuto de 
   		// entrenamiento
   		writer = new BufferedWriter( new FileWriter( "./out/model_" + particion + ".txt"));
   	    writer.write( Arbol.toString());
   	    writer.close( );
   		
   		signal("modeloJ48_" + particion);
   		
   		//System.out.println("Modelo en java");

	}
	
	
	@OPERATION
	void clasificaEjemplo(String instanceStr, OpFeedbackParam<Term> claseReal, OpFeedbackParam<Term> clase){
		Instance i = new Instance(numAttributes);
		String[] vals = instanceStr.split(",");
		String val;

		int n = vals.length - 1;

		for(int j = 0; j < n ; j++){
			val = vals[j];
			//System.out.println("\n\n\n\n==========================="+ attributes[j].name() + "\t" +  val);			
			
			if(attributes[j].isNumeric()){
				double valDouble = Double.parseDouble(val);
				i.setValue(attributes[j], valDouble);
			
			} else {
				i.setValue(attributes[j], val.replace("'", ""));
			};
			
			j++;
		}

		try {
			i.setDataset(instances);
			i.setClassValue(Arbol.classifyInstance(i));
			Atom classTerm = new Atom(i.stringValue(numAttributes-1));
			clase.set(classTerm);
			claseReal.set(new Atom(vals[n]));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OPERATION
	void cargaConjuntoPrueba() throws OperationException{
		data = new OpFeedbackParam<Instances>();
		execLinkedOp("portParticiona", "ejemploPruebaLink", data);
		trainData = data.get();
	}
	
	@OPERATION
	void clasificaChido(int i, OpFeedbackParam<Term> claseReal, OpFeedbackParam<Term> clase) throws Exception{
		Instance ejemplo = trainData.instance(i);
		ejemplo.setDataset(instances);
		ejemplo.setClassValue(Arbol.classifyInstance(ejemplo));
		Atom classTerm = new Atom(ejemplo.stringValue(numAttributes-1));
		clase.set(classTerm);
		claseReal.set(new Atom(ejemplo.stringValue(numAttributes - 1) ));
	}

}


