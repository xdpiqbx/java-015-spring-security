package cw.sprboot.dpiqb;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype") // singleton, request, session
// prototype - for every inject - new component
@Component
public class ChildComponent {
  public ChildComponent() {
    System.out.println("In ChildComponent constructor");
  }

  public void hello(){
    System.out.println("Hello i am ChildComponent");
  }
}
