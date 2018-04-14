package ij.plugin.psfx;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

public class parseBinImp2Res_ implements Measurements{
	private ImagePlus imp;
	private Analyzer anal;
	private ResultsTable rt;

	private ResultParser_ rp = new ResultParser_();

	parseBinImp2Res_(ImagePlus imp){
		this.imp = imp;
	}

	public void getRes(int measurement){
		this.anal = new Analyzer(this.imp, measurement, this.rt);
		this.anal.measure();
		this.rt.show("measure result");

		this.rp.setTable(this.rt);
		this.rp.logresult();

	}

	public int[] getCent() {
		this.rt = new ResultsTable();
		this.anal = new Analyzer(this.imp, Measurements.CENTROID, this.rt);
		this.anal.measure();
		//this.rt =this.anal.getResultsTable();
		this.rp.setTable(this.rt);
		return(this.rp.row2valsArray(0));
		//this.rp.logresult();
	}



}
