package ij.plugin.psfx;

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


}