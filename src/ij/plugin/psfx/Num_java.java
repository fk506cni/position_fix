package ij.plugin.psfx;

import java.io.File;

import ij.IJ;

//many function to process numerics

public class Num_java {
	public double ints2avg(int[] ints) {
		double sum = 0;
		for(int i=0; i<ints.length;i++) {
			sum = sum+(double)ints[i];
		}
		double avg = sum / ints.length;
		return avg;
	}

	public double longs2avg(long[] longs) {
		double sum = 0;
		for(int i=0; i<longs.length;i++) {
			sum = sum+(double)longs[i];
		}
		double avg = sum / longs.length;
		return avg;

	}

	public double doubles2avg(double[] ds) {
		double sum =0;
		for(int i=0; i< ds.length;i++) {
			sum=sum+ds[i];
		}
		double avg = sum / ds.length;
		return avg;
	}

	public double ints2vari(int[] ints) {
		double avg = ints2avg(ints);
		double sum_sq =0;
		for(int i=0; i<ints.length;i++) {
			sum_sq = sum_sq + Math.pow((double)ints[i], 2);
		}
		double vari= (sum_sq / ints.length) - Math.pow(avg, 2);
		return vari;
	}

	public double longs2vari(long[] longs) {
		double avg = longs2avg(longs);
		double sum_sq =0;
		for(int i=0;i<longs.length;i++) {
			sum_sq = sum_sq + Math.pow((double)longs[i], 2);
		}
		double vari = (sum_sq / longs.length) -Math.pow(avg, 2);
		return vari;
	}

	public double doubles2vari(double[] ds) {
		double avg = doubles2avg(ds);
		double sum_sq = 0;
		for(int i=0; i<ds.length;i++) {
			sum_sq = sum_sq + Math.pow(ds[i], 2);
		}
		double vari = (sum_sq / ds.length) - Math.pow(avg, 2);
		return vari;
	}

	public double ints2sd(int[] ints) {
		double vari = ints2vari(ints);
		double sd = Math.sqrt(vari);
		return sd;
	}

	public double longs2sd(long[] longs) {
		double vari = longs2vari(longs);
		double sd = Math.sqrt(vari);
		return sd;
	}

	public double doubles2sd(double[] ds) {
		double vari = doubles2vari(ds);
		double sd = Math.sqrt(vari);
		return sd;
	}

	public Integer[] ints2Ints(int[] ints) {
		Integer[] Is = new Integer[ints.length];
		for(int i=0; i < ints.length;i++) {
			Is[i] = new Integer(ints[i]);
		}
		return Is;
	}

	public double[] ints2dbs(int[] ints) {
		double[] dbs = new double[ints.length];

		for(int i=0;i<ints.length;i++) {
			dbs[i] = (double)ints[i];
		}
		return dbs;
	}



	public double int2med(int[] ints) {
		double med = 0.0;
		double[] dbs = ints2dbs(ints);

		//pre filtering.
		if(ints.length==1) {
			return ints[0];
		}else if(ints.length==0) {
			IJ.log("ints array is null!");
			return 0.0;
		}

		if(ints.length % 2==1) {
			med = dbs[(dbs.length+1)/2];
		}else if(ints.length % 2==0) {
			med = (dbs[dbs.length/2] + dbs[(dbs.length/2) +1])/2;
		}
		return med;
		}

	public double dbs2med(double[] dbs) {
		double med =0.0;

	}

	public double ints2mad(int[] ints) {
		double mad = 0.0;


		return mad;
	}

	public File[] fileSortByName(File[] files) {
		java.util.Arrays.sort(files, new java.util.Comparator<File>() {
			public int compare(File file1, File file2) {
				return file1.getName().compareTo(file2.getName());
			}
		});
		return files;
	}
}
