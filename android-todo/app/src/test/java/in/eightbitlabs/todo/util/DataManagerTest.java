package in.eightbitlabs.todo.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import in.eightbitlabs.todo.TestDataFactory;
import in.eightbitlabs.todo.data.DataManager;
import in.eightbitlabs.todo.data.local.DatabaseHelper;
import in.eightbitlabs.todo.data.local.PreferencesHelper;
import in.eightbitlabs.todo.data.remote.TodoService;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock
    TodoService mMockTodoService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockTodoService, mMockPreferencesHelper,
                mMockDatabaseHelper);
    }

    @Test
    public void syncRibotsEmitsValues() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        TestSubscriber<Ribot> result = new TestSubscriber<>();
        mDataManager.syncTasks().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(ribots);
    }

    @Test
    public void syncRibotsCallsApiAndDatabase() {
        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
                TestDataFactory.makeRibot("r2"));
        stubSyncRibotsHelperCalls(ribots);

        mDataManager.syncTasks().subscribe();
        // Verify right calls to helper methods
        verify(mMockTodoService).getTasks();
        verify(mMockDatabaseHelper).setTasks(ribots);
    }

    @Test
    public void syncRibotsDoesNotCallDatabaseWhenApiFails() {
        when(mMockTodoService.getTasks())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mDataManager.syncTasks().subscribe(new TestSubscriber<Ribot>());
        // Verify right calls to helper methods
        verify(mMockTodoService).getTasks();
        verify(mMockDatabaseHelper, never()).setTasks(anyListOf(Ribot.class));
    }

    private void stubSyncRibotsHelperCalls(List<Ribot> ribots) {
        // Stub calls to the ribot service and database helper.
        when(mMockTodoService.getTasks())
                .thenReturn(Observable.just(ribots));
        when(mMockDatabaseHelper.setTasks(ribots))
                .thenReturn(Observable.from(ribots));
    }

}
