package cw.sprboot.dpiqb;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor // from lombok
@Component
public class ParentComponent {
//  @Autowired // ask for autoinject (not recommended for fields)
  private final ChildComponent childComponent; // final - is required

  private final ApplicationContext applicationContext;

//  public ParentComponent(ChildComponent childComponent) {
//    this.childComponent = childComponent;
//  }

//  @Autowired
//  public void setChildComponent(ChildComponent childComponent) {
//    this.childComponent = childComponent;
//  }

  @PostConstruct
  public void init(){
    childComponent.hello();
    applicationContext.getBeansOfType(ChildComponent.class);

    ChildComponent newChildComp = applicationContext.getBean("childComponent", ChildComponent.class);
    newChildComp.hello();
  }
}
