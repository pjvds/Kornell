package kornell.gui.client.presentation.welcome.generic;

import java.math.BigDecimal;

import kornell.api.client.Callback;
import kornell.api.client.KornellClient;
import kornell.core.shared.data.CourseTO;
import kornell.core.shared.data.CoursesTO;
import kornell.gui.client.KornellConstants;
import kornell.gui.client.presentation.bar.ActivityBarView;
import kornell.gui.client.presentation.bar.MenuBarView;
import kornell.gui.client.presentation.bar.generic.GenericActivityBarView;
import kornell.gui.client.presentation.welcome.WelcomePlace;
import kornell.gui.client.presentation.welcome.WelcomeView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

//TODO - Courses will overflow the screen

public class GenericWelcomeView extends Composite implements WelcomeView {
	interface MyUiBinder extends UiBinder<Widget, GenericWelcomeView> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	FlowPanel pnlInProgress;
	@UiField
	FlowPanel pnlToStart;
	@UiField
	FlowPanel pnlToAcquire;
	@UiField
	FlowPanel pnlFinished;
	@UiField
	FlowPanel pnlCoursesInProgress;
	@UiField
	FlowPanel pnlCoursesToStart;
	@UiField
	FlowPanel pnlCoursesToAcquire;
	@UiField
	FlowPanel pnlCoursesFinished;
	@UiField
	Label labelCoursesInProgress;
	@UiField
	Label labelCoursesToStart;
	@UiField
	Label labelCoursesToAcquire;
	@UiField
	Label labelCoursesFinished;
	@UiField
	Label labelCoursesInProgressCount;
	@UiField
	Label labelCoursesToStartCount;
	@UiField
	Label labelCoursesToAcquireCount;
	@UiField
	Label labelCoursesFinishedCount;
	@UiField
	VerticalPanel pnlMenuLeft;
	@UiField
	Button btnCoursesAll;
	@UiField
	Button btnCoursesInProgress;
	@UiField
	Button btnCoursesToStart;
	@UiField
	Button btnCoursesToAcquire;
	@UiField
	Button btnCoursesFinished;
	

	private static String COURSES_ALL = "all";
	private static String COURSES_IN_PROGRESS = "inProgress";
	private static String COURSES_TO_START = "toStart";
	private static String COURSES_TO_ACQUIRE = "toAcquire";
	private static String COURSES_FINISHED = "finished";
	
	private KornellClient client;

	private PlaceController placeCtrl;
	
	private String displayCourses;

	private final EventBus eventBus = new SimpleEventBus();

	private KornellConstants constants = GWT.create(KornellConstants.class);
	
	private GenericMenuLeftView menuLeftView;
	
	
	public GenericWelcomeView(KornellClient client, PlaceController placeCtrl) {
		this.client = client;
		this.placeCtrl = placeCtrl;
		initWidget(uiBinder.createAndBindUi(this));
		initData();		
	}
	
	private void initData() {
		client.getCourses(new Callback<CoursesTO>() {
			@Override
			protected void ok(CoursesTO to) {
				if(displayCourses == null)
					displayCourses = COURSES_ALL;
				display(to);
			}
		});
	}


	private void display(CoursesTO to) {
		if(menuLeftView == null){
			menuLeftView = new GenericMenuLeftView(client, placeCtrl);
			pnlMenuLeft.add(menuLeftView);
		}
		clearPanels();
		for (final CourseTO course : to.getCourses()) {
			GenericCourseSummaryView courseSummaryView = new GenericCourseSummaryView(placeCtrl,course);
			if(course.getEnrollment().getProgress() == null){
				pnlCoursesToAcquire.add(courseSummaryView);
			}
			else if((course.getEnrollment().getProgress().compareTo(BigDecimal.valueOf(1)) == 0)){
				pnlCoursesFinished.add(courseSummaryView);
			}
			else if(!(course.getEnrollment().getProgress().compareTo(BigDecimal.valueOf(0)) == 0)){
				pnlCoursesInProgress.add(courseSummaryView);
			}
			else{
				pnlCoursesToStart.add(courseSummaryView);
			}
		}
		disablePanels();
		refreshButtonsSelection();
		btnCoursesAll.setText(constants.allCourses());

		if(pnlCoursesInProgress.getWidgetCount() > 0){
			labelCoursesInProgress.setText(constants.coursesInProgress());
			btnCoursesInProgress.setText(constants.inProgress());
			labelCoursesInProgressCount.setText(""+pnlCoursesInProgress.getWidgetCount());
		} else {
			pnlInProgress.setVisible(false);
			btnCoursesInProgress.setVisible(false);
		}
		
		
		if(pnlCoursesToStart.getWidgetCount() > 0){
			labelCoursesToStart.setText(constants.coursesToStart());
			btnCoursesToStart.setText(constants.toStart());
			labelCoursesToStartCount.setText(""+pnlCoursesToStart.getWidgetCount());
		} else {
			pnlToStart.setVisible(false);
			btnCoursesToStart.setVisible(false);
		}
		
		
		if(pnlCoursesToAcquire.getWidgetCount() > 0){
			labelCoursesToAcquire.setText(constants.coursesToAcquire());
			btnCoursesToAcquire.setText(constants.toAcquire());
			labelCoursesToAcquireCount.setText(""+pnlCoursesToAcquire.getWidgetCount());
		} else {
			pnlToAcquire.setVisible(false);
			btnCoursesToAcquire.setVisible(false);
		}
		
		
		if(pnlCoursesFinished.getWidgetCount() > 0){
			labelCoursesFinished.setText(constants.coursesFinished());
			btnCoursesFinished.setText(constants.finished());
			labelCoursesFinishedCount.setText(""+pnlCoursesFinished.getWidgetCount());
		} else {
			pnlFinished.setVisible(false);
			btnCoursesFinished.setVisible(false);
		}
		
	}

