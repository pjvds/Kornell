package kornell.gui.client.presentation.activity;


import com.google.gwt.user.client.ui.IsWidget;

public interface AtividadeView  extends IsWidget{
	public interface Presenter extends IsWidget {
	}

	void setPresenter(Presenter presenter);

}