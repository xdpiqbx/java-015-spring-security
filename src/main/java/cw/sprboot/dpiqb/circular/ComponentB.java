package cw.sprboot.dpiqb.circular;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

//@Service
public class ComponentB {
//  private final ComponentA componentA;
//  private final ApplicationContext context;
//
//  public ComponentB(ApplicationContext context) {
//    this.context = context;
//    System.out.println("Create B");
//  }
//
//  @PostConstruct
//  public void init(){
//    getComponentA().print();
//  }
//
//  private ComponentA getComponentA(){
////    return context.getBean("componentA", ComponentA.class);
//    return context
//      .getBeansOfType(ComponentA.class)
//      .values()
//      .stream()
//      .findFirst()
//      .orElse(null);
//  }

  public void print(){
    System.out.println("I am print from B");
  }
}
