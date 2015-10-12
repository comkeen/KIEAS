package kr.ac.uos.ai.ieas.alerter.alerterView;


/* Copyright 2014 Esri
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/

import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import com.esri.map.GraphicsLayer;
import com.esri.map.MapOverlay;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

class SymbolOverlay extends MapOverlay {

  private static final long serialVersionUID = 1L;
  private GraphicsLayer graphicsLayer;
  private PictureMarkerSymbol symPoint;

  public SymbolOverlay(GraphicsLayer graphicsLayer) {
    this.graphicsLayer = graphicsLayer;
    symPoint = new PictureMarkerSymbol(
        "http://static.arcgis.com/images/Symbols/Basic/RedStickpin.png");
    symPoint.setSize(40, 40);
    symPoint.setOffsetY(20.0f);
  }

  @Override
  public void onMouseClicked(MouseEvent event) {

    if (SwingUtilities.isLeftMouseButton(event)) {
      // left-click
      if (event.getClickCount() == 1) {

        // get clicked point, convert to map point
        Point point = getMap().toMapPoint(event.getX(), event.getY());
        // show point graphic on map
        Graphic pointGraphic = new Graphic(point, symPoint);
        graphicsLayer.addGraphic(pointGraphic);
      }
    }
  }
}
