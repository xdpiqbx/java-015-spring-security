package cw.sprboot.dpiqb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

//@Component
public class TestComponent {
  public TestComponent(){
    System.out.println("In constructor of TestComponent");
  }

  @PostConstruct
  public void init(){
    System.out.println("In PostConstruct of TestComponent");
  }

  @PreDestroy
  public void onDestroy(){
    System.out.println("In @PreDestroy onDestroy");
  }
}
