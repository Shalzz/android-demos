package in.eightbitlabs.guidesdemo.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import in.eightbitlabs.guidesdemo.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
