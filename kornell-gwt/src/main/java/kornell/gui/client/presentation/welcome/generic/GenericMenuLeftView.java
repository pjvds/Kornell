package kornell.gui.client.presentation.welcome.generic;


import java.math.BigDecimal;

import kornell.api.client.Callback;
import kornell.api.client.KornellClient;
import kornell.api.client.data.Person;
import kornell.core.shared.data.Course;
import kornell.core.shared.data.CourseTO;
import kornell.core.shared.data.CoursesTO;
import kornell.gui.client.presentation.atividade.AtividadePlace;
import kornell.gui.client.presentation.welcome.WelcomePlace;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class GenericMenuLeftView extends Composite {
	interface MyUiBinder extends UiBinder<Widget, GenericMenuLeftView> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	private KornellClient client;

	private PlaceController placeCtrl;
	
	private CoursesTO coursesTO;

	private final EventBus eventBus = new SimpleEventBus();

	@UiField
	FlowPanel menuLeftItemCourses;
	
	@UiField
	FlowPanel menuLeftItemNotifications;
	
	@UiField
	FlowPanel menuLeftItemMyParticipation;
	
	@UiField
	FlowPanel menuLeftItemProfile;

	GenericMenuLeftItemView genericMenuLeftItemCourses;
	
	GenericMenuLeftItemView genericMenuLeftItemNotifications;
	
	GenericMenuLeftItemView genericMenuLeftItemMyParticipation;
	
	GenericMenuLeftItemView genericMenuLeftItemProfile;

	
	public GenericMenuLeftView(KornellClient client, PlaceController placeCtrl) {
		this.client = client;
		this.placeCtrl = placeCtrl;
		/*menuLeftItemNotifications = new FlowPanel();
		menuLeftItemNotifications.sinkEvents(Event.ONCLICK);
		menuLeftItemNotifications.setTitle("Click me");
		//menuLeftItemNotifications.setSize("600px", "600px");
		menuLeftItemNotifications.addHandler(new ClickHandler(){

	        @Override
	        public void onClick(ClickEvent event) {
	        	System.out.println("wat");
	            Window.alert("SimplePanel clicked!");

	        }

	    }, ClickEvent.getType());*/
		initWidget(uiBinder.createAndBindUi(this));
		initData();		
	}

	private void initData() {
		
		
		client.getCourses(new Callback<CoursesTO>() {
			@Override
			protected void ok(CoursesTO to) {
				coursesTO = to;
				display();
			}
		});
	}
	
	public void clearSelection(){
		genericMenuLeftItemCourses.setUnselected();
		genericMenuLeftItemNotifications.setUnselected();
		genericMenuLeftItemMyParticipation.setUnselected();
		genericMenuLeftItemProfile.setUnselected();
	}

	private void display() {
		genericMenuLeftItemCourses = new GenericMenuLeftItemView(placeCtrl, GenericMenuLeftItemView.MENU_ITEM_COURSES, this);
		menuLeftItemCourses.add(genericMenuLeftItemCourses);
		
		genericMenuLeftItemNotifications = new GenericMenuLeftItemView(placeCtrl, GenericMenuLeftItemView.MENU_ITEM_NOTIFICATIONS, this);
		menuLeftItemNotifications.add(genericMenuLeftItemNotifications);
		
		genericMenuLeftItemMyParticipation = new GenericMenuLeftItemView(placeCtrl, GenericMenuLeftItemView.MENU_ITEM_MY_PARTICIPATION, this);
		menuLeftItemMyParticipation.add(genericMenuLeftItemMyParticipation);
		
		genericMenuLeftItemProfile = new GenericMenuLeftItemView(placeCtrl, GenericMenuLeftItemView.MENU_ITEM_PROFILE, this);
		menuLeftItemProfile.add(genericMenuLeftItemProfile);
	}

}
