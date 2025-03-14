import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

class Sieve {
		int n;
		boolean[] sieve;
		
		public Sieve(int n){
				this.n = n;
				sieve = new boolean[n + 1];
				init();
		}

		private void init(){
				Arrays.fill(sieve, true);
				sieve[0] = sieve[1] = false;
				for (int p = 2; p * p <= n; p++){
						if( sieve[p] ){
								for (int i = p * p; i <=n; i += p){
										sieve[i] = false;
								}
						}
				}
		}

		boolean isPrime(int n){
				return sieve[n];
		}
}

class SieveTests {
		/*
		 * First 10 primes are <= 29
		 * First 50 primes are <= 229
		 * First 100 primes are <=541
		 * First 500 primes are <= 3571
		 * First 1000 primes are <= 7919
		 */
		Sieve sieve = new Sieve(229);
		@ParameterizedTest
		@ValueSource(ints = {3, 5, 229}) 
		void shouldBePrime(int n){
				assertTrue(sieve.isPrime(n));
		}
		
		@ParameterizedTest
		@ValueSource(ints = {1, 6, 228}) 
		void shouldNotBePrime(int n){
				assertFalse(sieve.isPrime(n));
		}

}
