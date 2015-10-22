package kr.ac.uos.ai.ieas.abstractClass;

import java.lang.reflect.Method;
import java.util.ArrayList;

import kr.ac.uos.ai.ieas.alerter.alerterController.AbstractPropertyChangeListener;


public abstract class AbstractController {

	public ArrayList<AbstractView> registeredViews;
	public ArrayList<AbstractModel> registeredModels;
	
	private AbstractPropertyChangeListener abstractPropertyChangeListener;

	public AbstractController() {
		registeredViews = new ArrayList<AbstractView>();
		registeredModels = new ArrayList<AbstractModel>();
		abstractPropertyChangeListener = new AbstractPropertyChangeListener(this);
	}

	public void addModel(AbstractModel model) {
		registeredModels.add(model);
		model.addPropertyChangeListener(abstractPropertyChangeListener);
	}

	public void removeModel(AbstractModel model) {
		registeredModels.remove(model);
		model.removePropertyChangeListener(abstractPropertyChangeListener);
	}

	public void addView(AbstractView view) {
		registeredViews.add(view);
	}

	public void removeView(AbstractView view) {
		registeredViews.remove(view);
	}


	protected void setModelProperty(String propertyName, Object newValue) {
		
		for (AbstractModel model: registeredModels) {
			try {

				Method method = model.getClass().
						getMethod("set"+propertyName, new Class[] {
								newValue.getClass()
						}
								);
				method.invoke(model, newValue);
			} catch (Exception ex) {
				System.out.println("there is no method name of " + "set"+propertyName);
			}
		}
	}

	public ArrayList<AbstractView> getRegisteredViews() {
		return this.registeredViews;		
	}
	
	public ArrayList<AbstractModel> getRegisteredModels(){
		return this.registeredModels;
	}
}
