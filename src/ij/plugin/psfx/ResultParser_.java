package ij.plugin.psfx;

import java.util.ArrayList;

import ij.measure.ResultsTable;


public class ResultParser_ {
	private ResultsTable rt;
	private String[] colnames;
	private int ncol = 1;
	private int nrow = 1;
	private int[][] resultarray;
	//private ArrayList<ArrayList<>> reslutlist= new ArrayList<Arraylist<>>();
	private ArrayList<ArrayList> reslutlist= new ArrayList<ArrayList>();
	Mscs_ ms = new Mscs_();

	public void setTable(ResultsTable rt) {
		this.rt = rt;
		this.colnames = rt.getHeadings();
		this.ms.strs2log(this.colnames);

//		this.ncol = this.colnames.length+1;
		this.ncol = this.colnames.length;
		ms.int2ijlog(this.ncol);

		this.nrow = rt.getCounter();
		this.resultarray = new int[this.nrow][this.ncol];
		ms.int2ijlog(this.nrow);

		ms.ints2ijlog(row2valsArray(0));

		for(int i=0; i < this.nrow ; i++) {
			this.resultarray[i] = row2valsArray(i);
		}

		//IJ.log("kokomade kita ");

		//ms.arar2log(reslutlist);
	}

	public int[] row2valsArray(int rowindex) {
		String rowstr = rt.getRowAsString(rowindex);
//		IJ.log(rowstr);
		rowstr = rowstr.replaceAll("\n", "");
//		IJ.log(rowstr);
		String[] strs = rowstr.split("\t");

		int[] vals = new int[strs.length -1];
//		IJ.log(strs[0]);
//		IJ.log(strs[1]);
//		IJ.log(strs[2]);

		for(int i=0; i < (strs.length -1); i++) {
//			double db_i = Double.parseDouble(strs[i]);
			vals[i] = (int)Double.parseDouble(strs[i+1]);
			ms.int2ijlog(vals[i]);
		}

		ms.ints2ijlog(vals);
		return vals;
	}

	public ArrayList<ArrayList> getList(){
		return this.reslutlist;
	}

	public int[][] getArray(){
		return this.resultarray;
	}

	public void logresult() {
		this.ms.arar2log(resultarray);
	}

}
