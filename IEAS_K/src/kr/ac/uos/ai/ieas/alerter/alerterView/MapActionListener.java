package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;

public class MapActionListener implements MapEventListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4211933103010426459L;
	
	IeasArcGisMap ieasArcGisMap;
	
	public MapActionListener(IeasArcGisMap ieasArcGisMap) {
		this.ieasArcGisMap = ieasArcGisMap;
	}

	
	@Override
	public void mapDispose(MapEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapExtentChanged(MapEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mapReady(MapEvent event) {
		SpatialReference mapSR = event.getMap().getSpatialReference();		
		System.out.println("The map spatial reference is wkid=" + mapSR.getID());
		Point point = GeometryEngine.project(126.984063, 37.535887, mapSR);
		PictureMarkerSymbol symbol = new PictureMarkerSymbol("http://static.arcgis.com/images/Symbols/Basic/RedShinyPin.png");
		Graphic pointGraphic = new Graphic(point, symbol);
		ieasArcGisMap.getGraphicsLayer().addGraphic(pointGraphic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("symbolButton")){
			ieasArcGisMap.getSymbolOverlay().setActive(true);
		}
	}	
}
