import java.util.Arrays;

public class Main {

    public static final int UPPER_BOUND = (int)(1e8) + 1;
		public static final int SQRT_MAX = (int)(1e4);
    public static final int NUM_THREADS = 8;
    public static final boolean[] primes = new boolean[UPPER_BOUND];

    public static void main(String[] args) throws Exception {

        // Size of each section
        Thread[] t = new Thread[NUM_THREADS];
        int cutoff = (UPPER_BOUND + NUM_THREADS - 1) / NUM_THREADS, j = 0;

				// time elapsed start
        long sum = 0, time = System.currentTimeMillis();

        // Start each thread
        for (int i = 0; i < NUM_THREADS; i++) {
            t[i] = new Thread(new PrimeRun(cutoff * i, cutoff * (i + 1)));
            t[i].start();
        } for (int i = 0; i < NUM_THREADS; i++) t[i].join();
				primes[0] = primes[1] = false;

				// time elapsed end
        time = System.currentTimeMillis() - time;

        // store top 10 primes
        int[] largest_primes = new int[10];

				// Go over found primes
        for (int i = UPPER_BOUND - 2; i >= 3; i -= 2) {
            if (!primes[i]) {
                sum += i;
								
								// add to top primes
                if (++j <= 10) largest_primes[10 - j] = i;
            }
        } sum += 2;

        System.out.println("Time elapsed:" + time + "ms\tSum of primes" + sum);
        for (int i = 0; i < 10; i++)
            System.out.print(largest_primes[i] + " ");
        System.out.println();
    }


		// Thread worker
    static class PrimeRun implements Runnable {
        

				// Worker bounadaries (responsibilities)
				private int lo, hi;
    
        public PrimeRun(int lo, int hi) {
            this.lo = lo;
            this.hi = Math.min(hi, UPPER_BOUND);
        }
    

        public void run() {
						int tmp = 0;

            for (int i = 3; i < SQRT_MAX; i += 2, tmp=i*i) {
                // Ensure starting point in bound
								if (tmp < lo) tmp = (int) Math.ceil(1.0 * lo / i) * i;

                // flag non-primes
                for (int j = tmp; j < hi; j += i) primes[j] = true;
            }
        }
    }
}