package cw.sprboot.dpiqb.circular;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

//@Service
public class ComponentA {
//  private final ApplicationContext context;
//
//  public ComponentA(ApplicationContext context) {
//    this.context = context;
//    System.out.println("Create A");
//  }

//  @PostConstruct
//  public void init(){
//    System.out.println("@PostConstruct ComponentA");
//  }

//  private ComponentB getComponentB(){
////    return context.getBean("componentA", ComponentA.class);
//    return context
//      .getBeansOfType(ComponentB.class)
//      .values()
//      .stream()
//      .findFirst()
//      .orElse(null);
//  }

  public void print(){
    System.out.println("I am print from A");
  }
}
