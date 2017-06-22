package utils;

import java.io.BufferedReader;
import java.io.FileReader;


import weka.core.Instance;
import weka.core.Instances;

public class ManejadorInstancias
{
	public Instances leerArff(String archivo) throws Exception
	{
		BufferedReader reader = new BufferedReader(
	            new FileReader(archivo));
	    return new Instances(reader);
	}
	public Instance parsearCreencia(String instanciaAgente)
	{
	    /*
	     * instanciaAgente debe ser algo como [val1,val2,valn]
	     * Nuevo formato s([val1,val2,valn])
	     */
	    //quitar corchetes (Cambio Ajuste a nuevo formato)
	    String parcial = instanciaAgente.substring(3, instanciaAgente.length()-2);
	    //obtener valores separados
	    String[] valores = parcial.split(",");
	    //convertir a double
	    double[] vals = new double[valores.length];
	    for(int i = 0; i < valores.length; i++)
	    {
	        vals[i] = Double.parseDouble(valores[i]);
	    }
	    //creary regresar instancia
		return new Instance(1.0, vals); //el weight siempre es 1
	}
}
