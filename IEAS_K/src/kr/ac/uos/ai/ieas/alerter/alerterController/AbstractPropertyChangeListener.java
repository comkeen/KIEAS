package kr.ac.uos.ai.ieas.alerter.alerterController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import kr.ac.uos.ai.ieas.abstractClass.AbstractController;
import kr.ac.uos.ai.ieas.abstractClass.AbstractView;

public class AbstractPropertyChangeListener implements PropertyChangeListener{
	
	private AbstractController abstractController;
	
	public AbstractPropertyChangeListener(AbstractController abstractController) {
		this.abstractController = abstractController;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		for (AbstractView view: abstractController.getRegisteredViews()) {
			view.modelPropertyChange(evt);
		}
	}
}
