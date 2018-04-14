package ij.plugin.psfx;

import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.plugin.PlugIn;

public class Mscs_ implements PlugIn{

	public void ints2ijlog(int[] ints) {
		String str = "";
		for(int i =0; i < ints.length ; i++) {
			str = str + String.valueOf(ints[i]) +", ";
		}
		str = str.substring(0, str.length() -2);
		str = str+"\n";
		IJ.log(str);
	}

	public void int2ijlog(int n) {
		IJ.log(String.valueOf(n));
	}

	public void db2ijlog(double[] dbs) {
		String str = "";
		for(double db: dbs) {
			str = str+String.valueOf(db)+" ";
		}
		str = str.substring(0, str.length() -3);
		str = str+"\n";
		IJ.log(str);
	}

	public void arar2log(ArrayList<ArrayList> arar) {
		for(int i =0; i < arar.size() ; i++) {
			ArrayList<Integer> ar_i;
			ar_i = arar.get(i);
			arlist2log(ar_i);
		}
	}

	public void arar2log(List<List<int[]>> arar) {
		for(int i =0; i < arar.size() ; i++) {
			List<int[]> ar_i;
			ar_i = arar.get(i);
			for(int j=0; j < ar_i.size(); j++) {
				int[] ij = ar_i.get(j);
				ints2ijlog(ij);
			}
		}
	}


	public void arar2log(int[][] arar) {
		for(int i = 0; i<arar.length; i++) {
			ints2ijlog(arar[i]);
		}
	}

	public void arlist2log(ArrayList<Integer> arint) {
		String log = "";
		for(int i= 0; i < arint.size();i++) {
			log = log+ String.valueOf(arint.get(i)) + " ";
		}
		IJ.log(log);
	}

	public void strs2log(String[] strs) {
		String str = "";
		for(int i =0; i< strs.length; i++) {
			str = str + strs[i] +" ";
		}
		str = str.substring(0, str.length() -1);
		str = str+"\n";
		IJ.log(str);
	}

	public String ints2String(int[] ints) {
		String str = "";
		String sep = ", ";
		for(int i =0; i<ints.length;i++) {
			str = str+String.valueOf(ints[i])+sep;
		}
		str = str.substring(0, (str.length() -2));
		return str;
	}

	public boolean isElement(String[] set, String e) {
		boolean[] isE = new boolean[set.length];
		boolean result = false;

		for(int i=0; i < set.length;i++) {
			if(e.equals(set[i])) {
				result = true;
				}
			}
		return result;

	}


	public void run (String arg) {
		IJ.log("unko_intshow");
		int[] ints = {2,3,4, 4,2,6};
		ints2ijlog(ints);
		IJ.log(ints2String(ints));
	}
}