	@UiHandler("btnCoursesAll")
	void handleClickAll(ClickEvent e) {
		this.displayCourses = COURSES_ALL;
		initData();
	}

	@UiHandler("btnCoursesInProgress")
	void handleClickInProgress(ClickEvent e) {
		this.displayCourses = COURSES_IN_PROGRESS;
		initData();
	}

	@UiHandler("btnCoursesToAcquire")
	void handleClickToAcquire(ClickEvent e) {
		this.displayCourses = COURSES_TO_ACQUIRE;
		initData();
	}

	@UiHandler("btnCoursesToStart")
	void handleClickToStart(ClickEvent e) {
		this.displayCourses = COURSES_TO_START;
		initData();
	}

	@UiHandler("btnCoursesFinished")
	void handleClickFinished(ClickEvent e) {
		this.displayCourses = COURSES_FINISHED;
		initData();
	}

	private void refreshButtonsSelection() {
		btnCoursesAll.removeStyleName("btnSelected");
		btnCoursesInProgress.removeStyleName("btnSelected");
		btnCoursesToStart.removeStyleName("btnSelected");
		btnCoursesToAcquire.removeStyleName("btnSelected");
		btnCoursesFinished.removeStyleName("btnSelected");
		btnCoursesAll.addStyleName("btnNotSelected");
		btnCoursesInProgress.addStyleName("btnNotSelected");
		btnCoursesToStart.addStyleName("btnNotSelected");
		btnCoursesToAcquire.addStyleName("btnNotSelected");
		btnCoursesFinished.addStyleName("btnNotSelected");
		
		if(GenericWelcomeView.COURSES_ALL.equals(displayCourses)){
			btnCoursesAll.addStyleName("btnSelected");
			btnCoursesAll.removeStyleName("btnNotSelected");
		} else if(COURSES_IN_PROGRESS.equals(displayCourses)){
			btnCoursesInProgress.addStyleName("btnSelected");
			btnCoursesInProgress.removeStyleName("btnNotSelected");
		} else if(COURSES_TO_START.equals(displayCourses)){
			btnCoursesToStart.addStyleName("btnSelected");
			btnCoursesToStart.removeStyleName("btnNotSelected");
		} else if(COURSES_TO_ACQUIRE.equals(displayCourses)){
			btnCoursesToAcquire.addStyleName("btnSelected");
			btnCoursesToAcquire.removeStyleName("btnNotSelected");
		} else if(COURSES_FINISHED.equals(displayCourses)){
			btnCoursesFinished.addStyleName("btnSelected");
			btnCoursesFinished.removeStyleName("btnNotSelected");
		}
	}

	private void disablePanels() {
		pnlInProgress.setVisible(true);
		pnlToStart.setVisible(true);
		pnlToAcquire.setVisible(true);
		pnlFinished.setVisible(true);
		if(COURSES_IN_PROGRESS.equals(displayCourses)){
			pnlToStart.setVisible(false);
			pnlToAcquire.setVisible(false);
			pnlFinished.setVisible(false);
		} else if(COURSES_TO_ACQUIRE.equals(displayCourses)){
			pnlInProgress.setVisible(false);
			pnlToStart.setVisible(false);
			pnlFinished.setVisible(false);
		} else if(COURSES_TO_START.equals(displayCourses)){
			pnlInProgress.setVisible(false);
			pnlToAcquire.setVisible(false);
			pnlFinished.setVisible(false);
		} else if(COURSES_FINISHED.equals(displayCourses)){
			pnlInProgress.setVisible(false);
			pnlToStart.setVisible(false);
			pnlToAcquire.setVisible(false);
		}
	}

	private void clearPanels() {
		pnlCoursesToAcquire.clear();
		pnlCoursesFinished.clear();
		pnlCoursesInProgress.clear();
		pnlCoursesToStart.clear();
	}

	@Override
	public void setPresenter(Presenter presenter) {
	}

}