package cw.sprboot.dpiqb.threadSafe;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
public class ComponentA {
//  public void print(List<String> items){
  public void print(){
//    ArrayList<String> itemsCopy = new ArrayList<>(items);
    // And work with copy ...
    try {
      System.out.println("I am print from A - START");
      Thread.sleep(5000L);
      System.out.println("I am print from A - END");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
