package cw.sprboot.dpiqb.circular;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class ParentABComponent {
  private final ComponentA componentA;
  private final ComponentB componentB;
  @PostConstruct
  public void init(){
    componentA.print();
    componentB.print();
  }
}
