package in.eightbitlabs.todo.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import in.eightbitlabs.todo.TestDataFactory;
import in.eightbitlabs.todo.data.DataManager;
import in.eightbitlabs.todo.ui.main.MainMvpView;
import in.eightbitlabs.todo.ui.main.MainPresenter;
import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock MainMvpView mMockMainMvpView;
    @Mock DataManager mMockDataManager;
    private MainPresenter mMainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mMainPresenter = new MainPresenter(mMockDataManager);
        mMainPresenter.attachView(mMockMainMvpView);
    }

    @After
    public void tearDown() {
        mMainPresenter.detachView();
    }

    @Test
    public void loadRibotsReturnsRibots() {
        List<Ribot> ribots = TestDataFactory.makeListRibots(10);
        when(mMockDataManager.getTasks())
                .thenReturn(Observable.just(ribots));

        mMainPresenter.loadRibots();
        verify(mMockMainMvpView).showRibots(ribots);
        verify(mMockMainMvpView, never()).showTasksEmpty();
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mMockDataManager.getTasks())
                .thenReturn(Observable.just(Collections.<Ribot>emptyList()));

        mMainPresenter.loadRibots();
        verify(mMockMainMvpView).showTasksEmpty();
        verify(mMockMainMvpView, never()).showRibots(anyListOf(Ribot.class));
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsFails() {
        when(mMockDataManager.getTasks())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mMainPresenter.loadRibots();
        verify(mMockMainMvpView).showError();
        verify(mMockMainMvpView, never()).showTasksEmpty();
        verify(mMockMainMvpView, never()).showRibots(anyListOf(Ribot.class));
    }

}