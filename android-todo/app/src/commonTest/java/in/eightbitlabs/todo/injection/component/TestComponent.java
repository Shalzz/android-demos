package in.eightbitlabs.todo.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import in.eightbitlabs.todo.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
