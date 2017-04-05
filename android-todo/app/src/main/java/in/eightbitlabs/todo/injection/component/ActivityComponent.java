package in.eightbitlabs.todo.injection.component;

import dagger.Subcomponent;
import in.eightbitlabs.todo.injection.PerActivity;
import in.eightbitlabs.todo.injection.module.ActivityModule;
import in.eightbitlabs.todo.ui.main.MainActivity;
import in.eightbitlabs.todo.ui.tasks.TasksFragment;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(TasksFragment tasksFragment);
}
