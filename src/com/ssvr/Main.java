package com.ssvr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    final private static ArrayList<Result> primes = new ArrayList<>();

    final static ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        BigInteger now = BigInteger.TWO;

        new Thread(()->{
            Scanner sc = new Scanner(System.in);

            while(true){
                switch (sc.nextLine()){
                    case ".count":
                        System.out.println(primes.size());
                        break;
                    case ".status":
                        System.out.println(exec);
                        break;
                    case ".last":
                        System.out.println(primes.get(primes.size()-1));
                    case ".tasks":
                        String[] temp = exec.toString().split("=");
                        System.out.println(Integer.valueOf(temp[3].replaceAll(" ", "").split(",")[0]));
                    default:
                        break;
                }
            }
        }).start();


        while(true){
            String[] temp = exec.toString().split("=");
            Future<Result> r = exec.submit(new __Runner(now));

            Result result = r.get();

            if(result.isPrime){
                //System.out.println(result.target);
                primes.add(result);
            }

            now = now.add(BigInteger.ONE);

            if(Integer.parseInt(temp[3].replaceAll(" ", "").split(",")[0]) > 100){
                Thread.sleep(10000);
            }
        }


    }

    private final static class __Runner implements Callable<Result> {

        private BigInteger target;

        public __Runner(){
            this(BigInteger.ONE);
        }

        public __Runner(BigInteger bigInteger){
            this.target = bigInteger;
        }

        @Override
        public Result call(){
            Result temp = new Result(isPrime(this.target),Thread.currentThread().getName(),this.target);
            return temp;
        }

        private boolean isPrime(BigInteger value) { //3. 소수 판별 함수
            for(BigInteger j = BigInteger.TWO; j.compareTo(value.sqrt()) <= 0; j = j.add(BigInteger.ONE)){ //숫자의 제곱근으로 나눠진 value의 값까지 판별
                if(value.mod(j).equals(BigInteger.ZERO)) { //4.만약 다른 숫자에 나눠진다면 소수가 아님 - > 거짓
                    return false;
                }
            }
            return true; // 5.for-loop에 모두 통과되면 소수 맞음
        }
    }

    private final static class Result {
        private boolean isPrime;

        private String workerId;

        private BigInteger target;

        public Result(boolean isP, String str, BigInteger target){
            this.isPrime = isP;
            this.workerId = str;
            this.target = target;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "isPrime=" + isPrime +
                    ", workerId='" + workerId + '\'' +
                    ", target=" + target +
                    '}';
        }
    }

}
