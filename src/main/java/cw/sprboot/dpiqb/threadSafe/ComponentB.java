package cw.sprboot.dpiqb.threadSafe;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

//@Service
public class ComponentB {
  public void print(){
    System.out.println("I am print from B");
  }
}
