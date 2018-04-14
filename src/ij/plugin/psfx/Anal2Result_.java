package ij.plugin.psfx;
//class due to parse result of Analyzer class

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

public class Anal2Result_ implements Measurements{
	private ImagePlus imp;
	private Mscs_ ms = new Mscs_();

	Anal2Result_(ImagePlus imp){
		this.imp = imp;
	}

	public double[] getDblValsArray(int Measurements) {
		ResultsTable rt = new ResultsTable();
		Analyzer anal = new Analyzer(this.imp,Measurements, rt);
		anal.measure();

		String rowstr = rt.getRowAsString(0);
		rowstr = rowstr.replaceAll("\n", "");
		String[] strs = rowstr.split("\t");
		double[] result = new double[(strs.length -1)];

		for(int i=0; i < (strs.length -1); i++) {
			result[i] = Double.parseDouble(strs[i+1]);
		}
		//ms.db2ijlog(result);;

		return result;
	}

	public int[] getIntValsArray(int Measurements) {
		ResultsTable rt = new ResultsTable();
		Analyzer anal = new Analyzer(this.imp,Measurements, rt);
		anal.measure();

		String rowstr = rt.getRowAsString(0);
		rowstr = rowstr.replaceAll("\n", "");
		String[] strs = rowstr.split("\t");
		int[] result = new int[(strs.length -1)];

		for(int i=0; i < (strs.length -1); i++) {
			result[i] = (int)Double.parseDouble(strs[i+1]);
		}
		//ms.ints2ijlog(result);

		return result;
	}


	public String[] getColname(int Measurements) {
		ResultsTable rt = new ResultsTable();
		Analyzer anal = new Analyzer(this.imp,Measurements, rt);
		anal.measure();
		String[] colnames;
		colnames = rt.getHeadings();
		return colnames;
	}

}
