package com.concurrency.locks;

public class MyReadWriteLockExample {
	
	int counter = 8;
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	MyReadWriteLock readWriteBlock = new MyReadWriteLock();
	
	int readBlock(){
		int returnValue = 0;
		try {
			readWriteBlock.lockRead();
			returnValue = getCounter();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			readWriteBlock.unLockRead();
		}
		return returnValue;
		
	}
	
	
	void writeBlock(){
		
		try {
			readWriteBlock.lockWrite();
			setCounter(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			readWriteBlock.unLockWrite();
		}
		
		
	}
	
	public static void main(String args[]){
		MyReadWriteLockExample myReadWriteLockExample = new MyReadWriteLockExample();
		
		Thread thread1 = new Thread( new Runnable() {
			
			@Override
			public void run() {
				myReadWriteLockExample.readBlock();
				
			}
		});
		thread1.start();
		
		Thread thread2 = new Thread( new Runnable() {
			
			@Override
			public void run() {
				System.out.println(myReadWriteLockExample.readBlock());
				
			}
		});
		thread2.start();
	}

}

class MyReadWriteLock{
	
	int readers;
	int writers;
	int writerRequest;
	
	
	public synchronized  void lockRead() throws InterruptedException{
		
		if(writers > 0 || writerRequest > 0){
			wait();
		}
		
		readers ++ ;
		
	}
	
	public synchronized void unLockRead(){
		readers--;
		notifyAll();
		
	}
	
	public synchronized void lockWrite() throws InterruptedException{
		writerRequest ++ ;
		if(writers >0 || readers > 0 ){
			wait();
		}
		
		writers ++;
		writerRequest--;
	}
	
	public synchronized void unLockWrite(){
		writers--;
		notifyAll();
		
	}
}
