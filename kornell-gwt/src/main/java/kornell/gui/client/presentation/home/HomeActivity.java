package kornell.gui.client.presentation.home;

import kornell.gui.client.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class HomeActivity extends AbstractActivity {
	
	static HomePresenter presenter;
	public HomeActivity(ClientFactory clientFactory) {
	    if(presenter == null){
	    	presenter = new HomePresenter(clientFactory);
	    }
	  }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		GWT.log("Startint a home"); 
		panel.setWidget(presenter);
		
	}
}
