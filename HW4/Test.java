public class Test {
   public static void main(String[] arg)
   {
      MaxHeap maxHeap = new MaxHeap(15);
      maxHeap.insert(1);
      maxHeap.insert(4);
      maxHeap.insert(2);
      maxHeap.insert(5);
      maxHeap.insert(13);
      maxHeap.insert(6);
      maxHeap.insert(17);

      MinHeap minHeap = new MinHeap(15);
      minHeap.insert(308);
      minHeap.insert(8);
      minHeap.insert(554);
      minHeap.insert(44);
      minHeap.insert(53);
      minHeap.insert(12);
      minHeap.insert(23);
      minHeap.insert(34);

      maxHeap.print();
      System.out.println("The max is " + maxHeap.extractMax());
      System.out.println("now the min");
      minHeap.print();
      System.out.println("The min is " + minHeap.extractMin());
   }

} 